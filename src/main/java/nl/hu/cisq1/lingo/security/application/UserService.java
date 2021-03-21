package nl.hu.cisq1.lingo.security.application;

import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import nl.hu.cisq1.lingo.security.data.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 *  Implements UserDetailsService in order to make it usable
 *  as login/registration service for Spring.
 *  (see AuthenticationFilter)
 */
@Service
@Transactional
public class UserService implements UserDetailsService {
    private final SpringUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(SpringUserRepository repository, PasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String password) {
        String encodedPassword = this.passwordEncoder.encode(password);
        User user = new User(username, encodedPassword);
        this.userRepository.save(user);
    }

    public boolean login(String username, String password){
        User user = loadUserByUsername(username);
        return user.getPassword().equals(this.passwordEncoder.encode(password));
    }
    @Override
    public User loadUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Collection<User> getLeaderboard() {
        return userRepository.getTop10UsersByScore();
    }
}
