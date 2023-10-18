import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

import java.io.IOException;

/**
 * The OutputFrameController class.  It controls button input from the users when
 * playing the game.
 *
 * @author Jedid Ahn
 *
 */
public class OutputFrameController {
    @FXML
    private GridPane gameBoard;

    @FXML
    private GridPane scoreBoard;

    @FXML
    private Label roundsLeftLabel;
    @FXML
    private Label playerXName;
    @FXML
    private Label playerOName;
    @FXML
    private HBox playerXBoxPane;
    @FXML
    private HBox playerOBoxPane;
    @FXML
    private Label playerXScoreLabel;
    @FXML
    private Label playerOScoreLabel;


    private boolean playerXTurn;
    private int playerXScore;
    private int playerOScore;
    private int roundsLeft;
    private boolean isOFirst;
    private Bot botX;
    private Bot botO;

    private int delay;


    private static final int ROW = 8;
    private static final int COL = 8;
    private final Button[][] buttons = new Button[ROW][COL];


    /**
     * Set the name of player X (player) to be name1, set the name of player O (bot) to be name2,
     * and the number of rounds played to be rounds. This input is received from
     * the input frame and is output in the score board of the output frame.
     *
     * @param name1 Name of Player 1 (Player).
     * @param name2 Name of Player 2 (Bot).
     * @param rounds The number of rounds chosen to be played.
     * @param isOFirst True if player O is first, false otherwise.
     *
     */
    void getInput(String name1, String name2, String rounds, boolean isOFirst, String algorithm1, String algorithm2, int delay){
        this.playerXName.setText(name1);
        this.playerOName.setText(name2);
        this.roundsLeftLabel.setText(rounds);
        this.roundsLeft = Integer.parseInt(rounds);
        this.isOFirst = isOFirst;

        // List : "Human", "Bot Minimax", "Bot Simulated Annealing", "Bot Genetic", "Bot Randomize"
        if (!algorithm1.equals("Human")){
            int optionBot1;

            if (algorithm1.equals("Bot Minimax Algo")) {
                optionBot1 = 1;
            } else if (algorithm1.equals("Bot Simulated Annealing")) {
                optionBot1 = 2;
            } else if (algorithm1.equals("Bot Sideways Move")) {
                optionBot1 = 3;
            } else if (algorithm1.equals("Bot Genetic Algo")) {
                optionBot1 = 4;
            } else {
                optionBot1 = 5;
            }
            this.botX = new Bot(optionBot1, 'X', 'O');
        } else {
            this.botX = null;
        }

        if (!algorithm2.equals("Human")){
            int optionBot2;
            if (algorithm2.equals("Bot Minimax Algo")) {
                optionBot2 = 1;
            } else if (algorithm2.equals("Bot Simulated Annealing")) {
                optionBot2 = 2;
            } else if (algorithm2.equals("Bot Sideways Move")) {
                optionBot2 = 3;
            } else if (algorithm2.equals("Bot Genetic Algo")) {
                optionBot2 = 4;
            }  else {
                optionBot2 = 5;
            }
            this.botO = new Bot(optionBot2, 'O', 'X');
        } else {
            this.botO = null;
        }

        this.delay = delay;
        this.playerXTurn = !isOFirst;
        if (isOFirst){
            this.movePlayerO();
        } else {
            this.movePlayerX();
        }
    }



    /**
     * Construct the 8x8 game board by creating a total of 64 buttons in a 2
     * dimensional array, and construct the 8x2 score board for scorekeeping
     * and then initialize turn and score.
     *
     */
    @FXML
    private void initialize() {
        // Construct game board with 8 rows.
        for (int i = 0; i < ROW; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ROW);
            this.gameBoard.getRowConstraints().add(rowConst);
        }

        // Construct game board with 8 columns.
        for (int i = 0; i < COL; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / COL);
            this.gameBoard.getColumnConstraints().add(colConst);
        }

        // Style buttons and construct 8x8 game board.
        for (int i = 0; i < ROW; i++){
            for (int j = 0; j < COL; j++) {
                this.buttons[i][j] = new Button();
                this.buttons[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                this.buttons[i][j].setCursor(Cursor.HAND);
                this.gameBoard.add(this.buttons[i][j], j, i);

                // Add ActionListener to each button such that when it is clicked, it calls
                // the selected coordinates method with its i and j coordinates.
                final int finalI = i;
                final int finalJ = j;
                this.buttons[i][j].setOnAction(event -> this.selectedCoordinates(finalI, finalJ));
            }
        }

        // Setting up the initial game board with 4 X's in bottom left corner and 4 O's in top right corner.
        this.buttons[ROW - 2][0].setText("X");
        this.buttons[ROW - 1][0].setText("X");
        this.buttons[ROW - 2][1].setText("X");
        this.buttons[ROW - 1][1].setText("X");
        this.buttons[0][COL - 2].setText("O");
        this.buttons[0][COL - 1].setText("O");
        this.buttons[1][COL - 2].setText("O");
        this.buttons[1][COL - 1].setText("O");


        // Construct score board with 8 rows.
        for (int i = 0; i < ROW; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ROW);
            this.scoreBoard.getRowConstraints().add(rowConst);
        }

        // Construct score board with 2 column.
        for (int i = 0; i < 2; i++){
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 2);
            this.scoreBoard.getColumnConstraints().add(colConst);
        }


        // Initialize turn and score for the game.
        if (playerXTurn){
            this.playerXBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
            this.playerOBoxPane.setStyle("-fx-background-color: white; -fx-border-color: #D3D3D3;");
        } else {
            this.playerXBoxPane.setStyle("-fx-background-color: white; -fx-border-color: #D3D3D3;");
            this.playerOBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
        }

        this.playerXScoreLabel.setText("4");
        this.playerOScoreLabel.setText("4");

        this.playerXTurn = !isOFirst;
        this.playerXScore = 4;
        this.playerOScore = 4;
    }



    /**
     * Process the coordinates of the button that the user selected on the game board.
     *
     * @param i The row number of the button clicked.
     * @param j The column number of the button clicked.
     *
     */
    private void selectedCoordinates(int i, int j){
        // Invalid when a button with an X or an O is clicked.
        if (!this.buttons[i][j].getText().equals(""))
            new Alert(Alert.AlertType.ERROR, "Invalid coordinates: Try again!").showAndWait();
        // Button must be blank.
        else {
                if (this.playerXTurn) {
                    // Changed background color to green to indicate next player's turn.
                    this.playerXBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                    this.playerOBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                    this.buttons[i][j].setText("X");  // Mark the board with X.
                    this.playerXScore++;              // Increment the score of player X.

                    // Update game board by changing surrounding cells to X if applicable.
                    this.updateGameBoard(i, j);

                    if (isOFirst) {
                        this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have played.
                        this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                    }

                }
                else {
                    this.playerXBoxPane.setStyle("-fx-background-color: #90EE90; -fx-border-color: #D3D3D3;");
                    this.playerOBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
                    this.buttons[i][j].setText("O");
                    this.playerOScore++;

                    this.updateGameBoard(i, j);

                    if (!isOFirst) {
                        this.roundsLeft--; // Decrement the number of rounds left after both Player X & Player O have played.
                        this.roundsLeftLabel.setText(String.valueOf(this.roundsLeft));
                    }

                }

            if (this.botO != null && this.botX != null){
                // Delay is only implemented if there is no human player
                PauseTransition pause = new PauseTransition(Duration.millis(delay));
                pause.setOnFinished(event -> {
                    if (this.roundsLeft != 0){
                        this.nextPlayerMove();
                    } else {
                        pause.stop();
                        this.endOfGame();
                    }
                });
                pause.play();
            } else {
                // Proceed to continue the game otherwise
                if (this.roundsLeft == 0){
                    this.endOfGame();
                } else {
                    this.nextPlayerMove();
                }

            }
        }
    }

    private void nextPlayerMove(){
        if (playerXTurn) {
            // Change turn to Player O
            this.playerXTurn = false;         // Alternate player's turn.
            this.movePlayerO();
        }
        else {
            // Change turn to Player X
            this.playerXTurn = true;
            this.movePlayerX();
        }
    }

    /**
     * Change adjacent cells to X's or O's.
     *
     * @param i The row number of the button clicked.
     * @param j The column number of the button clicked.
     *
     */
    private void updateGameBoard(int i, int j) {
        // Value of indices to control the lower/upper bound of rows and columns
        // in order to change surrounding/adjacent X's and O's only on the game board.
        // Four boundaries:  First & last row and first & last column.

        int startRow, endRow, startColumn, endColumn;

        if (i - 1 < 0)     // If clicked button in first row, no preceding row exists.
            startRow = i;
        else               // Otherwise, the preceding row exists for adjacency.
            startRow = i - 1;

        if (i + 1 >= ROW)  // If clicked button in last row, no subsequent/further row exists.
            endRow = i;
        else               // Otherwise, the subsequent row exists for adjacency.
            endRow = i + 1;

        if (j - 1 < 0)     // If clicked on first column, lower bound of the column has been reached.
            startColumn = j;
        else
            startColumn = j - 1;

        if (j + 1 >= COL)  // If clicked on last column, upper bound of the column has been reached.
            endColumn = j;
        else
            endColumn = j + 1;


        // Search for adjacency for X's and O's or vice versa, and replace them.
        // Update scores for X's and O's accordingly.
        for (int x = startRow; x <= endRow; x++) {
            this.setPlayerScore(x, j);
        }

        for (int y = startColumn; y <= endColumn; y++) {
            this.setPlayerScore(i, y);
        }

        this.playerXScoreLabel.setText(String.valueOf(this.playerXScore));
        this.playerOScoreLabel.setText(String.valueOf(this.playerOScore));
    }

    private void setPlayerScore(int i, int j){
        if (this.playerXTurn) {
            if (this.buttons[i][j].getText().equals("O")) {
                this.buttons[i][j].setText("X");
                this.playerXScore++;
                this.playerOScore--;
            }
        } else if (this.buttons[i][j].getText().equals("X")) {
            this.buttons[i][j].setText("O");
            this.playerOScore++;
            this.playerXScore--;
        }
    }


    /**
     * Determine and announce the winner of the game.
     *
     */
    private void endOfGame(){
        // Player X is the winner.
        if (this.playerXScore > this.playerOScore) {
            new Alert(Alert.AlertType.INFORMATION,
                    this.playerXName.getText() + " has won the game!").show();
            this.playerXBoxPane.setStyle("-fx-background-color: CYAN; -fx-border-color: #D3D3D3;");
            this.playerOBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
            this.playerXName.setText(this.playerXName.getText() + " (Winner!)");
        }

        // Player O is the winner,
        else if (this.playerOScore > this.playerXScore) {
            new Alert(Alert.AlertType.INFORMATION,
                    this.playerOName.getText() + " has won the game!").show();
            this.playerXBoxPane.setStyle("-fx-background-color: WHITE; -fx-border-color: #D3D3D3;");
            this.playerOBoxPane.setStyle("-fx-background-color: CYAN; -fx-border-color: #D3D3D3;");
            this.playerOName.setText(this.playerOName.getText() + " (Winner!)");
        }

        // Player X and Player O tie.
        else {
            new Alert(Alert.AlertType.INFORMATION,
                    this.playerXName.getText() + " and " + this.playerOName.getText() + " have tied!").show();
            this.playerXBoxPane.setStyle("-fx-background-color: ORANGE; -fx-border-color: #D3D3D3;");
            this.playerOBoxPane.setStyle("-fx-background-color: ORANGE; -fx-border-color: #D3D3D3;");
        }

        // Disable the game board buttons to prevent from playing further.
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL; j++)
                this.buttons[i][j].setDisable(true);
    }


    /**
     * Close OutputFrame controlled by OutputFrameController if end game button is clicked.
     *
     */
    @FXML
    private void endGame(){
        System.exit(0);
    }


    /**
     * Reopen InputFrame controlled by InputFrameController if play new game button is clicked.
     *
     */
    @FXML
    private void playNewGame() throws IOException {
        // Close secondary stage/output frame.
        Stage secondaryStage = (Stage) this.gameBoard.getScene().getWindow();
        secondaryStage.close();

        // Reopen primary stage/input frame.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InputFrame.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Adjacency Gameplay");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void movePlayerX(){
        if (this.botX != null){
            int[] botXMove = this.botX.move(buttons, this.roundsLeft);
            int i = botXMove[0];
            int j = botXMove[1];

            if (!this.buttons[i][j].getText().equals("")) {
                new Alert(Alert.AlertType.ERROR, "Bot Invalid Coordinates. Exiting.").showAndWait();
                System.exit(1);
                return;
            }
            this.selectedCoordinates(i, j);
        }
    }

    private void movePlayerO(){
        if (this.botO != null){
            int[] botOMove = this.botO.move(buttons, this.roundsLeft);
            int i = botOMove[0];
            int j = botOMove[1];

            if (!this.buttons[i][j].getText().equals("")) {
                new Alert(Alert.AlertType.ERROR, "Bot Invalid Coordinates. Exiting.").showAndWait();
                System.exit(1);
                return;
            }
            this.selectedCoordinates(i, j);
        }
    }

}