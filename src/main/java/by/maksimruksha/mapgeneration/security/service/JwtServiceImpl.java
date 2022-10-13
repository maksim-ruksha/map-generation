package by.maksimruksha.mapgeneration.security.service;

import by.maksimruksha.mapgeneration.security.api.service.JwtService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    // TODO: change this
    private final long JWT_TOKEN_TIME = 1000L * 1000 * 60 * 60; // 1000 hours

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(UserDetails userDetails) {
        Date expirationDate = new Date(System.currentTimeMillis() + JWT_TOKEN_TIME);

        Map<String, String> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @Override
    public String getUserName(String token) {
        JwtParser jwtParser = getTokenParser();
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public Boolean isValid(String token, UserDetails userDetails) {

        JwtParser jwtParser = getTokenParser();
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();

            String usernameFromToken = claims.getSubject();
            Date expirationDate = claims.getExpiration();

            Boolean isUsernameValid = userDetails.getUsername().equals(usernameFromToken);
            Boolean isExpired = expirationDate.before(new Date());

            return isUsernameValid && !isExpired;
        } catch (Exception e) {
            return false;
        }
    }

    private JwtParser getTokenParser()
    {
        return Jwts.parserBuilder().setSigningKey(secret).build();
    }

}
