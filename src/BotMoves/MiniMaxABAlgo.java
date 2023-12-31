package BotMoves;

import DataStructure.PointValue;
import DataStructure.Tree;
import javafx.scene.control.Button;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class MiniMaxABAlgo implements Algorithm{

    private Tree root;

    private char selfMark;
    private char enemyMark;
    private int maxDepth;
    private Double maxWidth;
    private long startTime;

    // validate point
    public boolean isValid(int x, int y){
        if(x >= 0 && x < 8 && y>= 0 && y < 8){
            return true;
        }
        return false;
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

    // generate successor based on heuristic
    public ArrayList<PointValue> searchAdjacent(char[][] boardMap, boolean isBot) {
        ArrayList<int[]> point = new ArrayList<int[]>();
        ArrayList<PointValue> pointValues = new ArrayList<PointValue>();
        PointValue edge = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int totalAdj = searchTotalAdjacent(boardMap,isBot,new Point(i,j));
                int totalDia = searchDiagonal(boardMap, isBot, new Point(i,j));
                int val = 2*totalAdj - totalDia ;
//                Memastikan bahwa boardMap[i][j] kosong dan memiliki tetangga berupa musuh
                if (boardMap[i][j] == ' '){
                    if(totalAdj > 0) {
                        pointValues.add(new PointValue(i, j, val));
                    }else if (edge==null){ //edge case
                        edge= new PointValue(i,j,val);
                    }
                }
            }
        }

        // edge case
        if(pointValues.isEmpty() && edge!=null){
            pointValues.add(edge);
        }
        pointValues.sort(Comparator.comparing(PointValue::getPointValue).reversed());
        return pointValues;
    }

    // heuristic point
    public int searchTotalAdjacent(char[][] boardMap,boolean isBot ,Point point){
        char enemy = this.enemyMark;
        if(!isBot){
            enemy = this.selfMark;
        }
        int total = 0;
        int x = point.x;
        int y = point.y;
        // left
        if(isValid(x-1,y)&&boardMap[x-1][y] == enemy){
            total += 1;
        }
        // down
        if(isValid(x,y-1) && boardMap[x][y-1] == enemy){
            total += 1;
        }
        // right
        if(isValid(x+1,y) && boardMap[x+1][y] == enemy){
            total += 1;
        }
        // up
        if(isValid(x,y+1)&&boardMap[x][y+1] == enemy){
            total +=1;
        }
        return total;
    }

    // heuristic defensive pattern
    public int searchDiagonal(char[][] boardMap,boolean isBot ,Point point){
        char self = this.selfMark;
        if(!isBot){
            self = this.enemyMark;
        }
        int total = 0;
        int x = point.x;
        int y = point.y;
        if(isValid(x-1,y-1)&&boardMap[x-1][y-1] == self){
            // left and down
            if((isValid(x-1,y)&&boardMap[x-1][y] == ' ')||(isValid(x,y-1) && boardMap[x][y-1] == ' ')){
                total += 1;
            }
        }
        else if(isValid(x+1,y-1) && boardMap[x+1][y-1] == self){
            // right and down
            if((isValid(x+1,y)&&boardMap[x+1][y] == ' ') || (isValid(x,y-1) && boardMap[x][y-1] == ' ')){
                total += 1;
            }
        }
        else if(isValid(x+1,y+1) && boardMap[x+1][y+1] == self){
            // right and up
            if((isValid(x+1,y)&&boardMap[x+1][y] == ' ' )||( isValid(x,y+1) && boardMap[x][y+1] == ' ')){
                total += 1;
            }
        }
        else if(isValid(x-1,y+1)&&boardMap[x-1][y+1] == self){
            // left and up
            if((isValid(x-1,y)&&boardMap[x-1][y] == ' ')||(isValid(x,y+1) && boardMap[x][y+1] == ' ')){
                total += 1;
            }
        }
        return total;
    }
    @Override
    public int[] move(char[][] boardMap, int roundLeft, char selfMark, char enemyMark) {
        this.root = new Tree(boardMap);
        this.selfMark = selfMark;
        this.enemyMark = enemyMark;
        this.maxDepth = 8;
        this.maxWidth = 0.5;
        this.startTime = System.currentTimeMillis();

        Point next = new Point();

        processTree(boardMap, true, 0, roundLeft * 2, -999, 999, next);
        return new int[] {next.x, next.y};
    }

    public int processTree(char[][] boardMap, boolean isBot, int depth, int leftround, int alpha, int beta, Point selectedPoint) {
        // if processing time > 5s, then terminate program
        if(System.currentTimeMillis() - this.startTime >5000){
            return calculateObjective(boardMap);
        }
        if (leftround > 0 && depth <= this.maxDepth) {
            // generate successor
            ArrayList<PointValue> pointValues = searchAdjacent(boardMap, isBot);
            int maxIteration = (int) Math.ceil(pointValues.size() * this.maxWidth);
            for(int z = 0; z < maxIteration;z++ ){
                PointValue pointValue = pointValues.get(z);
                char [][] state = new char[8][];
                for(int i = 0; i < 8; i++) {
                    state[i] = boardMap[i].clone();
                }
                changeState(state, isBot, pointValue.getPoint().x, pointValue.getPoint().y);
                Point nextPath = new Point(pointValue.getPoint());
                int val = processTree(state, !isBot, depth+1, leftround-1, alpha, beta, nextPath);
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
                if(beta<=alpha){
                    break;
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

