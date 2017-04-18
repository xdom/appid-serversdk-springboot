package sk.tuke.cloud.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Dominik Matta
 */
final class DefaultAppIDTokens implements AppIDTokens {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAppIDTokens.class);
    private static final String SCOPES = "scope";

    private final PublicKey publicKey;
    private final String accessToken;
    private final String identityToken;

    DefaultAppIDTokens(String accessToken, PublicKey publicKey) {
        this(accessToken, "", publicKey);
    }

    DefaultAppIDTokens(String accessToken, String identityToken, PublicKey publicKey) {
        Objects.requireNonNull(publicKey);
        Objects.requireNonNull(accessToken);
        Objects.requireNonNull(identityToken);
        this.publicKey = publicKey;
        this.accessToken = accessToken;
        this.identityToken = identityToken;
    }

    @Override
    public Authentication getAuthentication() {
        Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(accessToken)
                .getBody();
        Collection<? extends GrantedAuthority> authorities = getGrantedAuthorities(claims);
        User principal = new User(claims.getSubject(), "", authorities);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, "", authorities
        );
        return processIdentityToken(authentication);
    }

    private UsernamePasswordAuthenticationToken processIdentityToken(UsernamePasswordAuthenticationToken authentication) {
        if (StringUtils.isEmpty(identityToken)) {
            return authentication;
        }
        Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(identityToken)
                .getBody();
        AppIDIdentity identity = new AppIDIdentity(
                claims.get("name", String.class), claims.get("email", String.class),
                claims.get("gender", String.class), claims.get("locale", String.class),
                claims.get("picture", String.class), claims.get("auth_by", String.class),
                claims.get("identities")
        );
        authentication.setDetails(identity);
        return authentication;
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Claims claims) {
        return Arrays.stream(claims.get(SCOPES).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validate() {
        return validateToken(accessToken)
                && (StringUtils.isEmpty(identityToken) || validateToken(identityToken));
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOG.info("Invalid JWT signature.");
            LOG.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            LOG.info("Invalid JWT token.");
            LOG.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            LOG.info("Expired JWT token.");
            LOG.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            LOG.info("Unsupported JWT token.");
            LOG.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            LOG.info("JWT token compact of handler are invalid.");
            LOG.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
