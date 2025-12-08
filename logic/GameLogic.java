package tictactoe.logic;

public class GameLogic {

    private final String[] board = new String[9];
    private boolean xTurn = true;

    public GameLogic() {
        reset();
    }

    public boolean isXTurn() {
        return xTurn;
    }

    public boolean makeMove(int index) {
        if (!board[index].isEmpty()) return false;

        board[index] = xTurn ? "X" : "O";
        return true;
    }

    public void undoMove(int index) {
        board[index] = "";
    }


    public boolean checkWinner() {
        int[][] wins = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };

        for (int[] w : wins) {
            String a = board[w[0]], b = board[w[1]], c = board[w[2]];
            if (!a.isEmpty() && a.equals(b) && b.equals(c)) return true;
        }
        return false;
    }

    public boolean isDraw() {
        for (String s : board) if (s.isEmpty()) return false;
        return true;
    }

    public void switchTurn() {
        xTurn = !xTurn;
    }

    public void reset() {
        for (int i = 0; i < 9; i++) board[i] = "";
        xTurn = true;
    }
}
