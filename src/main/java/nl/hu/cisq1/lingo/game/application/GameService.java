package nl.hu.cisq1.lingo.game.application;


import nl.hu.cisq1.lingo.security.filter.UserService;
import nl.hu.cisq1.lingo.security.data.User;
import nl.hu.cisq1.lingo.words.application.WordService;
import nl.hu.cisq1.lingo.game.data.GameRepository;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final UserService userService;
    private final WordService wordService;

    public GameService(GameRepository gameRepository, UserService userService, WordService wordService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.wordService = wordService;
    }

    public Game getGameForUser(String username){
        User user = userService.loadUserByUsername(username);
        Game game = user.getGame();
        if (game == null){
            game = getNewGame();
            user.setGame(game);
        }
        return game;
    }

    public Game getNewGame(){
        Word word = wordService.provideRandomWord();
        return getNewGame(word);
    }

    public Game getNewGame(Word word) {
        Game game = new Game(word);
        gameRepository.save(game);
        return game;
    }

    public Game gameDoGuess(String username, String guess) {
        Game game = getGameForUser(username);
        game.doGuess(guess);
        gameRepository.save(game);
        return game;
    }

    public Game resetGame(String username) throws Exception {
        Word word = wordService.provideRandomWord();
        return resetGame(word, username);
    }

    public Game resetGame(Word word, String username) {
        Game game = getGameForUser(username);
        game.resetGame(word);
        gameRepository.save(game);
        return game;
    }
}
