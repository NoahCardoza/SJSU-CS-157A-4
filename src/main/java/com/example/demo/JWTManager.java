package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTManager {
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private static JWTManager instance;
    private JWTManager() {
        String secret = Env.get("JWT_SECRET");

        algorithm = Algorithm.HMAC256(secret);

        verifier = JWT.require(algorithm)
                .withIssuer("littlehiddengems.link")
                .build();
    }

    public static JWTManager getInstance() {
        if (instance == null) {
            instance = new JWTManager();
        }
        return instance;
    }

    public String createEmailVerificationToken(Long userId) {
        return JWT.create()
                .withIssuer("littlehiddengems.link")
                .withClaim("user_id", userId)
                .sign(algorithm);
    }

    public Long verifyEmailVerificationToken(String token) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("user_id").asLong();
        } catch (JWTVerificationException exception){
            return -1L;
        }
    }
}
