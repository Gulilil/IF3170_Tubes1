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
    public int[] move(Button[][] boardMap, int roundLeft) {
        char[][] board = new char[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8 ;j++){
                if(boardMap[i][j].getText().isEmpty()){
                    board[i][j] = ' ';
                }else{
                    board[i][j] = boardMap[i][j].getText().charAt(0);
                }
            }
        }
        System.out.println("Ini bot move");
        return this.typeMove.move(board, roundLeft);
    }

}
