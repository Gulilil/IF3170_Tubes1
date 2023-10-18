package BotMoves;

import java.util.ArrayList;

public class SidewaysMoveAlgo implements Algorithm{

    private char selfMark;
    private char enemyMark;

    @Override
    public int[] move(char[][] boardMap, int roundLeft, char selfMark, char enemyMark) {
        this.selfMark = selfMark;
        this.enemyMark = enemyMark;

        ArrayList<int[]> emptyArr = getEmptyTiles(boardMap);

        double maxVal = calculateObjective(boardMap);
        int[] resTile = null;
        for (int[] tile: emptyArr) {
            if (countAdjacentOfMark(boardMap, tile, this.enemyMark) > 0){
                char[][] newBoard = duplicateBoardAndInsert(boardMap, tile);
                double currentVal = calculateObjective(newBoard);

//                if (!potentialTakenHeuristics(boardMap, tile)){
//                    currentVal += 0.5;
//                }
                if (currentVal >= maxVal){
                    System.out.println("SM construct: " + tile[0] + " " + tile[1] + ", Val: " + currentVal);
                    resTile = tile;
                    maxVal = currentVal;
                }
            }
        }
        return resTile;
    }

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

    private ArrayList<int[]> getEmptyTiles(char[][] boardMap){
        ArrayList<int[]> arr = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (boardMap[i][j] == ' '){
                    int[] tile = new int[2];
                    tile[0] = i;
                    tile[1] = j;
                    arr.add(tile);
                }
            }
        }
        return arr;
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
