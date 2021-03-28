package nl.hu.cisq1.lingo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nl.hu.cisq1.lingo.security.filter.UserService;
import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import nl.hu.cisq1.lingo.security.data.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;

public class SecurityConfigHelper {

    public static String getAuthToken(SpringUserRepository userRepository) {
        UserService userService = new UserService(userRepository, mock(PasswordEncoder.class));

        try{
            User user = userService.loadUserByUsername("user4");
            userRepository.delete(user);
        }catch (Exception ignored){};

        userService.register("user4", "test123");
        return getToken();
    }

    public static String getToken(){
        String username = "user4";

        String secret = "\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImp0aSI6IjcyZDMyZTAzLTIxNzUtNGI3Mi1hOTQ5LWRjNzE0ZGE5OTM5YyIsImlhdCI6MTYxNjI0MjA2NiwiZXhwIjoxNjE2MjQ1NjY2fQ.kTXHnKPZDGYyNFpFnZrMA7Az1ScCwnM_5It1jRHEf0U\"";

        System.out.println("secret " + secret);
        int expDateMs = 4600000;
        List<String> roles = List.of("USER");

        byte[] signingKey = secret.getBytes();

        return "Bearer " + Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, Keys.hmacShaKeyFor(signingKey))
                .setHeaderParam("typ", "JWT")
                .setIssuer("hu-bep2-casino-api")
                .setAudience("hu-bep2-casino")
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expDateMs))
                .claim("rol", roles)
                .compact();
    }

}
