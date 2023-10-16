package BotMoves;

import javafx.scene.control.Button;

public class LocalSearchAlgo implements Algorithm{

//    private int checkBoardValue(char[][] boardMap){
////        int countX = 0;
////        int countO = 0;
////        for (char[] rowButtons : boardMap) {
////            for (char tile : rowButtons){
////                if (!tile.getText().equals("")){
////                    if (tile.getText().equals("O")){
////                        countO++;
////                    } else {
////                        countX++;
////                    }
////                }
////            }
////        }
////        return countO - countX;
//    }
    @Override
    public int[] move(char[][] boardMap, int roundLeft) {
        return new int[] {1,1};
    }
}
