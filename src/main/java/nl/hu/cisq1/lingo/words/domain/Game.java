package nl.hu.cisq1.lingo.words.domain;

import nl.hu.cisq1.lingo.words.domain.Logic.gameState;
import nl.hu.cisq1.lingo.words.domain.exception.GameResetException;
import nl.hu.cisq1.lingo.words.domain.exception.WordAlreadyGuessed;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotEqual;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.words.domain.Logic.gameState.*;

@Entity(name = "games")
public class Game implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    private Double score = (double) 0;
    final private Integer attemptsAllowed = 10;
    private Integer attempts = 0;

    @ManyToOne(cascade=CascadeType.ALL)
    private Word word;

    private gameState state = STARTED;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Guess> guesses = new ArrayList<>();

    public Game() {}

    public Game(Word word) {
        this.word = word;
    }

    public void doGuess(String guess){

        /*Cant guess when already guessed*/
        if (isCorrect()) throw new WordAlreadyGuessed();

        /*Should be same value*/
        if (word.getValue().length() != guess.length()) throw new WordLengthNotEqual();

        /*Addd attempt*/
        this.attempts += 1;

        /*Add guess*/
        guesses.add(new Guess(word.getValue(), guess));

        /*Check logic*/
        Logic.checkLogic(this);
    }

    public boolean isCorrect(){

        /*Check if last guess is correct*/
        if (guesses.size() == 0) return false;
        return guesses.get(guesses.size() - 1).isCorrect();
    }

    public Integer getId() {
        return id;
    }

    public void resetGame(Word word){
        try{
            if (this.state == STARTED) throw new Exception("Game is still running");

            if (this.state == WON){
                this.score += 100 - (this.guesses.size() * 10);
            }

            if (this.state == GAMEOVER){
                this.score = Math.ceil(this.score / 2);
            }

            guesses = new ArrayList<>();
            this.word = word;
            this.state = STARTED;
            attempts = 0;

        }catch (Exception e){
            throw new GameResetException(e.getMessage());
        }
    }

    public void addScore(int score){
        this.score += score;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getAttemptsAllowed() {
        return attemptsAllowed;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public gameState getState() {
        return state;
    }

    public void setState(gameState state) {
        this.state = state;
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