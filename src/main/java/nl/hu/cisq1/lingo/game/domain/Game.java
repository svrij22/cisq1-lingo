package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.enums.GameState;
import nl.hu.cisq1.lingo.game.domain.exception.GameResetException;
import nl.hu.cisq1.lingo.security.data.User;
import nl.hu.cisq1.lingo.words.domain.Word;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.game.domain.enums.GameState.*;


@Entity(name = "games")
public class Game implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    private Double score = (double) 0;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Round> rounds = new ArrayList<>();

    public Game() {}
    public Game(Word word) {
        this.rounds.add(new Round(word));
    }

    public Round getCurrentRound(){
        return this.rounds.get(this.rounds.size()-1);
    }

    public void doGuess(String guess){
        getCurrentRound().doGuess(guess);
    }

    public void resetGame(Word newWord) {
        if (getState() == STARTED) throw new GameResetException("Game is still running");
        if (getState() == WON) this.addScore(100 - (getCurrentRound().getGuesses().size() * 10));
        if (getState() == GAMEOVER) this.removeScore();

        /*Reset game values*/
        this.rounds.add(new Round(newWord));
    }

    public GameState getState() {
        if (getCurrentRound().isGameOver()) return GAMEOVER;
        if (getCurrentRound().isCorrect()) return WON;
        return STARTED;
    }

    public Integer getId() {
        return id;
    }

    public void addScore(int score){
        this.score += score;
    }

    public Double getScore() {
        return score;
    }

    public void removeScore() {
        this.score = Math.ceil(this.score / 2);
    }
}