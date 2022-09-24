package by.maksimruksha.mapgeneration.security.service;

import by.maksimruksha.mapgeneration.security.UserDetails;
import by.maksimruksha.mapgeneration.security.api.service.JwtService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private final long JWT_TOKEN_TIME = 1 * 1000 * 60 * 60; // 1 hour
    private final String CLAIMS_KEY_PASSWORD = "password";

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(UserDetails userDetails) {
        Date expirationDate = new Date(System.currentTimeMillis() + JWT_TOKEN_TIME);

        Map<String, String> claims = new HashMap<>();
        claims.put(CLAIMS_KEY_PASSWORD, userDetails.getPassword());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getName())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @Override
    public Boolean isValid(String token, UserDetails userDetails) {
        JwtParserBuilder builder = Jwts.parserBuilder().setSigningKey(secret);
        Claims claims = builder.build().parseClaimsJws(token).getBody();

        if (claims.containsKey(CLAIMS_KEY_PASSWORD))
        {
            String usernameFromToken = claims.getSubject();
            String passwordFromToken = claims.get(CLAIMS_KEY_PASSWORD, String.class);
            Date expirationDate = claims.getExpiration();

            boolean isUsernameValid = userDetails.getName().equals(usernameFromToken);
            boolean isPasswordValid = userDetails.getPassword().equals(passwordFromToken);
            boolean isExpired = expirationDate.before(new Date());

            return isUsernameValid && isPasswordValid && isExpired;
        }

        return false;
    }
}
