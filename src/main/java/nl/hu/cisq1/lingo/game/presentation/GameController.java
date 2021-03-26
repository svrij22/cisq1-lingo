package nl.hu.cisq1.lingo.game.presentation;


import nl.hu.cisq1.lingo.game.application.GameService;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.game.domain.Hint;
import nl.hu.cisq1.lingo.game.presentation.dto.GameDto;
import nl.hu.cisq1.lingo.security.data.UserProfile;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;

@RestController
@RequestMapping("game")
public class GameController {
    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping()
    public GameDto getGame(Authentication authentication) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        String username = details.getUsername();

        return getGameDto(this.service.getGameForUser(username));
    }

    @GetMapping("reset")
    public GameDto resetGame(Authentication authentication) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        String username = details.getUsername();
        try {
            return getGameDto(this.service.resetGame(username));
        } catch (Exception ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage()); }
    }

    @PostMapping("guess")
    public GameDto getRandomWord(Authentication authentication, @RequestParam String guess) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        String username = details.getUsername();
        try {
            return getGameDto(this.service.gameDoGuess(username, guess));
        } catch (Exception ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage()); }
    }

    public GameDto getGameDto(Game game){
        return new GameDto(
                game.getId(),
                game.getCurrentRound().getGuesses(),
                game.getScore(),
                game.getState(),
                new Word(game.getCurrentRound().getHint().hintStr()));
    }
}
