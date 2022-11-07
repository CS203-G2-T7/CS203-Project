package com.G2T7.OurGardenStory.security;

import com.nimbusds.jwt.JWTClaimsSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static java.util.List.of;


@Component
public class AwsCognitoIdTokenProcessor {

    private final JWTProcessor jwtProcessor;
    private final JWTConfiguration jwtConfiguration;

    @Autowired
    public AwsCognitoIdTokenProcessor(JWTProcessor jwtProcessor, JWTConfiguration jwtConfiguration) {
        this.jwtProcessor = jwtProcessor;
        this.jwtConfiguration = jwtConfiguration;
    }


    /**
     * Check Http Servlet Request and return Authentication object
     * @param request Http Servlet Request
     * @return Authentication authenticated user's information
     * @throws Exception throws exception if unable to authenticate
     */
    public Authentication authenticate(HttpServletRequest request) throws Exception {
        String idToken = request.getHeader("Authorization");
        if (idToken != null) {
            JWTClaimsSet claims = this.jwtProcessor.configurableJWTProcessor().process(this.getBearerToken(idToken),null);
            validateIssuer(claims);
            verifyIfIdToken(claims);

            Object usernameObject = claims.getClaim("username");
            String username = usernameObject.toString();
            if (username != null) {
                List<GrantedAuthority> grantedAuthorities = of( new SimpleGrantedAuthority("ROLE_USER"));
                User user = new User(username, "", of());
                return new JwtAuthentication(user, claims, grantedAuthorities);
            }
        }
        return null;
    }

    /**
     * Validate that the issuer matches cognito's identity pool
     * @param claims JWTClaimsSet claims
     * @throws Exception throws exception of JWT Token is not an ID Token
     */
    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(), jwtConfiguration.getCognitoIdentityPoolUrl()));
        }
    }

    private void verifyIfIdToken(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception("JWT Token is not an ID Token");
        }
    }

    /**
     * Get the bearer token from the token
     * @param token JWT token
     * @return String
     */
    private String getBearerToken(String token) {
        return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
    }
}
