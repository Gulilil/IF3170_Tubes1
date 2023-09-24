package BotMoves;

import javafx.scene.control.Button;

public class RandomizeAlgo implements Algorithm{
    @Override
    public int[] move(Button[][] boardMap) {
        // create random move
        int[] currentMove = new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
        while (!boardMap[currentMove[0]][currentMove[1]].getText().equals("")){
            currentMove = new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
        }
        return currentMove;
    }
}
