package nl.hu.cisq1.lingo.game.application;

import nl.hu.cisq1.lingo.game.data.GameRepository;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.security.application.UserService;
import nl.hu.cisq1.lingo.security.data.SpringUserRepository;
import nl.hu.cisq1.lingo.words.application.WordService;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * This is a unit test.
 *
 * It tests the behaviors of our system under test,
 * WordService, in complete isolation:
 * - its methods are called by the test framework instead of a controller
 * - the WordService calls a test double instead of an actual repository
 */
@DataJpaTest
class GameServiceIntegrationTest {

    @Autowired
    GameRepository mockRepositoryG;

    @ParameterizedTest
    @DisplayName("Creates a new game from service and checks word")
    @MethodSource("randomWordExamples")
    void testGameService(Word word) throws Exception {

        /*Mock repos*/
        SpringWordRepository mockRepositoryW = mock(SpringWordRepository.class);
        GameRepository mockRepositoryG2 = mock(GameRepository.class);
        WordService service = new WordService(mockRepositoryW);
        UserService userService = mock(UserService.class);
        GameService gameService = new GameService(mockRepositoryG2, userService, service);

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
        SpringWordRepository mockRepositoryW = mock(SpringWordRepository.class);
        WordService service = new WordService(mockRepositoryW);
        UserService userService = mock(UserService.class);
        GameService gameService = new GameService(mockRepositoryG, userService, service);

        /*Create game*/
        Game game = gameService.getNewGame(word);

        /*Do guess*/
        Integer id = game.getId();
        //gameService.gameDoGuess(id, word.getValue());

        assertTrue(game.getCurrentRound().isCorrect());
    }

    @ParameterizedTest
    @DisplayName("Creates a new game from service and tries resetting the game before guessing")
    @MethodSource("randomWordExamples")
    void testGameServiceTryResetGame_False(Word word) throws Exception {

        /*Mock repos*/
        SpringWordRepository mockRepositoryW = mock(SpringWordRepository.class);
        WordService service = new WordService(mockRepositoryW);
        UserService userService = mock(UserService.class);
        GameService gameService = new GameService(mockRepositoryG, userService, service);

        /*Create game*/
        Game game = gameService.getNewGame(word);

        /*Do guess*/
        try{
            //gameService.resetGame(word, game.getId());
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