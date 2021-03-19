package nl.hu.cisq1.lingo.game.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.ArrayList;

import static nl.hu.cisq1.lingo.game.domain.Guess.letterStatus.*;

@Entity(name = "guess")
public class Guess implements Serializable {

    @Id
    @GeneratedValue
    public Integer id;

    String word;
    String guess;

    @Lob
    ArrayList<LingoLetter> lingoLetters;

    public enum letterStatus{
        CORRECT,
        NEAR,
        INCORRECT
    }

    public boolean isCorrect(){
        return this.lingoLetters.stream().allMatch(n -> n.getStatus() == CORRECT);
    }

    public Guess() {
    }

    public Guess(String word, String guess) {
        this.lingoLetters = matchLingoLetterGuess(word, guess);
    }

    public ArrayList<LingoLetter> getLingoLetters() {
        return lingoLetters;
    }

    public void setLingoLetters(ArrayList<LingoLetter> lingoLetters) {
        this.lingoLetters = lingoLetters;
    }


    public ArrayList<LingoLetter> matchLingoLetterGuess(String word, String guess){

        ArrayList<LingoLetter> result = new ArrayList<>();
        StringBuilder nonGuessedLetters = new StringBuilder();

        /*Get correctly guessed words*/
        for (int i = 0; i < word.length(); i++){

            /*Set status*/
            Guess.letterStatus status = INCORRECT;
            if (word.charAt(i) == guess.charAt(i)) {
                status = CORRECT;
            }else{
                nonGuessedLetters.append(word.charAt(i));
            }

            /*Add to result*/
            result.add(new LingoLetter(guess.charAt(i), status));
        }

        /*Get orange letters that are in the non guessed letters*/
        result.forEach((letter) -> {

            /*Get character and status*/
            Guess.letterStatus status = letter.getStatus();
            String character = letter.getCharacter().toString();

            /*Not correct?*/
            if (status != CORRECT){

                /*Is in non guessed letters? Remove char from non guessed letters and set status to near*/
                if (nonGuessedLetters.toString().contains(character)) {
                    nonGuessedLetters.setCharAt(nonGuessedLetters.indexOf(character), ' ');
                    status = NEAR;
                }
            }

            letter.setStatus(status);
        });

        return result;
    }
}