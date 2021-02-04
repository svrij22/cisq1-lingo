package nl.hu.cisq1.lingo.words.domain;

import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotEqual;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "games")
public class Game implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    private Double score;

    @ManyToOne(cascade=CascadeType.ALL)
    private Word word;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Guess> guesses = new ArrayList<>();

    public Game() {}

    public Game(Word word) {
        this.word = word;
    }

    public void doGuess(String guess) throws Exception {
        if (isCorrect()) throw new Exception("Word is already guessed!");
        if (word.getValue().length() != guess.length()) throw new WordLengthNotEqual();
        guesses.add(new Guess(word.getValue(), guess));
    }

    public boolean isCorrect(){
        /*Check if last guess is correct*/
        if (guesses.size() == 0) return false;
        return guesses.get(guesses.size() - 1).isCorrect();
    }

    public Integer getId() {
        return id;
    }

    public void addScore(int score){
        this.score += score;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Double getScore() {
        return score;
    }

    public Word getWord() {
        return word;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }
}