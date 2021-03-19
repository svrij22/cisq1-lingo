package nl.hu.cisq1.lingo.game.presentation.dto;

import nl.hu.cisq1.lingo.game.domain.Guess;
import nl.hu.cisq1.lingo.game.domain.enums.GameState;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.List;

public class GameDto {
    public int id;
    public List<Guess> guesses;
    public double score;
    public GameState state;
    public Word wordToGuess;

    public GameDto(int id, List<Guess> guesses, Double score, GameState state, Word wordToGuess) {
        this.id = id;
        this.guesses = guesses;
        this.score = score;
        this.state = state;
        this.wordToGuess = wordToGuess;
    }
}
