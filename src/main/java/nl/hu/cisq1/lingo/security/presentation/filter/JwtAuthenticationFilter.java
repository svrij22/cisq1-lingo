package nl.hu.cisq1.lingo.security.presentation.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nl.hu.cisq1.lingo.security.data.User;
import nl.hu.cisq1.lingo.security.filter.LoginAttemptService;
import nl.hu.cisq1.lingo.security.presentation.dto.AuthDto;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tries to authenticate a user, based on the incoming request.
 *
 * Once authenticated, it will return a Bearer token (JWT) set in the
 * Authorization header of the 200 Response.
 *
 * This exact Bearer has to be added in the Authorization header of subsequent
 * requests to restricted endpoints.
 */
@CrossOrigin
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final String secret;
    private final Integer expirationInMs;

    private final AuthenticationManager authenticationManager;
    private LoginAttemptService loginAttemptService;

    public JwtAuthenticationFilter(
            String path,
            String secret,
            Integer expirationInMs,
            AuthenticationManager authenticationManager,
            LoginAttemptService loginAttemptService) {
        super(new AntPathRequestMatcher(path));

        this.secret = secret;
        this.expirationInMs = expirationInMs;
        this.authenticationManager = authenticationManager;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws IOException, RuntimeException {

        String ip = getClientIP(request);

        if (loginAttemptService.isBlocked(ip)) {
            response.setStatus(406);
            throw new RuntimeException("blocked");
        }

        AuthDto login = new ObjectMapper()
                .readValue(request.getInputStream(), AuthDto.class);

        Authentication authentication;
        try{
            authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.username, login.password));
            loginAttemptService.loginSucceeded(ip);
        }catch (Exception e){
            loginAttemptService.loginFailed(ip);
            throw e;
        }
        return authentication;
    }

    private String getClientIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = this.secret.getBytes();

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, Keys.hmacShaKeyFor(signingKey))
                .setHeaderParam("typ", "JWT")
                .setIssuer("hu-bep2-casino-api")
                .setAudience("hu-bep2-casino")
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + this.expirationInMs))
                .claim("rol", roles)
                .compact();

        response.addHeader("Authorization", "Bearer " + token);
    }
}