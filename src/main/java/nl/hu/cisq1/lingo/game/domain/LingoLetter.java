package nl.hu.cisq1.lingo.game.domain;

import java.io.Serializable;

public class LingoLetter implements Serializable {
    private Character character;
    private Guess.letterStatus status;

    public LingoLetter() {
    }

    public LingoLetter(Character character, Guess.letterStatus status) {
        this.character = character;
        this.status = status;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Guess.letterStatus getStatus() {
        return status;
    }

    public void setStatus(Guess.letterStatus status) {
        this.status = status;
    }
}
