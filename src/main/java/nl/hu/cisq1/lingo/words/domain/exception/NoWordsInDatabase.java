package nl.hu.cisq1.lingo.words.domain.exception;

public class NoWordsInDatabase extends RuntimeException {
    public NoWordsInDatabase() {
        super("Could not pick random word");
    }
}
