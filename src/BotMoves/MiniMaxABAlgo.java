package BotMoves;

import javafx.scene.control.Button;
import DataStructure.*;

import java.awt.*;
import java.util.ArrayList;

public class MiniMaxABAlgo implements Algorithm{
    private Tree root;

    private int checkBoardValue(Button[][] boardMap){
        int countX = 0;
        int countO = 0;
        for (Button[] rowButtons : boardMap) {
            for (Button tile : rowButtons){
                if (!tile.getText().equals("")){
                    if (tile.getText().equals("O")){
                        countO++;
                    } else {
                        countX++;
                    }
                }
            }
        }
        return countO - countX;
    }

    private boolean checkEndOfGame(Button[][] boardMap){
        int emptyTile = 0;
        for(Button[] rowButtons : boardMap){
            for (Button tile : rowButtons){
                if (tile.getText().equals("")){
                    emptyTile++;
                }
            }
        }
        return emptyTile == 0;
    }
    public boolean isValid(int x, int y){
        if(x >= 0 && x < 8 && y>= 0 && y < 8){
            return true;
        }
        return false;
    }
    public boolean isAdjacent(char[][] boardMap, int x, int y, boolean isBot){
//        Cek apakah sebuah point memiliki tetangga
        if(isValid(x-1,y)){
            if (boardMap[x-1][y] == (isBot ? 'X' : 'O')) {
                return true;
            }
        }

        if(isValid(x,y-1)){
            if (boardMap[x][y-1] == (isBot ? 'X' : 'O')) {
                return true;
            }
        }

        if(isValid(x+1,y)){
            if (boardMap[x+1][y] == (isBot ? 'X' : 'O')) {
                return true;
            }
        }

        if(isValid(x,y+1)){
            if (boardMap[x][y+1] == (isBot ? 'X' : 'O')) {
                return true;
            }
        }
        return false;
    };

    public int calculateObjective(char[][] boardMap){
        int point = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardMap[i][j] == 'O') {
                    point++;
                } else if (boardMap[i][j] == 'X') {
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

    @Override
    public int[] move(char[][] boardMap, int roundLeft) {
        this.root = new Tree(boardMap);

        Point next = new Point();

        processTree(boardMap, true, 0, roundLeft, -999, 999, next);
        System.out.printf(next.x + " " + next.y);
//        this.root.printTree();
        return new int[] {next.x, next.y};
    }

    public int processTree(char[][] boardMap, boolean isBot, int depth, int leftround, int alpha, int beta, Point selectedPoint) {
        if (leftround > 0) {
            ArrayList<int[]> potentialPoint = searchAdjacent(boardMap, isBot);
            for (int[] point: potentialPoint) {
                char [][] state = new char[8][];
                for(int i = 0; i < 8; i++) {
                    state[i] = boardMap[i].clone();
                }

                changeState(state, isBot, point[0], point[1]);

                Point nextpath = new Point(point[0],point[1]);
                int val = processTree(state, !isBot, depth+1, leftround-1, alpha, beta, nextpath);
//                for (int p = 0; p < depth; p++) {
//                    System.out.print("  ");
//                }
//                System.out.println(point[0] + " " + point[1]);
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
                        selectedPoint.setLocation(point[0],point[1]);
                    }

                } else if (!isBot && val < beta) {
                    beta = val;
                    if (depth == 0) {
                        selectedPoint.setLocation(point[0],point[1]);
                    }
                }
            }
//            for (int p = 0; p < depth; p++) {
//                System.out.print("  ");
//            }
//            System.out.print(selectedPoint.x + " " + selectedPoint.y + "=>");
//            System.out.println(isBot ? alpha : beta);
            return isBot ? alpha : beta;
        } else { //
//            for (int p = 0; p < depth; p++) {
//                System.out.print("  ");
//            }
//            System.out.print(selectedPoint.x + " " + selectedPoint.y + "=>");
//            System.out.println(calculateObjective(boardMap));
            return calculateObjective(boardMap);
        }
    }

    private void changeState(char[][] state, boolean isBot, int x, int y) {
        state[x][y] = isBot ? 'O' : 'X';
        if(isValid(x-1,y)){
            state[x-1][y] = isBot && state[x-1][y] == 'X' ? 'O' : !isBot && state[x-1][y] == 'O' ? 'X' : state[x-1][y];
        }

        if(isValid(x,y-1)){
            state[x][y-1] = isBot && state[x][y-1] == 'X' ? 'O' : !isBot && state[x][y-1] == 'O' ? 'X' : state[x][y-1];
        }

        if(isValid(x+1,y)){
            state[x+1][y] = isBot && state[x+1][y] == 'X' ? 'O' : !isBot && state[x+1][y] == 'O' ? 'X' : state[x+1][y];
        }

        if(isValid(x,y+1)){
            state[x][y+1] = isBot && state[x][y+1] == 'X' ? 'O' : !isBot && state[x][y+1] == 'O' ? 'X' : state[x][y+1];
        }
    }


}
