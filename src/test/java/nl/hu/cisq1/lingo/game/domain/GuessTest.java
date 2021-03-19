package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.Guess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuessTest {

    @Test
    @DisplayName("Creates a lingo letter array")
    void createsArray() {
        Guess guess = new Guess("woord", "wourd");
        assertEquals(guess.lingoLetters.size(), 5);
    }

    @Test
    @DisplayName("Checks if letters match")
    void allMatch() {
        Guess guess = new Guess("woord", "woord");

        boolean allMatch = guess.getLingoLetters()
                .stream()
                .allMatch((l) -> l.getStatus() == Guess.letterStatus.CORRECT);

        assertTrue(allMatch);
    }

    @Test
    @DisplayName("Checks if letters don't match")
    void incorrectMatch() {
        Guess guess = new Guess("woord", "woozq");

        long matches = guess.getLingoLetters()
                .stream()
                .filter(l -> l.getStatus().equals(Guess.letterStatus.INCORRECT))
                .count();

        assertEquals(matches, 2);
    }

    @Test
    @DisplayName("2 letters wrongly placed")
    void nearMatch() {
        Guess guess = new Guess("woord", "worod");

        long allMatch = guess.getLingoLetters()
                .stream()
                .filter(l -> l.getStatus().equals(Guess.letterStatus.NEAR))
                .count();

        assertEquals(allMatch, 2);
    }
}