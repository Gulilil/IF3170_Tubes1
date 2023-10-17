package BotMoves;

import javafx.scene.control.Button;

public interface Algorithm {
    public int[] move(char[][] boardMap, int roundLeft, char selfMark, char enemyMark);

}
