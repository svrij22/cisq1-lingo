package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.words.domain.Word;
import org.apache.logging.log4j.util.Chars;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Hint {
    public List<Character> hint;
    public Hint(Word wordToGuess, List<Guess> guesses) {
        setHint(wordToGuess, guesses);
    }

    public void setHint(Word wordToGuess, List<Guess> guesses){
        /*For each character in word*/
        hint = IntStream.range(0, wordToGuess.getLength())
                .mapToObj(i -> {
                    /*Get character*/
                    Character curChar = wordToGuess.getValue().charAt(i);
                    /*Check if matches correctly anywhere*/
                    boolean matchesAny = guesses.stream().anyMatch(guess -> (guess.getLetters().get(i).getCharacter().equals(curChar)));
                    /*First letter always visible*/
                    if (i == 0) matchesAny = true;
                    /*Return either _ or correct char*/
                    return (matchesAny ? curChar : '_');
                })
                .collect(Collectors.toList());
    }

    public String hintStr(){
        return hint.stream().map(String::valueOf).collect(Collectors.joining());
    }
}