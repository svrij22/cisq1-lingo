package nl.hu.cisq1.lingo.game.domain.exception;

public class RoundOverException extends RuntimeException {
    public RoundOverException(String err) {
        super(err);
    }
}
