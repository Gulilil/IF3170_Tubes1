package BotMoves;

import java.lang.*;
import java.util.Random;

public class LocalSearchAlgo implements Algorithm{

    private int checkBoardValue(char[][] boardMap){
        int countX = 0;
        int countO = 0;
        for (char[] rows : boardMap) {
            for (char tile : rows){
                if (tile != ' '){
                    if (tile == 'O'){
                        countO++;
                    } else {
                        countX++;
                    }
                }
            }
        }
        return countO - countX;
    }

    private char[][] duplicateBoardAndInsert(char[][] boardMap, int[] pos){
        // Duplicate
        char[][] newMap = new char[8][8];
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                newMap[i][j] = boardMap[i][j];
            }
        }

        // Insert
        newMap[pos[0]][pos[1]] = 'O';
        if (pos[0] != 0 && newMap[pos[0]-1][pos[1]] == 'X'){
            newMap[pos[0]-1][pos[1]] = 'O';
        }
        if (pos[0] != 7 && newMap[pos[0]+1][pos[1]] == 'X'){
            newMap[pos[0]+1][pos[1]] = 'O';
        }
        if (pos[1] != 0 && newMap[pos[0]][pos[1]-1] == 'X'){
            newMap[pos[0]][pos[1]-1] = 'O';
        }
        if (pos[1] != 7 && newMap[pos[0]][pos[1]+1] == 'X'){
            newMap[pos[0]][pos[1]+1] = 'O';
        }
        return newMap;
    }

    private int[] generateRandom(char[][] boardMap){
        int[] currentMove = new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
        while (boardMap[currentMove[0]][currentMove[1]] != ' '){
            currentMove = new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
        }
        return currentMove;
    }

    private double moveProbability(int valueDiff, int t){
        if (valueDiff > 1){
//            System.out.println("Optimal");
            return 1;
        } else {
            return Math.exp( (double) (valueDiff-1)/ (double) t);
        }
    }

    private void printBoardMap(char[][] boardMap){
        for (char[] board: boardMap) {
            for (char c : board){
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    private boolean moveSuccess(double probability){
        Random rd = new Random();
        double res = rd.nextDouble();
        return res == 1.0 || res < probability;
    }

    @Override
    public int[] move(char[][] boardMap, int roundLeft) {
        int[] current = null;
        int currentVal = checkBoardValue(boardMap);
        double maxVal = Double.NEGATIVE_INFINITY;
        int[] best = null;
        int temperature = 500;
        while (temperature > 0){
            int[] newPos = generateRandom(boardMap);
            char[][] newBoardMap = duplicateBoardAndInsert(boardMap, newPos);
            int newVal = checkBoardValue(newBoardMap);

            double prob = moveProbability(newVal-currentVal, temperature);

            if (moveSuccess(prob)){
                current = newPos;
                currentVal = newVal;
                if (currentVal > maxVal){
                    maxVal = currentVal;
                    best = current;
                }
            }
            temperature--;
        }
        return best;
    }
}
