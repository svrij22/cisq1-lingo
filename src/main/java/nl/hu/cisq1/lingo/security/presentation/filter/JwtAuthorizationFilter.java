package nl.hu.cisq1.lingo.security.presentation.filter;


import io.jsonwebtoken.*;
import nl.hu.cisq1.lingo.security.data.UserProfile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tries to authorize a user, based on the Bearer token (JWT) from
 * the Authorization header of the incoming request.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final String secret;

    public JwtAuthorizationFilter(
            String secret,
            AuthenticationManager authenticationManager
    ) {
        super(authenticationManager);

        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = this.getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            return null;
        }

        if (!token.startsWith("Bearer ")) {
            return null;
        }

        byte[] signingKey = this.secret.getBytes();

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();

        Jws<Claims> parsedToken = jwtParser
                .parseClaimsJws(token.replace("Bearer ", ""));

        String username = parsedToken
                .getBody()
                .getSubject();

        List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                .get("rol")).stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());

        if (username.isEmpty()) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(new UserProfile(username), null, authorities);
    }
}