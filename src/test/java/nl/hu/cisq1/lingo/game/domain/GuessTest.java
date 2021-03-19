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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuessTest {

    @Test
    @DisplayName("Creates a lingo letter array")
    void createsArray() {
        Guess guess = new Guess("woord", "wourd");
        assertEquals(guess.getLetters().size(), 5);
    }

    @Test
    @DisplayName("Checks if letters match")
    void allMatch() {
        Guess guess = new Guess("woord", "woord");

        boolean allMatch = guess.getLetters()
                .stream()
                .allMatch((l) -> l.getStatus() == CORRECT);

        assertTrue(allMatch);
    }

    @Test
    @DisplayName("Checks if letters don't match")
    void incorrectMatch() {
        Guess guess = new Guess("woord", "woozq");

        long matches = guess.getLetters()
                .stream()
                .filter(l -> l.getStatus().equals(ABSENT))
                .count();

        assertEquals(matches, 2);
    }

    @Test
    @DisplayName("2 letters wrongly placed")
    void nearMatch() {
        Guess guess = new Guess("woord", "worod");

        long allMatch = guess.getLetters()
                .stream()
                .filter(l -> l.getStatus().equals(NEAR))
                .count();

        assertEquals(allMatch, 2);
    }

    @ParameterizedTest
    @DisplayName("Test letter state values based on word and guess")
    @MethodSource("guessStream")
    void testLetterStates(String word, String guess, List<LetterState> expectedStates) {
        Guess guess1 = new Guess(word, guess);

        List<LetterState> allStates = guess1.getLetters()
                .stream()
                .map(LingoLetter::getStatus)
                .collect(Collectors.toList());

        System.out.println("Word: " + word + " Guess: " + guess);
        System.out.println("States: " + allStates.toString());
        System.out.println("Expected: " + expectedStates.toString());

        assertEquals(allStates, expectedStates);
    }

    static Stream<Arguments> guessStream() {
        return Stream.of(
                Arguments.of("castle", "castld", Arrays.asList(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT, ABSENT)),
                Arguments.of("castle", "castll", Arrays.asList(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT, ABSENT)),
                Arguments.of("castle", "csaase", Arrays.asList(CORRECT, NEAR,    NEAR,    ABSENT,  ABSENT,  CORRECT)),
                Arguments.of("kaas", "aask", Arrays.asList(NEAR, CORRECT, NEAR, NEAR)),
                Arguments.of("kaas", "aasq", Arrays.asList(NEAR, CORRECT, NEAR, ABSENT))
        );
    }
}