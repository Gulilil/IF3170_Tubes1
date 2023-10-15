package BotMoves;

import javafx.scene.control.Button;
import DataStructure.*;

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
    public boolean isAdjacent(char[][] boardMap, int x, int y){
//        Cek apakah sebuah point memiliki tetangga
        if(isValid(x-1,y)){
            if (boardMap[x-1][y] != ' ') {
                return true;
            }
        }

        if(isValid(x,y-1)){
            if (boardMap[x][y-1] != ' ') {
                return true;
            }
        }

        if(isValid(x+1,y)){
            if (boardMap[x+1][y] != ' ') {
                return true;
            }
        }

        if(isValid(x,y+1)){
            if (boardMap[x][y+1] != ' ') {
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

    public ArrayList<int[]> searchAdjacent(char[][] boardMap) {
        ArrayList<int[]> point = new ArrayList<int[]>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardMap[i][j] == ' ' && isAdjacent(boardMap, i, j)) {
                    point.add(new int[] {i, j});
                }
            }
        }
        return point;
    }

    @Override
    public int[] move(char[][] boardMap) {
        this.root = new Tree(boardMap);

        processTree(boardMap, true, 0, 2, -999, 999);

        this.root.printTree();

        RandomizeAlgo hehe = new RandomizeAlgo();
        return hehe.move(boardMap);
    }

    public int processTree(char[][] boardMap, boolean isBot, int depth, int leftround, int alpha, int beta) {
        if (leftround > 0) {
            ArrayList<int[]> potentialPoint = searchAdjacent(boardMap);
            for (int[] point: potentialPoint) {
                char [][] state = new char[8][];
                for(int i = 0; i < 8; i++) {
                    state[i] = boardMap[i].clone();
                }

                changeState(state, isBot, point[0], point[1]);
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
                int val = processTree(state, !isBot, depth+1, leftround-1, alpha, beta);

                if (isBot && val > alpha) {
                    alpha = val;
                } else if (!isBot && val < beta) {
                    beta = val;
                }
            }
            for (int p = 0; p < depth; p++) {
                System.out.print("  ");
            }
            System.out.print(isBot + "| ");
            System.out.println(isBot ? alpha : beta);
            return isBot ? alpha : beta;
        } else { //
            for (int p = 0; p < depth; p++) {
                System.out.print("  ");
            }
            System.out.print(isBot + "| ");
            System.out.println(calculateObjective(boardMap));
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
