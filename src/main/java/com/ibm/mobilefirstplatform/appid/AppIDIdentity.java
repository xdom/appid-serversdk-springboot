package com.ibm.mobilefirstplatform.appid;

/**
 * @author Dominik Matta
 */
public class AppIDIdentity {
    private final String name;
    private final String email;
    private final String gender;
    private final String locale;
    private final String picture;
    private final String authBy;
    private final Object identities;

    public AppIDIdentity(String name, String email, String gender, String locale, String picture,
                         String authBy, Object identities) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.locale = locale;
        this.picture = picture;
        this.authBy = authBy;
        this.identities = identities;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getLocale() {
        return locale;
    }

    public String getPicture() {
        return picture;
    }

    public String getAuthBy() {
        return authBy;
    }

    public Object getIdentities() {
        return identities;
    }
}
