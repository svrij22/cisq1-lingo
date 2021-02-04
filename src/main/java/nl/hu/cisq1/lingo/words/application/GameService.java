package nl.hu.cisq1.lingo.words.application;


import nl.hu.cisq1.lingo.words.data.GameRepository;
import nl.hu.cisq1.lingo.words.domain.Game;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.GameIdNotFound;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotEqual;
import org.hibernate.annotations.CreationTimestamp;
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

    public Game gameDoGuess(Integer id, String guess) throws Exception {
        Game game = gameRepository.findById(id).orElseThrow(GameIdNotFound::new);
        if (game.getWord().getValue().length() != guess.length()) throw new WordLengthNotEqual();
        game.doGuess(guess);
        gameRepository.save(game);
        return game;
    }

    public void checkGame(){

    }
}
