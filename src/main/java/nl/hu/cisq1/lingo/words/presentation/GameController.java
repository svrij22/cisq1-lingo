package nl.hu.cisq1.lingo.words.presentation;


import nl.hu.cisq1.lingo.words.application.GameService;
import nl.hu.cisq1.lingo.words.application.WordService;
import nl.hu.cisq1.lingo.words.domain.Game;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("game")
public class GameController {
    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping("new")
    public Game getNewGame() {
        return this.service.getNewGame();
    }

    @PostMapping("guess")
    public Game getRandomWord(@RequestParam Integer id,
                                @RequestParam String guess) {
        try {
            return this.service.gameDoGuess(id, guess);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
