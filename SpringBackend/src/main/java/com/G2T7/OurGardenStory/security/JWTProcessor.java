package com.G2T7.OurGardenStory.security;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

import java.net.MalformedURLException;
import java.net.URL;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JWTProcessor {
    @Autowired
    private JWTConfiguration jwtConfiguration;

    /**
     * Return Configurable JWT Processor used for processing JWT tokens
     * @return ConfigurableJWTProcessor
     * @throws MalformedURLException
     */
    @Bean
    public ConfigurableJWTProcessor configurableJWTProcessor() throws MalformedURLException {
        ResourceRetriever resourceRetriever =
                new DefaultResourceRetriever(jwtConfiguration.getConnectionTimeout(),
                        jwtConfiguration.getReadTimeout());
        URL jwkSetURL= new URL(jwtConfiguration.getJwkUrl());
        JWKSource keySource= new RemoteJWKSet(jwkSetURL, resourceRetriever);
        ConfigurableJWTProcessor jwtProcessor= new DefaultJWTProcessor();
        JWSKeySelector keySelector= new JWSVerificationKeySelector(RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);
        return jwtProcessor;
    }
}
