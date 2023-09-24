import BotMoves.*;
import javafx.scene.control.Button;

import java.awt.*;

public class Bot {
    private final Algorithm typeMove;
    // Type Move List:
    // 1 : moveRandomize
    // 2 : moveMiniMaxABPruning
    // 3 : moveLocalSearch
    // 4 : moveGASearch

    public Bot(){
        this.typeMove = new RandomizeAlgo();
    }

    public Bot(int moveOption){
        if (moveOption == 1){
            this.typeMove = new MiniMaxABAlgo();
        } else if (moveOption == 2){
            this.typeMove = new LocalSearchAlgo();
        } else if (moveOption == 3){
            this.typeMove = new GASearchAlgo();
        } else {
            this.typeMove = new RandomizeAlgo();
        }
    }

    public int[] move(Button[][] boardMap) {
        return this.typeMove.move(boardMap);
    }

}
