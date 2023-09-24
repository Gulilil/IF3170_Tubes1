package BotMoves;

import javafx.scene.control.Button;

public class MiniMaxABAlgo implements Algorithm{

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
    @Override
    public int[] move(Button[][] boardMap) {
        return new int[] {1,1};
    }

}
