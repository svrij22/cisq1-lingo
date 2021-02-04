package nl.hu.cisq1.lingo.words.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "games")
public class Game implements Serializable {

    @Id
    @GeneratedValue
    public Integer id;
    public Double score;

    @ManyToOne
    public Word word;

    @OneToMany(cascade=CascadeType.ALL)
    public List<Guess> guesses;

    public Game() {}

    public Game(Word word) {
        this.word = word;
    }

    public void doGuess(String guess) {
        guesses.add(new Guess(word.getValue(), guess));
    }
}