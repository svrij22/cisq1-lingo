package nl.hu.cisq1.lingo.words.domain.exception;

public class GameResetException extends RuntimeException {
    public GameResetException(String err) {
        super(err);
    }
}
