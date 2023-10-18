package BotMoves;

import java.lang.*;
import java.util.Random;

public class SimulatedAnnealingAlgo implements Algorithm{

    private char selfMark;
    private char enemyMark;

    @Override
    public int calculateObjective(char[][] boardMap){
        int point = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardMap[i][j] == this.selfMark) {
                    point++;
                } else if (boardMap[i][j] == this.enemyMark) {
                    point--;
                }
            }
        }
        return point;
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
        newMap[pos[0]][pos[1]] = this.selfMark;
        if (pos[0] != 0 && newMap[pos[0]-1][pos[1]] == this.enemyMark){
            newMap[pos[0]-1][pos[1]] = this.selfMark;
        }
        if (pos[0] != 7 && newMap[pos[0]+1][pos[1]] == this.enemyMark){
            newMap[pos[0]+1][pos[1]] = this.selfMark;
        }
        if (pos[1] != 0 && newMap[pos[0]][pos[1]-1] == this.enemyMark){
            newMap[pos[0]][pos[1]-1] = this.selfMark;
        }
        if (pos[1] != 7 && newMap[pos[0]][pos[1]+1] == this.enemyMark){
            newMap[pos[0]][pos[1]+1] = this.selfMark;
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

    private double moveProbability(double valueDiff, double t){
        if (valueDiff > 1){
            return 1;
        } else {
            return Math.exp( (double) (valueDiff-1)/  t);
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
        return res < probability;
    }

    @Override
    public int[] move(char[][] boardMap, int roundLeft, char selfMark, char enemyMark) {
        this.selfMark = selfMark;
        this.enemyMark = enemyMark;
        int[] current = null;
        int[] tempTile = null;
        double currentVal = calculateObjective(boardMap);
        double temperature = 10;
        while (temperature > 0){
            int[] newPos = generateRandom(boardMap);
            if (countAdjacentOfMark(boardMap, newPos, this.enemyMark) == 0){
                tempTile = newPos;
            }
            if (countAdjacentOfMark(boardMap, newPos, this.enemyMark) > 0){
                char[][] newBoardMap = duplicateBoardAndInsert(boardMap, newPos);
                double newVal = calculateObjective(newBoardMap);

                if (!potentialTakenHeuristics(boardMap, newPos)) {
                    newVal +=0.5;
                }

                double prob = moveProbability(newVal-currentVal, temperature);

                if (moveSuccess(prob)){
                    System.out.println("SA construct: "+ newPos[0] + " " + newPos[1] + ", val: " + newVal);
                    current = newPos;
                    currentVal = newVal;
                }
            }
            temperature -= 0.1;
        }
        if (current == null){
            return tempTile;
        } else {
            return current;
        }
    }

    private int countAdjacentOfMark(char[][] boardMap, int[] pos, char mark){
        int count = 0;
        if (pos[0] != 0 && boardMap[pos[0]-1][pos[1]] == mark){
            count++;
        }
        if (pos[0] != 7 && boardMap[pos[0]+1][pos[1]] == mark){
            count++;
        }
        if (pos[1] != 0 && boardMap[pos[0]][pos[1]-1] == mark){
            count++;
        }
        if (pos[1] != 7 && boardMap[pos[0]][pos[1]+1] == mark){
            count++;
        }
        return count;
    }

    private boolean potentialTakenHeuristics(char[][] boardMap, int[] tile){
        int count = 0;
        if (tile[0] != 0 && boardMap[tile[0]-1][tile[1]] == ' '){
            count += countAdjacentOfMark(boardMap, new int[] {tile[0]-1, tile[1]},this.selfMark);
        }
        if (tile[0] != 7 && boardMap[tile[0]+1][tile[1]] == ' '){
            count += countAdjacentOfMark(boardMap, new int[] {tile[0]+1, tile[1]},this.selfMark);
        }
        if (tile[1] != 0 && boardMap[tile[0]][tile[1]-1] == ' '){
            count += countAdjacentOfMark(boardMap, new int[] {tile[0], tile[1]-1},this.selfMark);
        }
        if (tile[1] != 7 && boardMap[tile[0]][tile[1]+1] == ' '){
            count += countAdjacentOfMark(boardMap, new int[] {tile[0], tile[1]+1},this.selfMark);
        }
        return count != 0;
    }
}
