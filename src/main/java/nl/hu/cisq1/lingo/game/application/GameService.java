package nl.hu.cisq1.lingo.game.application;


import nl.hu.cisq1.lingo.words.application.WordService;
import nl.hu.cisq1.lingo.game.data.GameRepository;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.game.domain.exception.GameIdNotFound;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotEqual;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final WordService wordService;

    public GameService(GameRepository gameRepository, WordService wordService) {
        this.gameRepository = gameRepository;
        this.wordService = wordService;
    }

    public Game getNewGame(Word word) {
        Game game = new Game(word);
        gameRepository.save(game);
        return game;
    }

    public Game getNewGame(){
        Word word = wordService.provideRandomWord();
        return getNewGame(word);
    }

    public Game gameDoGuess(Integer id, String guess) {
        Game game = gameRepository.findById(id).orElseThrow(GameIdNotFound::new);
        game.doGuess(guess);
        gameRepository.save(game);
        return game;
    }

    public Game resetGame(Integer id) throws Exception {
        Game game = gameRepository.findById(id).orElseThrow(GameIdNotFound::new);
        Word word = wordService.provideRandomWord();
        game.resetGame(word);
        gameRepository.save(game);
        return game;
    }
}
