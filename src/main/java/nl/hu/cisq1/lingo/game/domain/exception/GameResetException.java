package nl.hu.cisq1.lingo.game.domain.exception;

public class GameResetException extends RuntimeException {
    public GameResetException(String err) {
        super(err);
    }
}
