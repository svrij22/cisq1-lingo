package nl.hu.cisq1.lingo.words.domain;

import javax.persistence.Entity;

import static nl.hu.cisq1.lingo.words.domain.Logic.gameState.*;

public class Logic {

    public enum gameState{
        WON,
        STARTED,
        GAMEOVER
    }

    public Logic() {
    }

    public static void checkLogic(Game game){
        if (game.getAttempts().equals(game.getAttemptsAllowed())){
            game.setState(GAMEOVER);
        }

        if (game.isCorrect()){
            game.setState(WON);
        }
    }


}
