package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.enums.LetterState;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.game.domain.enums.LetterState.*;

@Entity(name = "guess")
public class Guess implements Serializable {

    @Id
    @GeneratedValue
    public Integer id;

    @NotNull
    String word;

    @NotNull
    String guess;


    public boolean isCorrect(){
        return this.matchLingoLetterGuess().stream().allMatch(n -> n.getStatus() == CORRECT);
    }

    public Guess() {
    }

    public Guess(String word, String guess) {
        this.word = word;
        this.guess = guess;
    }

    public List<LingoLetter> getLetters() {
        return this.matchLingoLetterGuess();
    }

    public ArrayList<LingoLetter> matchLingoLetterGuess(){

        ArrayList<LingoLetter> result = new ArrayList<>();
        StringBuilder nonGuessedLetters = new StringBuilder();

        /*Get correctly guessed words*/
        for (int i = 0; i < word.length(); i++){

            /*Set status*/
            LetterState status = ABSENT;
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
            LetterState status = letter.getStatus();
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