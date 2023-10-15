package DataStructure;

import java.util.ArrayList;

public class Tree {
    private char[][] currentNode;
    private ArrayList<Tree> childs;

    public Tree(char[][] board){
        this.currentNode = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.currentNode[i][j] = board[i][j];
            }
        }
        this.childs = new ArrayList<Tree>();
    }

    public void addChild(Tree board){
        this.childs.add(board);
    }

    public boolean isTerminal(){
      return this.childs.isEmpty();
    };

    public void printTree() {
        System.out.println("Print tree");
        printTreeHelper(this, 0);
    }

    private void printTreeHelper(Tree node, int depth) {
        char[][] board = node.currentNode;

        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }

        System.out.println("Depth " + depth + ":");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        for (Tree child : node.childs) {
            printTreeHelper(child, depth + 1);
        }
    }


}
