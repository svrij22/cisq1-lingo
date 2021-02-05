package nl.hu.cisq1.lingo.words.domain.exception;

public class WordAlreadyGuessed extends RuntimeException {
    public WordAlreadyGuessed() {
        super("Word length already guessed");
    }
}
