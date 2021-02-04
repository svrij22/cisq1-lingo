package nl.hu.cisq1.lingo.words.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity(name = "guess")
public class Guess implements Serializable {

    @Id
    @GeneratedValue
    public Integer id;

    String word;
    String guess;

    @Lob
    ArrayList<List<?>> result;

    public enum letterStatus{
        CORRECT,
        NEAR,
        INCORRECT
    }

    public Guess() {
    }

    public Guess(String word, String guess) {
        result = new ArrayList<>();

        for (int i = 0; i < word.length(); i++){
            /*Set status*/
            letterStatus status = letterStatus.INCORRECT;
            if (word.indexOf(guess.charAt(i)) > 0) status = letterStatus.NEAR;
            if (word.charAt(i) == guess.charAt(i)) status = letterStatus.CORRECT;

            /*Add to result*/
            result.add(Arrays.asList(guess.charAt(i), status));
        }
    }

    public ArrayList<List<?>> getResultMap() {
        return result;
    }
}