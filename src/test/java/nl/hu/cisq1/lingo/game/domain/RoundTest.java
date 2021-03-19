package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {


    @ParameterizedTest
    @DisplayName("Creates a new round and fails the round")
    @MethodSource("randomWordExamples")
    void testFailRound(Word word) {
        Round round = new Round(word);

        /*9 Times*/
        IntStream.range(0, 9).forEach(i -> round.doGuess("xxxxx"));
        assertFalse(round.isCorrect());
        assertFalse(round.isGameOver());

        /*Last Time*/
        round.doGuess("xxxxx");
        assertTrue(round.isGameOver());
    }

    @ParameterizedTest
    @DisplayName("Creates a new round and wins the round")
    @MethodSource("randomWordExamples")
    void testWinRound(Word word) {
        Round round = new Round(word);

        /*9 Times*/
        IntStream.range(0, 9).forEach(i -> round.doGuess("xxxxx"));
        assertFalse(round.isCorrect());
        assertFalse(round.isGameOver());

        /*Last Time*/
        round.doGuess(word.getValue());
        assertTrue(round.isCorrect());
    }

    static Stream<Arguments> randomWordExamples() {
        return Stream.of(
                Arguments.of(new Word("tawer")),
                Arguments.of(new Word("castl")),
                Arguments.of(new Word("knagh")),
                Arguments.of(new Word("taste")),
                Arguments.of(new Word("banan"))
        );
    }
}