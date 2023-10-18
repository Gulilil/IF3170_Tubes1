package BotMoves;

import java.util.ArrayList;
import java.util.Arrays;

public class GASearchAlgo implements Algorithm{

    private char[][] map;
    private char selfMark;
    private char enemyMark;

    public void initializeMap(char[][] boardMap) {
        this.map = new char[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(boardMap[i], 0, this.map[i], 0, 8);
        }
    }

    public ArrayList<int[]> getEmpty() {
        ArrayList<int[]> point = new ArrayList<int[]>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.map[i][j] == ' ') {
                    point.add(new int[] {i, j});
                }
            }
        }
        return point;
    }

    public int[] getParent(ArrayList<int[]> emptyCell, int roundsLeft) {
        int[] parent = new int[roundsLeft];
        int length = emptyCell.size();
        for (int i = 0; i < roundsLeft; i++) {
            boolean retry = true;
            int selected = -1;
            while (retry) {
                selected = (int) (Math.random()*length);
                retry = false;
                for (int j = 0; j < i; j++) {
                    if (parent[j] == selected) {
                        retry = true;
                        break;
                    }
                }
            }
            parent[i] = selected;
        }
        return parent;
    }

    public void crossover(ArrayList<int[]> emptyCell, int roundsLeft, int[] parent1, int[] parent2) {
        int crossoverpoint = (int) (Math.random()*roundsLeft);
        while (crossoverpoint == 0) {
            crossoverpoint = (int) (Math.random()*roundsLeft);
        }

        ArrayList<Integer> buffer = new ArrayList<Integer>();

        System.out.println("Crossover point : " + crossoverpoint);
        for (int i = crossoverpoint; i < roundsLeft; i++) {
            buffer.add(parent1[i]);
            parent1[i] = parent2[i];
        }

        for (int i = crossoverpoint; i < roundsLeft; i++) {
            parent2[i] = buffer.get(i-crossoverpoint);
        }
    }

    private void mutation(ArrayList<int[]> emptyCell, int roundsLeft, int[] gene1, int[] gene2) {
        ArrayList<Integer> doublegene1 = searchDouble(gene1, emptyCell.size());
        ArrayList<Integer> doublegene2 = searchDouble(gene2, emptyCell.size());

        System.out.println("Mutate Gene 1");
        mutateGene(emptyCell.size(), gene1, doublegene1, roundsLeft);
        System.out.println("Mutate Gene 2");
        mutateGene(emptyCell.size(), gene2, doublegene2, roundsLeft);

        heuristicFirst(emptyCell, gene1);
        heuristicFirst(emptyCell, gene2);
    }

    private ArrayList<Integer> searchDouble(int[] gene, int length) {
        ArrayList<Integer> valueDouble = new ArrayList<>();
        int[] dictionary = new int[length];
        for(int i = 0; i < length; i++) {
            dictionary[i] = -1;
        }

        for(int i = 0; i < gene.length; i++) {
            if (dictionary[gene[i]] == -1) {
                dictionary[gene[i]] = i;
            } else {
                valueDouble.add(i);
                if (valueDouble.indexOf(dictionary[gene[i]]) == -1) {
                    valueDouble.add(dictionary[gene[i]]);
                }
            }
        }

        return valueDouble;
    }

    private void mutateGene(int length, int[] gene, ArrayList<Integer> mutationpoint, int roundsLeft) {
        if (mutationpoint.isEmpty()) {
            int point = (int) (Math.random()*roundsLeft);
            System.out.println("Mutation point: " + point);
            boolean retry = true;
            int selected = -1;
            while (retry) {
                selected = (int) (Math.random()*length);
                retry = false;
                for (int j = 0; j < roundsLeft; j++) {
                    if (gene[j] == selected) {
                        retry = true;
                        break;
                    }
                }
            }
            gene[point] = selected;
        } else {
            for (int i = 0; i < mutationpoint.size(); i++) {
                System.out.println("Mutation point: " + mutationpoint.get(i));
                boolean retry = true;
                int selected = -1;
                while (retry) {
                    selected = (int) (Math.random()*length);
                    retry = false;
                    for (int j = 0; j < roundsLeft; j++) {
                        if (gene[j] == selected) {
                            retry = true;
                            break;
                        }
                    }
                }
                gene[mutationpoint.get(i)] = selected;
            }
        }
    }

    private void heuristicFirst(ArrayList<int[]> emptyCell, int[] gene) {
        while (!isAdjacent(this.map, emptyCell.get(gene[0])[0], emptyCell.get(gene[0])[1], true)) {
            boolean retry = true;
            int selected = -1;
            while (retry) {
                selected = (int) (Math.random() * emptyCell.size());
                retry = false;
                for (int j = 0; j < gene.length; j++) {
                    if (gene[j] == selected) {
                        retry = true;
                        break;
                    }
                }
            }
            gene[0] = selected;
        }
    }

    private int evaluate(ArrayList<int[]> emptyCell, int[] gene) {
        char [][] state = new char[8][8];
        for(int i = 0; i < 8; i++) {
            state[i] = this.map[i].clone();
        }

        boolean isBot = true;
        for (int idx : gene) {
            changeState(state, isBot, emptyCell.get(idx)[0], emptyCell.get(idx)[1]);
            isBot = !isBot;
//            System.out.println("Point: " + emptyCell.get(idx)[0] + " " + emptyCell.get(idx)[1]);
//            for(int i = 0; i < 8; i++) {
//                for(int j = 0; j < 8; j++) {
//                    System.out.print(state[i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println("---------------------------------");
        }

        return calculateObjective(state);
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

    public boolean isValid(int x, int y){
        if(x >= 0 && x < 8 && y>= 0 && y < 8){
            return true;
        }
        return false;
    }

    public boolean isAdjacent(char[][] boardMap, int x, int y, boolean isBot){
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

    public int[] geneticAlgorithm(ArrayList<int[]> emptyCell, int roundsLeft) {
        int[] gene1, gene2, selectedgene = new int[0];
        boolean retry = true;
        int i = 0;
        while (retry) {
            gene1 = getParent(emptyCell, roundsLeft);
            gene2 = getParent(emptyCell, roundsLeft);
            printGeneStatus(emptyCell, roundsLeft, gene1, gene2);

            crossover(emptyCell, roundsLeft, gene1, gene2);
            System.out.println("After Crossover");
            printGeneStatus(emptyCell, roundsLeft, gene1, gene2);

            mutation(emptyCell, roundsLeft, gene1, gene2);
            System.out.println("After Mutation");
            printGeneStatus(emptyCell, roundsLeft, gene1, gene2);

            int point1 = evaluate(emptyCell, gene1);
            int point2 = evaluate(emptyCell, gene2);
            System.out.println("Point: " + point1 + " " + point2);

            int maxpoint = Math.max(point1, point2);
            if (maxpoint < 0) {
                retry = true;
                i++;
                if (i == 10){
                    selectedgene = point1 > point2 ? gene1 : gene2;
                    break;
                }
            } else {
                selectedgene = point1 > point2 ? gene1 : gene2;
                retry = false;
            }
        }

        return new int[] {emptyCell.get(selectedgene[0])[0], emptyCell.get(selectedgene[0])[1]};
    }



    @Override
    public int[] move(char[][] boardMap, int roundLeft, char selfMark, char enemyMark) {
        initializeMap(boardMap);
        ArrayList<int[]> emptyCell = this.getEmpty();
        this.selfMark = selfMark;
        this.enemyMark = enemyMark;

        return geneticAlgorithm(emptyCell, roundLeft * 2);
    }

    public void printGeneStatus(ArrayList<int[]> emptyCell, int roundsLeft, int[] parent1, int[] parent2) {
        System.out.println("Empty Cell");
        for (int[] ints : emptyCell) {
            System.out.print(ints[0] + " " + ints[1] + " | ");
        }
        System.out.println();
        System.out.println("Parent 1");
        for (int i = 0; i < roundsLeft; i++) {
            System.out.print(emptyCell.get(parent1[i])[0] + " " + emptyCell.get(parent1[i])[1] + " | ");
        }
        System.out.println();
        System.out.println("Parent 2");
        for (int i = 0; i < roundsLeft; i++) {
            System.out.print(emptyCell.get(parent2[i])[0] + " " + emptyCell.get(parent2[i])[1] + " | ");
        }
        System.out.println();
    }

    @Override
    public int calculateObjective(char[][] boardMap) {
        return 0;
    }
}
