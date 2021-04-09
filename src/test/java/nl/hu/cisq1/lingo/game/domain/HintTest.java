package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.enums.LetterState;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.game.domain.enums.LetterState.*;
import static org.junit.jupiter.api.Assertions.*;

class HintTest {

    @Test
    @DisplayName("Checks hint")
    void checkHintFalse() {
        Round round = new Round(new Word("woord"));
        round.doGuess("wooxx");
        round.doGuess("xxxxx");
        round.doGuess("xxxxx");

        Hint hint = round.getHint();
        assertNotEquals(hint.hint, Arrays.asList('w', 'o', '_', '_', '_'));
        assertNotEquals(hint.hintStr(), "wo___");
    }

    @Test
    @DisplayName("Checks hint")
    void checkHint() {
        Round round = new Round(new Word("woord"));
        round.doGuess("woxxx");
        round.doGuess("xxxxx");
        round.doGuess("xxxxx");

        Hint hint = round.getHint();
        assertEquals(hint.hint, Arrays.asList('w', 'o', '_', '_', '_'));
        assertEquals(hint.hintStr(), "wo___");
    }

}