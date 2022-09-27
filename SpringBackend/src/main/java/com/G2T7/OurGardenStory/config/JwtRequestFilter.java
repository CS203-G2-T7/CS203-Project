package com.G2T7.OurGardenStory.config;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwk.SigningKeyNotFoundException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  @Value(value = "${aws.cognito.jwk}")
  private String JWKURL;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
    String requestTokenHeader = request.getHeader("Authorization");

    String jwtToken = null;
    DecodedJWT decodedToken = null;
    String username = "";
    String kid = "";
    boolean valid = true;

    // JWT Token is in the form "Bearer token". Remove Bearer word and get
    // only the Token
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7);
      // System.out.println("Access Token: " + jwtToken);

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
      System.out.println(username + " authentication successful!");
    }

    filterChain.doFilter(request, response);
  }
}
