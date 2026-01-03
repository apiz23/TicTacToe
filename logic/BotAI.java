package tictactoe.logic;

import java.util.Random;

public class BotAI {

    public static int getMove(String difficulty, String[] board, int boardSize, int spotsTaken) {
        if ("Easy".equals(difficulty)) {
            return easyBot(board, boardSize);
        } else if ("Medium".equals(difficulty)) {
            return mediumBot(board, boardSize, spotsTaken);
        } else if ("Hard".equals(difficulty)) {
            return hardBot(board, boardSize, spotsTaken);
        }
        return -1;
    }

    private static int easyBot(String[] board, int boardSize) {
        Random rand = new Random();
        int botIndex;
        // Safety check to prevent infinite loop if board is full
        boolean full = true;
        for (String s : board) {
            if (s.isEmpty()) {
                full = false;
                break;
            }
        }
        if (full) return -1;

        do {
            botIndex = rand.nextInt(boardSize * boardSize);
        } while (!board[botIndex].isEmpty());
        return botIndex;
    }

    private static int mediumBot(String[] board, int boardSize, int spotsTaken) {
        // 1. Check if bot (O) can win
        for (int i = 0; i < board.length; i++) {
            if (board[i].isEmpty()) {
                board[i] = "O"; // Try move
                if (checkWinningMove(board, boardSize)) {
                    board[i] = ""; // Reset
                    return i;
                }
                board[i] = ""; // Reset
            }
        }

        // 2. Block player (X)
        for (int i = 0; i < board.length; i++) {
            if (board[i].isEmpty()) {
                board[i] = "X"; // Simulate Player
                if (checkWinningMove(board, boardSize)) {
                    board[i] = ""; // Reset
                    return i;
                }
                board[i] = ""; // Reset
            }
        }

        return easyBot(board, boardSize);
    }

    private static int hardBot(String[] board, int boardSize, int spotsTaken) {
        // For larger boards, use limited depth minimax to prevent lag
        int maxDepth = (boardSize <= 3) ? 9 : 4;

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < board.length; i++) {
            if (board[i].isEmpty()) {
                board[i] = "O"; // Bot makes move
                int score = minimax(board, boardSize, false, 0, maxDepth);
                board[i] = ""; // Undo move

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove == -1) return easyBot(board, boardSize);
        return bestMove;
    }

    private static int minimax(String[] board, int boardSize, boolean isMaximizing, int depth, int maxDepth) {
        if (depth >= maxDepth) return 0;

        if (checkWinningMove(board, boardSize)) {
            String winner = getWinner(board, boardSize);
            if (winner.equals("O")) return 10 - depth;
            else if (winner.equals("X")) return depth - 10;
        }

        boolean boardFull = true;
        for (String s : board) {
            if (s.isEmpty()) {
                boardFull = false;
                break;
            }
        }
        if (boardFull) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < board.length; i++) {
                if (board[i].isEmpty()) {
                    board[i] = "O";
                    int score = minimax(board, boardSize, false, depth + 1, maxDepth);
                    board[i] = "";
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                if (board[i].isEmpty()) {
                    board[i] = "X";
                    int score = minimax(board, boardSize, true, depth + 1, maxDepth);
                    board[i] = "";
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    // Helper: Check for winning move on the virtual board array
    private static boolean checkWinningMove(String[] board, int boardSize) {
        // Rows
        for (int row = 0; row < boardSize; row++) {
            String first = board[row * boardSize];
            if (first.isEmpty()) continue;
            boolean win = true;
            for (int col = 1; col < boardSize; col++) {
                if (!board[row * boardSize + col].equals(first)) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        // Cols
        for (int col = 0; col < boardSize; col++) {
            String first = board[col];
            if (first.isEmpty()) continue;
            boolean win = true;
            for (int row = 1; row < boardSize; row++) {
                if (!board[row * boardSize + col].equals(first)) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        // Diagonals
        String firstD1 = board[0];
        if (!firstD1.isEmpty()) {
            boolean win = true;
            for (int i = 1; i < boardSize; i++) {
                if (!board[i * boardSize + i].equals(firstD1)) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        String firstD2 = board[boardSize - 1];
        if (!firstD2.isEmpty()) {
            boolean win = true;
            for (int i = 1; i < boardSize; i++) {
                if (!board[i * boardSize + (boardSize - 1 - i)].equals(firstD2)) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        return false;
    }

    private static String getWinner(String[] board, int boardSize) {
        // Simplified winner check reusing logic structure (returns "X" or "O")
        // Rows
        for (int row = 0; row < boardSize; row++) {
            String first = board[row * boardSize];
            if (!first.isEmpty()) {
                boolean win = true;
                for (int col = 1; col < boardSize; col++) {
                    if (!board[row * boardSize + col].equals(first)) { win = false; break; }
                }
                if (win) return first;
            }
        }
        // Cols
        for (int col = 0; col < boardSize; col++) {
            String first = board[col];
            if (!first.isEmpty()) {
                boolean win = true;
                for (int row = 1; row < boardSize; row++) {
                    if (!board[row * boardSize + col].equals(first)) { win = false; break; }
                }
                if (win) return first;
            }
        }
        // Diagonals
        if (!board[0].isEmpty()) {
            boolean win = true;
            for (int i = 1; i < boardSize; i++) {
                if (!board[i * boardSize + i].equals(board[0])) { win = false; break; }
            }
            if (win) return board[0];
        }
        if (!board[boardSize - 1].isEmpty()) {
            boolean win = true;
            for (int i = 1; i < boardSize; i++) {
                if (!board[i * boardSize + (boardSize - 1 - i)].equals(board[boardSize - 1])) { win = false; break; }
            }
            if (win) return board[boardSize - 1];
        }
        return "";
    }
}