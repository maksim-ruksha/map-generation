package by.maksimruksha.mapgeneration.security.api.service;

import by.maksimruksha.mapgeneration.security.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    Boolean isValid(String token, UserDetails userDetails);
}
