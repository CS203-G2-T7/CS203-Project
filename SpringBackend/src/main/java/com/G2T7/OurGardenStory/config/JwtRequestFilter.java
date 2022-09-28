package com.G2T7.OurGardenStory.config;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwk.JwkException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  @Value(value = "${aws.cognito.jwk}")
  private String JWKURL;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String jwtToken = null;
    DecodedJWT decodedToken = null;
    String username = "";
    String kid = "";
    boolean valid = true;

    String requestTokenHeader = request.getHeader("Authorization");

    // JWT Token is in the form "Bearer token". Remove Bearer word and get
    // only the Token
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7); // Raw JWT string

      // get and decode JWTToken from HTTP request
      try {
        decodedToken = JWT.decode(jwtToken);
      } catch (JWTDecodeException e) {
        System.out.println(e);
        valid = false;
      }
      System.out.println();
      // System.out.println("ID: " + decodedToken.getId());
      System.out.println("kid: " + decodedToken.getKeyId());
      // System.out.println("iss: " + decodedToken.getIssuer());

      kid = decodedToken.getKeyId();
      username = decodedToken.getClaims().get("username").asString();

      // decodedToken.getClaims().forEach((key, claim) -> {
      // System.out.println("Key: " + key);
      // System.out.println("Claim: " + claim.toString());
      // });
      System.out.println("Username: " + username);
      valid = !decodedToken.getExpiresAt().before(Date.from(Instant.now())); // check expiry

    } else {
      valid = false;
    }

    // Get JWK Keyset from Amazon Cognito
    // Verify JWT Signature from request
    try {
      JwkProvider provider = new JwkProviderBuilder(new URL(JWKURL)).build(); // create JWK provider from Cognito URL.
      Jwk jwk = provider.get(kid); // get JWK from key id
      Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null); // algo based only on public key.

      Verification verifier = JWT.require(algorithm);
      verifier.build().verify(decodedToken); // check if signature match
    } catch (JWTVerificationException | JwkException | MalformedURLException e) {
      System.out.println("Verification error. JWT token is invalid.");
      System.out.println(e);
      valid = false;
    }
    if (!valid)
      System.out.println("JWT not valid.");
    else {
      // TODO: From here means JWT verified. Provide logic to authenticate route.
      System.out.println(username + " authentication successful!");
    }
    filterChain.doFilter(request, response);
  }
}
