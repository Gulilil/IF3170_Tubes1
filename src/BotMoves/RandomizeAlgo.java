package BotMoves;

import javafx.scene.control.Button;

public class RandomizeAlgo implements Algorithm{
    @Override
    public int[] move(char[][] boardMap, int roundleft) {
        // create random move
        int[] currentMove = new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
        while (boardMap[currentMove[0]][currentMove[1]] != ' '){
            currentMove = new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
        }
        System.out.println("Masuk ke rnaodmi");
        return currentMove;
    }
}
