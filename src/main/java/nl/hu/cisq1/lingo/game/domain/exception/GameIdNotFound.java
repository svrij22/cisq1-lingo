package nl.hu.cisq1.lingo.game.domain.exception;

public class GameIdNotFound extends RuntimeException {
    public GameIdNotFound() {
        super("Game Id Not Found");
    }
}
