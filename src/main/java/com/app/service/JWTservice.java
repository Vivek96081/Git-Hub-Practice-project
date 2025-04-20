package com.app.service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JWTservice {

    @Value("${jwt.algorithms.key}")
    private String algorithmKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiry}")
    private int expirytime;

    private Algorithm algorithm;

    @PostConstruct
   public void postConstruct() throws UnsupportedEncodingException {
         algorithm = Algorithm.HMAC256(algorithmKey);
    }
   public String generateToken(String username){
     return   JWT.create()
               .withClaim("username",username)
               .withExpiresAt(new Date(System.currentTimeMillis()+expirytime))
               .withIssuer(issuer)
               .sign(algorithm);
   }
   public String getUsername(String token){
       DecodedJWT decodedToken = JWT.require(algorithm)
               .withIssuer(issuer)
               .build()
               .verify(token);
       return JWT.decode(token).getClaim("username").asString();
   }
}
