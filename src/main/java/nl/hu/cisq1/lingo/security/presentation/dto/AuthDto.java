package nl.hu.cisq1.lingo.security.presentation.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthDto {
    @NotBlank
    public String username;

    @Size(min = 5)
    public String password;

}
