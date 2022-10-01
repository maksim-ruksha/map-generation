package by.maksimruksha.mapgeneration.security.dto;

import lombok.Data;

@Data
public class RegisterDto {
    public String name;
    public String password;
    public String passwordRepeat;
}
