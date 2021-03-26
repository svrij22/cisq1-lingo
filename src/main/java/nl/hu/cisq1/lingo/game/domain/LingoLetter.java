package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.enums.LetterState;

import javax.persistence.*;
import java.io.Serializable;

public class LingoLetter implements Serializable {

    private int id;
    private Character character;
    private LetterState status;

    public LingoLetter(Character character, LetterState status) {
        this.character = character;
        this.status = status;
    }

    public Character getCharacter() {
        return character;
    }

    public LetterState getStatus() {
        return status;
    }

    public void setStatus(LetterState status) {
        this.status = status;
    }
}
