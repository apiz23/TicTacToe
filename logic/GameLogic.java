package tictactoe.logic;

public class GameLogic {

    private final String[] board = new String[9];
    private boolean xTurn = true;

    public GameLogic() {
        reset();
    }

    /* ---------------------- TURN CONTROL ---------------------- */

    public boolean isXTurn() {
        return xTurn;
    }

    public void switchTurn() {
        xTurn = !xTurn;
    }

    /* ---------------------- BOARD CONTROL ---------------------- */

    public String[] getBoard() {
        return board;
    }

    public boolean isCellEmpty(int index) {
        return board[index].isEmpty();
    }

    public void setCell(int index, String symbol) {
        board[index] = symbol;
    }

    public boolean makeMove(int index) {
        if (!isCellEmpty(index)) return false;

        board[index] = xTurn ? "X" : "O";
        return true;
    }

    public void undoMove(int index) {
        board[index] = "";
    }

    /* ------------------------- GAME CHECKS ------------------------- */

    public boolean checkWinner() {
        int[][] wins = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };

        for (int[] w : wins) {
            String a = board[w[0]];
            String b = board[w[1]];
            String c = board[w[2]];

            if (!a.isEmpty() && a.equals(b) && b.equals(c)) {
                return true;
            }
        }
        return false;
    }

    public String getWinnerSymbol() {
        int[][] wins = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };

        for (int[] w : wins) {
            String a = board[w[0]];
            String b = board[w[1]];
            String c = board[w[2]];

            if (!a.isEmpty() && a.equals(b) && b.equals(c)) {
                return a;
            }
        }
        return "";
    }

    public boolean isDraw() {
        for (String cell : board) {
            if (cell.isEmpty()) return false;
        }
        return true;
    }

    /* ------------------------- RESET ------------------------- */

    public void reset() {
        for (int i = 0; i < 9; i++) {
            board[i] = "";
        }
        xTurn = true;
    }
}
