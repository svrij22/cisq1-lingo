package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

        try{
            game.resetGame(word);
        }catch (Exception e){
            fail("Exception thrown");
        }
    }

    @ParameterizedTest
    @DisplayName("Creates a new game and guesses the wrong word")
    @MethodSource("randomWordExamples")
    void testGameWrong(Word word) throws Exception {

        Game game = new Game(word);
        game.doGuess(word.getValue().replace('a', 'b'));

        assertFalse(game.getCurrentRound().getGuesses().get(0).isCorrect());
    }

    @ParameterizedTest
    @DisplayName("Creates a new game and guesses the wrong word length")
    @MethodSource("randomWordExamples")
    void testGameWrongLength(Word word) {
        try {
            Game game = new Game(word);
            game.doGuess(word.getValue() + "xxx");
            assertFalse(game.getCurrentRound().getGuesses().get(0).isCorrect());
            fail( "My method didn't throw when I expected it to" );
        } catch (WordLengthNotEqual ignored) {} catch (Exception e) {
            fail( "Failed on wrong exception" );
        }
    }

    @ParameterizedTest
    @DisplayName("Creates a new game and fails the round")
    @MethodSource("randomWordExamples")
    void testGameFailRound(Word word) {
        try {
            Game game = new Game(word);
            game.doGuess(word.getValue() + "xxx");
            assertFalse(game.getCurrentRound().getGuesses().get(0).isCorrect());
            fail( "My method didn't throw when I expected it to" );
        } catch (WordLengthNotEqual ignored) {} catch (Exception e) {
            fail( "Failed on wrong exception" );
        }
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