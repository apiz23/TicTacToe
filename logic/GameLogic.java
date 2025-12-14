package tictactoe.logic;

public class GameLogic {
    private String[] board;
    private boolean isXTurn;
    private int boardSize;

    public GameLogic(int boardSize) {
        this.boardSize = boardSize;
        this.board = new String[boardSize * boardSize];
        this.isXTurn = true;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            board[i] = "";
        }
    }

    public boolean makeMove(int index) {
        if (index < 0 || index >= board.length || !board[index].isEmpty()) {
            return false;
        }
        board[index] = isXTurn ? "X" : "O";
        return true;
    }

    public void switchTurn() {
        isXTurn = !isXTurn;
    }

    public boolean isXTurn() {
        return isXTurn;
    }

    public boolean checkWinner() {
        // Check all rows
        for (int row = 0; row < boardSize; row++) {
            boolean rowWin = true;
            String first = board[row * boardSize];
            if (first.isEmpty()) continue;

            for (int col = 1; col < boardSize; col++) {
                if (!board[row * boardSize + col].equals(first)) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) return true;
        }

        // Check all columns
        for (int col = 0; col < boardSize; col++) {
            boolean colWin = true;
            String first = board[col];
            if (first.isEmpty()) continue;

            for (int row = 1; row < boardSize; row++) {
                if (!board[row * boardSize + col].equals(first)) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) return true;
        }

        // Check main diagonal (top-left to bottom-right)
        boolean diagWin1 = true;
        String first1 = board[0];
        if (!first1.isEmpty()) {
            for (int i = 1; i < boardSize; i++) {
                if (!board[i * boardSize + i].equals(first1)) {
                    diagWin1 = false;
                    break;
                }
            }
            if (diagWin1) return true;
        }

        // Check anti-diagonal (top-right to bottom-left)
        boolean diagWin2 = true;
        String first2 = board[boardSize - 1];
        if (!first2.isEmpty()) {
            for (int i = 1; i < boardSize; i++) {
                if (!board[i * boardSize + (boardSize - 1 - i)].equals(first2)) {
                    diagWin2 = false;
                    break;
                }
            }
            if (diagWin2) return true;
        }

        return false;
    }

    public boolean isDraw() {
        for (String cell : board) {
            if (cell.isEmpty()) {
                return false;
            }
        }
        return !checkWinner();
    }

    public void reset() {
        initializeBoard();
        isXTurn = true;
    }

    public int getBoardSize() {
        return boardSize;
    }
}