package nl.hu.cisq1.lingo.game.presentation;


import nl.hu.cisq1.lingo.game.application.GameService;
import nl.hu.cisq1.lingo.game.domain.Game;
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
    public GameDto getNewGame() {
        return getGameDto(this.service.getNewGame());
    }


    @GetMapping("reset")
    public GameDto resetGame(@RequestParam Integer id) {
        try {
            return getGameDto(this.service.resetGame(id));
        } catch (Exception ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage()); }
    }

    @PostMapping("guess")
    public GameDto getRandomWord(@RequestParam Integer id,
                                @RequestParam String guess) {
        try {
            return getGameDto(this.service.gameDoGuess(id, guess));
        } catch (Exception ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage()); }
    }

    public GameDto getGameDto(Game game){
        return new GameDto(
                game.getId(),
                game.getCurrentRound().getGuesses(),
                game.getScore(),
                game.getState(),
                game.getCurrentRound().getWordToGuess());
    }
}
