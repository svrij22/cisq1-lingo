package nl.hu.cisq1.lingo.security.presentation;

import nl.hu.cisq1.lingo.security.application.UserService;
import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("leaderboard")
    public List<UserDto> getLeaderboard() {
        return service.getLeaderboard()
                .stream()
                .map(u -> new UserDto(u.getUsername(), u.getGame().getScore()))
                .collect(Collectors.toList());
    }
}
