package nl.hu.cisq1.lingo.words.domain;

import nl.hu.cisq1.lingo.words.domain.exception.GameResetException;
import nl.hu.cisq1.lingo.words.domain.exception.WordAlreadyGuessed;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotEqual;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.words.domain.Game.gameState.*;


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


    public enum gameState{
        WON,
        STARTED,
        GAMEOVER
    }

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
        checkState();
    }

    public boolean isCorrect(){

        /*Check if last guess is correct*/
        if (guesses.size() == 0) return false;
        return guesses.get(guesses.size() - 1).isCorrect();
    }

    public void checkState(){
        if (this.attempts.equals(this.attemptsAllowed)){
            this.state = GAMEOVER;
        }

        if (this.isCorrect()){
            this.state = WON;
        }
    }

    public void resetGame(Word word) {
        try{
            if (state == STARTED) throw new Exception("Game is still running");

            if (state == WON){
                this.addScore(100 - (guesses.size() * 10));
            }

            if (state == GAMEOVER){
                this.removeScore();
            }

            guesses = new ArrayList<>();
            this.word = word;
            state = STARTED;
            attempts = 0;

        }catch (Exception e){
            throw new GameResetException(e.getMessage());
        }
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

    public void removeScore() {
        this.score = Math.ceil(this.score / 2);
    }

}