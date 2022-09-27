package com.G2T7.OurGardenStory.config;

import java.io.IOException;

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

import com.auth0.jwt.interfaces.DecodedJWT;

// import org.springframework.security.oauth2.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
    String requestTokenHeader = request.getHeader("Authorization");

    String username = null;
    String jwtToken = null;
    DecodedJWT decodedToken = null;

    // JWT Token is in the form "Bearer token". Remove Bearer word and get
    // only the Token
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7);

      try {
        decodedToken = JWT.decode(jwtToken);
      } catch (JWTDecodeException e) {
        System.out.println(e);
      }
      // username = jwtTokenUtil.getUsernameFromToken(jwtToken);
      // } catch (IllegalArgumentException e) {
      // System.out.println("Unable to get JWT Token");
      // } catch (ExpiredJwtException e) {
      // System.out.println("JWT Token has expired");
      // }
      String header = decodedToken.getHeader();
      String payload = decodedToken.getPayload();
      String sig = decodedToken.getSignature();
      // String id = decodedToken.getId();
      System.out.println(header + payload + sig);
    }

    // DecodedJWT decodedJWT = JWT.decode(jwtToken);

  }
}

// try {
// DecodedJWT decodedJWT = JWT.decode(jwt); // your string
// JwkProvider provider = new JwkProviderBuilder(new URL("JWKS URL")).build();
// Jwk jwk = provider.get(decodedJWT.getKeyId());
// Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(),
// null);

// Verification verifier = JWT.require(algorithm);
// verifier.build().verify(decodedJWT);
// } catch (JWTVerificationException | JwkException | MalformedURLException e) {
// // throw your exception
// }