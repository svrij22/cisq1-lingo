package nl.hu.cisq1.lingo.game.application;

import nl.hu.cisq1.lingo.game.application.GameService;
import nl.hu.cisq1.lingo.game.data.GameRepository;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.words.application.WordService;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        GameService gameService = new GameService(mockRepositoryG2, service);

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
        GameService gameService = new GameService(mockRepositoryG, service);

        /*Create game*/
        Game game = gameService.getNewGame(word);

        /*Do guess*/
        Integer id = game.getId();
        gameService.gameDoGuess(id, word.getValue());

        assertTrue(game.getCurrentRound().isCorrect());
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