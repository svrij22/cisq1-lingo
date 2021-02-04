package nl.hu.cisq1.lingo.words.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity(name = "guess")
public class Guess implements Serializable {

    @Id
    @GeneratedValue
    public Integer id;

    String word;
    String guess;

    @ElementCollection
    Map<Character, List> resultMap;

    public enum letterStatus{
        CORRECT,
        NEAR,
        INCORRECT
    }

    public Guess() {
    }

    public Guess(String word, String guess) {
        resultMap = new LinkedHashMap<>();
        for (int i = 0; i < word.length(); i++){
            letterStatus status = letterStatus.INCORRECT;
            if (word.charAt(i) == guess.charAt(i)) status = letterStatus.CORRECT;
            resultMap.put(guess.charAt(i), status);
        }
    }

    public Map<Character, letterStatus> getResultMap() {
        return resultMap;
    }
}