package com.ibm.mobilefirstplatform.appid;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class JWTFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(JWTFilter.class);

    private PublicKeyProvider publicKeyProvider;

    JWTFilter(PublicKeyProvider publicKeyProvider) {
        this.publicKeyProvider = publicKeyProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            AppIDTokens tokens = resolveTokens(httpServletRequest);
            if (tokens.validate()) {
                Authentication authentication = tokens.getAuthentication();
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ExpiredJwtException eje) {
            LOG.info("Security exception for user {} - {}",
                    eje.getClaims().getSubject(), eje.getMessage());

            LOG.trace("Security exception trace: {}", eje);
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private AppIDTokens resolveTokens(HttpServletRequest request) {
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String[] tokenParts = bearerToken.split(" ");
            PublicKey publicKey = publicKeyProvider.getPublicKey();
            if (tokenParts.length == 2) {
                return new DefaultAppIDTokens(tokenParts[1], publicKey);
            } else if (tokenParts.length == 3) {
                return new DefaultAppIDTokens(tokenParts[1], tokenParts[2], publicKey);
            }
        }
        LOG.warn("Malformed authorization header {}", JWTConfigurer.AUTHORIZATION_HEADER);
        return new NullAppIDTokens();
    }

}
