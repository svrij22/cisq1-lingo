package nl.hu.cisq1.lingo.words.application;

import nl.hu.cisq1.lingo.words.data.GameRepository;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Game;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This is a unit test.
 *
 * It tests the behaviors of our system under test,
 * WordService, in complete isolation:
 * - its methods are called by the test framework instead of a controller
 * - the WordService calls a test double instead of an actual repository
 */
@DataJpaTest
class GameServiceTest {

    @Autowired
    GameRepository mockRepositoryG;

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

        assertTrue(game.getGuesses().get(0).isCorrect());
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