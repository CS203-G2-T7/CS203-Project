package com.G2T7.OurGardenStory.security;

import java.util.Collection;

import com.nimbusds.jwt.JWTClaimsSet;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthentication extends AbstractAuthenticationToken {
    private final Object principal;
    private final JWTClaimsSet jwtClaimsSet;

    public JwtAuthentication(Object principal, JWTClaimsSet jwtClaimsSet, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.jwtClaimsSet = jwtClaimsSet;
        super.setAuthenticated(true);
    }

    /**
     * Get credentials object
     * @return Object
     */
    public Object getCredentials() {
        return null;
    }

    /**
     * Get principal object
     * @return Object
     */
    public Object getPrincipal() {
        return this.principal;
    }
}
