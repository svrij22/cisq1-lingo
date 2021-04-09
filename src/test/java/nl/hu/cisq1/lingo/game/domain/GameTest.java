package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.game.domain.enums.GameState;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @ParameterizedTest
    @DisplayName("Creates a new game and guesses the right word")
    @MethodSource("randomWordExamples")
    void testGameCorrect(Word word) {

        Game game = new Game(word);
        game.doGuess(word.getValue());

        assertTrue(game.getCurrentRound().getGuesses().get(0).isCorrect());
    }

    @ParameterizedTest
    @DisplayName("Guesses word and resets game")
    @MethodSource("randomWordExamples")
    void testGameCorrectReset(Word word) {

        Game game = new Game(word);
        game.doGuess(word.getValue());
        assertTrue(game.getCurrentRound().getGuesses().get(0).isCorrect());
        assertEquals(game.getState(), GameState.WON);

        assertDoesNotThrow(() -> game.resetGame(word));
    }

    @ParameterizedTest
    @DisplayName("Creates a new game and guesses the wrong word")
    @MethodSource("randomWordExamples")
    void testGameWrong(Word word) throws Exception {

        Game game = new Game(word);
        assertEquals(game.getState(), GameState.STARTED);

        game.doGuess(word.getValue().replace('a', 'b'));
        System.out.println(game.getState());

        assertFalse(game.getCurrentRound().isCorrect());
        assertEquals(game.getState(), GameState.STARTED);
    }

    @ParameterizedTest
    @DisplayName("Creates a new game and guesses the wrong word length")
    @MethodSource("randomWordExamples")
    void testGameWrongLength(Word word) {
        Game game = new Game(word);

        assertThrows(WordLengthNotEqual.class, () -> {
            game.doGuess(word.getValue() + "xxx");
        });

        assertEquals(game.getState(), GameState.STARTED);
    }

    @ParameterizedTest
    @DisplayName("Creates a new game and fails the round")
    @MethodSource("randomWordExamples")
    void testGameFailRound(Word word) {
        Game game = new Game(word);

        IntStream.range(0, 10).forEach((n) -> {
            game.doGuess(word.getValue().replace('a', 'b'));
        });

        assertEquals(game.getState(), GameState.GAMEOVER);
    }

    static Stream<Arguments> randomWordExamples() {
        return Stream.of(
                Arguments.of(new Word("tawer")),
                Arguments.of(new Word("castle")),
                Arguments.of(new Word("knaghts")),
                Arguments.of(new Word("tast")),
                Arguments.of(new Word("banaan"))
        );
    }
}