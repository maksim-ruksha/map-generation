package by.maksimruksha.mapgeneration.security.api.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String getUserName(String token);
    Boolean isValid(String token, UserDetails userDetails);

}
