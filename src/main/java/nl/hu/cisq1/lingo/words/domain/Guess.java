package nl.hu.cisq1.lingo.words.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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

    public boolean isCorrect(){
        return this.result
                .stream()
                .allMatch(n -> n.get(1) == letterStatus.CORRECT);
    }

    public Guess() {
    }

    public Guess(String word, String guess) {

        result = new ArrayList<>();
        StringBuilder nonGuessedLetters = new StringBuilder();

        /*Get correctly guessed words*/
        for (int i = 0; i < word.length(); i++){

            /*Set status*/
            letterStatus status = letterStatus.INCORRECT;
            if (word.charAt(i) == guess.charAt(i)) {
                status = letterStatus.CORRECT;
            }else{
                nonGuessedLetters.append(word.charAt(i));
            }

            /*Add to result*/
            result.add(Arrays.asList(guess.charAt(i), status));
        }

        String nonGuessedLettersStr = nonGuessedLetters.toString();

        /*Get orange letters that are in the non guessed letters*/
        result = result
                .stream()
                .map((arr) -> {
                    letterStatus status = (letterStatus) arr.get(1);
                    String letter = arr.get(0).toString();
                    if (status != letterStatus.CORRECT){
                        if (nonGuessedLettersStr.contains(letter)) {
                            status = letterStatus.NEAR;
                        }
                    }
                    return Arrays.asList(arr.get(0), status);
                })
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public ArrayList<List<?>> getResultMap() {
        return result;
    }
}