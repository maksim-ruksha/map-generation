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

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(UserDetails userDetails) {
        Date expirationDate = new Date(System.currentTimeMillis() + JWT_TOKEN_TIME);

        Map<String, String> claims = new HashMap<>();

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
        try {
            Claims claims = builder.build().parseClaimsJws(token).getBody();

            String usernameFromToken = claims.getSubject();
            Date expirationDate = claims.getExpiration();

            boolean isUsernameValid = userDetails.getName().equals(usernameFromToken);
            boolean isExpired = expirationDate.before(new Date());

            return isUsernameValid && !isExpired;
        } catch (Exception e) {
            return false;
        }
    }
}
