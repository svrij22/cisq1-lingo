package nl.hu.cisq1.lingo.game.application;

import nl.hu.cisq1.lingo.game.data.GameRepository;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.security.application.UserService;
import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import nl.hu.cisq1.lingo.security.data.User;
import nl.hu.cisq1.lingo.words.application.WordService;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class GameServiceIntegrationTest {

    @Autowired GameRepository gameRepository;
    @Autowired SpringWordRepository wordRepository;
    @Autowired SpringUserRepository userRepository;

    @BeforeEach
    void setup(){
        UserService userService = new UserService(userRepository, mock(PasswordEncoder.class));
        userService.register("test", "test123");
    }

    @ParameterizedTest
    @DisplayName("Creates a new game from service and checks word")
    @MethodSource("randomWordExamples")
    void testGameService(Word word) {
        /*Mock repos*/
        GameService gameService = new GameService(gameRepository, mock(UserService.class), mock(WordService.class));

        /*Create game*/
        Game expected = new Game(word);
        Game game = gameService.getNewGame(word);

        /*Asserts equal word value*/
        assertEquals(game.getCurrentRound().getWordToGuess().getValue(), expected.getCurrentRound().getWordToGuess().getValue());
        assertEquals(game.getCurrentRound().getWordToGuess().getValue(), word.getValue());
    }

    @ParameterizedTest
    @DisplayName("Creates a new game from service and guesses the right word")
    @MethodSource("randomWordExamples")
    void testGameServiceCorrect(Word word) throws Exception {

        /*Mock repos*/
        WordService wordService = new WordService(wordRepository);
        UserService userService = new UserService(userRepository, mock(PasswordEncoder.class));
        GameService gameService = new GameService(gameRepository, userService, wordService);

        //Save word
        wordRepository.save(word);

        /*Create user and game*/
        User user = userService.loadUserByUsername("test");
        Game game = gameService.getGameForUser("test");

        /*Do guess*/
        Integer id = game.getId();
        gameService.gameDoGuess(user.getUsername(), word.getValue());

        assertTrue(game.getCurrentRound().isCorrect());
    }

    @ParameterizedTest
    @DisplayName("Creates a new game from service and tries resetting the game before guessing")
    @MethodSource("randomWordExamples")
    void testGameServiceTryResetGame_False(Word word) throws Exception {

        /*Mock repos*/
        WordService wordService = new WordService(wordRepository);
        UserService userService = new UserService(userRepository, mock(PasswordEncoder.class));
        GameService gameService = new GameService(gameRepository, userService, wordService);

        //Save word
        wordRepository.save(word);

        /*Create user and game*/
        User user = userService.loadUserByUsername("test");
        Game game = gameService.getGameForUser("test");

        /*Do guess*/
        try{
            gameService.resetGame(word, user.getUsername());
            fail("No Exception thrown");
        }catch (Exception e){
            assertTrue(e.getMessage().contains("Game is still running"));
        }
    }

    static Stream<Arguments> randomWordExamples() {
        return Stream.of(
                Arguments.of(new Word("tower")),
                Arguments.of(new Word("castle")),
                Arguments.of(new Word("knights")),
                Arguments.of(new Word("test")),
                Arguments.of(new Word("banaan"))
        );
    }
}