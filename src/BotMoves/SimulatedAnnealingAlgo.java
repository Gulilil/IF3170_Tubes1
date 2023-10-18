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

    private int countAdjacentEnemies(char[][] boardMap, int[] pos){
        int count = 0;
        if (pos[0] != 0 && boardMap[pos[0]-1][pos[1]] == this.enemyMark){
            count++;
        }
        if (pos[0] != 7 && boardMap[pos[0]+1][pos[1]] == this.enemyMark){
            count++;
        }
        if (pos[1] != 0 && boardMap[pos[0]][pos[1]-1] == this.enemyMark){
            count++;
        }
        if (pos[1] != 7 && boardMap[pos[0]][pos[1]+1] == this.enemyMark){
            count++;
        }
        return count;
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

    private double moveProbability(int valueDiff, double t){
        if (valueDiff > 1){
//            System.out.println("Optimal");
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
//        System.out.println(probability);
        Random rd = new Random();
        double res = rd.nextDouble();
        return res < probability;
    }

    @Override
    public int[] move(char[][] boardMap, int roundLeft, char selfMark, char enemyMark) {
        this.selfMark = selfMark;
        this.enemyMark = enemyMark;
        int[] current = null;
        int currentVal = calculateObjective(boardMap);
        double temperature = 10;
        while (temperature > 0){
            int[] newPos = generateRandom(boardMap);
            if (countAdjacentEnemies(boardMap, newPos) > 0){
                char[][] newBoardMap = duplicateBoardAndInsert(boardMap, newPos);
                int newVal = calculateObjective(newBoardMap);

                double prob = moveProbability(newVal-currentVal, temperature);

                if (moveSuccess(prob)){
                    System.out.println(newPos[0] + " " + newPos[1] + ", val: " + newVal);
                    current = newPos;
                    currentVal = newVal;
                }
            }
            temperature -= 0.1;
        }
        return current;
    }
}