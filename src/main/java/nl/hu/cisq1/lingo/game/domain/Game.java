package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.enums.GameState;
import nl.hu.cisq1.lingo.words.domain.Word;
import nl.hu.cisq1.lingo.game.domain.exception.GameResetException;

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
    private GameState state = STARTED;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Round> rounds;

    public Game() {}
    public Game(Word word) {
        this.rounds = new ArrayList<Round>(){};
        this.rounds.add(new Round(word));
    }

    public Round getCurrentRound(){
        if (!this.rounds.isEmpty()){
            return this.rounds.get(this.rounds.size()-1);
        }
        return null;
    }

    public void doGuess(String guess){
        getCurrentRound().doGuess(guess);
        checkState();
    }

    public void checkState(){
        if (getCurrentRound().isCorrect()) this.state = WON;
        if (getCurrentRound().isGameOver()) this.state = GAMEOVER;
    }

    public void resetGame(Word newWord) throws Exception {
        if (state == STARTED) throw new Exception("Game is still running");
        if (state == WON) this.addScore(100 - (getCurrentRound().getGuesses().size() * 10));
        if (state == GAMEOVER) this.removeScore();

        /*Reset game values*/
        this.state = STARTED;
        this.rounds.add(new Round(newWord));
    }

    public GameState getState() {
        return state;
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