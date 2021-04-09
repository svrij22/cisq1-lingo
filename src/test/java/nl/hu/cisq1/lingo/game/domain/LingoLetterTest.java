package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.enums.LetterState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nl.hu.cisq1.lingo.game.domain.enums.LetterState.ABSENT;
import static nl.hu.cisq1.lingo.game.domain.enums.LetterState.CORRECT;
import static org.junit.jupiter.api.Assertions.*;

class LingoLetterTest {

    @Test
    @DisplayName("Creates a lingo letter")
    void lingoLetter() {
        LingoLetter lingoLetter = new LingoLetter('a', CORRECT);
        assertEquals(lingoLetter.getCharacter(), 'a');
        assertEquals(lingoLetter.getStatus(), CORRECT);
    }

    @Test
    @DisplayName("Creates a lingo letter with wrong attributes")
    void lingoLetter_False() {
        LingoLetter lingoLetter = new LingoLetter('b', ABSENT);
        assertNotEquals(lingoLetter.getCharacter(), 'a');
        assertNotEquals(lingoLetter.getStatus(), CORRECT);
    }
}