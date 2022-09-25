package by.maksimruksha.mapgeneration.security.api.service;

import by.maksimruksha.mapgeneration.security.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String getUserName(String token);
    Boolean isValid(String token, UserDetails userDetails);

}
