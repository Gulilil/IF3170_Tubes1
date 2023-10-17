package BotMoves;

//import javafx.scene.control.Button;
import DataStructure.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MiniMaxABAlgo implements Algorithm{
    private Tree root;
    private char selfMark;
    private char enemyMark;
//    private int checkBoardValue(Button[][] boardMap){
//        int countX = 0;
//        int countO = 0;
//        for (Button[] rowButtons : boardMap) {
//            for (Button tile : rowButtons){
//                if (!tile.getText().equals("")){
//                    if (tile.getText().equals("O")){
//                        countO++;
//                    } else {
//                        countX++;
//                    }
//                }
//            }
//        }
//        return countO - countX;
//    }
//
//    private boolean checkEndOfGame(Button[][] boardMap){
//        int emptyTile = 0;
//        for(Button[] rowButtons : boardMap){
//            for (Button tile : rowButtons){
//                if (tile.getText().equals("")){
//                    emptyTile++;
//                }
//            }
//        }
//        return emptyTile == 0;
//    }
    public boolean isValid(int x, int y){
        if(x >= 0 && x < 8 && y>= 0 && y < 8){
            return true;
        }
        return false;
    }
    public boolean isAdjacent(char[][] boardMap, int x, int y, boolean isBot){
//        Cek apakah sebuah point memiliki tetangga
        if(isValid(x-1,y)){
            if (boardMap[x-1][y] == (isBot ? this.enemyMark : this.selfMark)) {
                return true;
            }
        }

        if(isValid(x,y-1)){
            if (boardMap[x][y-1] == (isBot ? this.enemyMark : this.selfMark)) {
                return true;
            }
        }

        if(isValid(x+1,y)){
            if (boardMap[x+1][y] == (isBot ? this.enemyMark : this.selfMark)) {
                return true;
            }
        }

        if(isValid(x,y+1)){
            if (boardMap[x][y+1] == (isBot ? this.enemyMark : this.selfMark)) {
                return true;
            }
        }
        return false;
    };

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

    public ArrayList<int[]> searchAdjacent(char[][] boardMap, boolean isBot) {
        ArrayList<int[]> point = new ArrayList<int[]>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardMap[i][j] == ' ' && isAdjacent(boardMap, i, j, isBot)) {
                    point.add(new int[] {i, j});
                }
            }
        }
        return point;
    }
    public ArrayList<PointValue> searchPointValue(char[][] boardMap,boolean isBot, ArrayList<int[]>allAdjacent){
        ArrayList<PointValue>result =new ArrayList<PointValue>();
        for(int[] point : allAdjacent){
            int value = searchTotalAdjacent(boardMap,isBot,new Point(point[0],point[1]));
            result.add(new PointValue(point[0],point[1],value));
        }

        result.sort(Comparator.comparing(PointValue::getPointValue).reversed());
        return result;
    }
    public int searchTotalAdjacent(char[][] boardMap,boolean isBot ,Point point){
        char enemy = this.enemyMark;
        if(!isBot){
            enemy = this.selfMark;
        }
        int total = 0;
        int x = point.x;
        int y = point.y;
        if(isValid(x-1,y)&&boardMap[x-1][y] == enemy){
            total += 1;
        }
        if(isValid(x,y-1) && boardMap[x][y-1] == enemy){
            total += 1;
        }
        if(isValid(x+1,y) && boardMap[x+1][y] == enemy){
            total += 1;
        }
        if(isValid(x,y+1)&&boardMap[x][y+1] == enemy){
            total +=1;
        }
        return total;
    }
    @Override
    public int[] move(char[][] boardMap, int roundLeft, char selfMark, char enemyMark) {
        this.root = new Tree(boardMap);
        this.selfMark = selfMark;
        this.enemyMark = enemyMark;
        Point next = new Point();

        processTree(boardMap, true, 0, roundLeft * 2, -999, 999, next);
        System.out.printf(next.x + " " + next.y);
//        this.root.printTree();
        return new int[] {next.x, next.y};
    }

    public int processTree(char[][] boardMap, boolean isBot, int depth, int leftround, int alpha, int beta, Point selectedPoint) {
        if (leftround > 0 && depth <= 8) {
            ArrayList<int[]> potentialPoint = searchAdjacent(boardMap, isBot);
            ArrayList<PointValue> pointValues = searchPointValue(boardMap,isBot,potentialPoint);
            int maxIteration = (int) Math.ceil(pointValues.size() * 0.5);
            for(int z = 0; z < maxIteration;z++ ){
                PointValue pointValue = pointValues.get(z);
                char [][] state = new char[8][];
                for(int i = 0; i < 8; i++) {
                    state[i] = boardMap[i].clone();
                }
                changeState(state, isBot, pointValue.getPoint().x, pointValue.getPoint().y);
                Point nextPath = new Point(pointValue.getPoint());
                int val = processTree(state, !isBot, depth+1, leftround-1, alpha, beta, nextPath);
//                for (int p = 0; p < depth; p++) {
//                    System.out.print("  ");
//                }
//                System.out.println(pointValue.getPoint().x + " " + pointValue.getPoint().y);
//                for(int i = 0; i < 8; i++) {
//                    for (int p = 0; p < depth; p++) {
//                        System.out.print("  ");
//                    }
//                    System.out.print("| ");
//                    for(int j = 0; j < 8; j++) {
//                        System.out.print(state[i][j] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println("-------------------------");
                if (isBot && val > alpha) {
                    alpha = val;
                    if (depth == 0) {
                        selectedPoint.setLocation(pointValue.getPoint().x,pointValue.getPoint().y);
                    }

                } else if (!isBot && val < beta) {
                    beta = val;
                    if (depth == 0) {
                        selectedPoint.setLocation(pointValue.getPoint().x,pointValue.getPoint().y);
                    }
                }
            }
            return isBot ? alpha : beta;

        } else {
            return calculateObjective(boardMap);
        }
    }

    private void changeState(char[][] state, boolean isBot, int x, int y) {
        state[x][y] = isBot ? this.selfMark : this.enemyMark;
        if(isValid(x-1,y)){
            state[x-1][y] = isBot && state[x-1][y] == this.enemyMark ? this.selfMark : !isBot && state[x-1][y] == this.selfMark ?this.enemyMark : state[x-1][y];
        }

        if(isValid(x,y-1)){
            state[x][y-1] = isBot && state[x][y-1] == this.enemyMark ? this.selfMark : !isBot && state[x][y-1] == this.selfMark ? this.enemyMark : state[x][y-1];
        }

        if(isValid(x+1,y)){
            state[x+1][y] = isBot && state[x+1][y] == this.enemyMark ? this.selfMark : !isBot && state[x+1][y] == this.selfMark ? this.enemyMark : state[x+1][y];
        }

        if(isValid(x,y+1)){
            state[x][y+1] = isBot && state[x][y+1] == this.enemyMark ? this.selfMark : !isBot && state[x][y+1] == this.selfMark ? this.enemyMark : state[x][y+1];
        }
    }


}
