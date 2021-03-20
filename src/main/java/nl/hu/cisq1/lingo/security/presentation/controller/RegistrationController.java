package nl.hu.cisq1.lingo.security.presentation.controller;

import nl.hu.cisq1.lingo.security.application.UserService;
import nl.hu.cisq1.lingo.security.presentation.dto.AuthDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void register(@Validated @RequestBody AuthDto registration, HttpServletResponse response) {
        try{
            this.userService.register(
                    registration.username,
                    registration.password
            );
            response.setStatus(200);
        }catch (Exception ignored){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}