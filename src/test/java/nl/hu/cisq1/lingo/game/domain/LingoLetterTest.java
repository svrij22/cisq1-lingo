package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.enums.LetterState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LingoLetterTest {

    @Test
    @DisplayName("Creates a lingo letter")
    void createsArray() {
        LingoLetter lingoLetter = new LingoLetter('a', LetterState.CORRECT);
    }

}