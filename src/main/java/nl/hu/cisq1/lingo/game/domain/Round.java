package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.exception.RoundOverException;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotEqual;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "round")
public class Round {
    @GeneratedValue
    @Id
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    private Word wordToGuess;
    private Integer attempts;

    @OneToMany(cascade= CascadeType.ALL)
    private List<Guess> guesses = new ArrayList<>();

    public Round() {}
    public Round(Word word) {
        attempts = 0;
        wordToGuess = word;
    }

    public boolean isCorrect(){
        /*Check if last guess is correct*/
        if (guesses.size() == 0) return false;
        return guesses.get(guesses.size() - 1).isCorrect();
    }

    public boolean isGameOver(){
        Integer attemptsAllowed = 10;
        return (attempts.equals(attemptsAllowed));
    }

    public void doGuess(String guess){

        /*Cant guess when already guessed, or when words arent equal*/
        if (isCorrect()) throw new RoundOverException("Word already guessed");
        if (isGameOver()) throw new RoundOverException("Game is over");
        if (wordToGuess.getValue().length() != guess.length()) throw new WordLengthNotEqual();

        /*Add attempt and guess*/
        attempts += 1;
        guesses.add(new Guess(wordToGuess.getValue(), guess));
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public Word getWordToGuess() {
        return wordToGuess;
    }
}
