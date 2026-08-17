package TicTacToe.Model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public int size;
    public PlayingPiece[][] board;

    public Board(int size) {
        this.size = size;
        this.board = new PlayingPiece[size][size];
    }

    public boolean addPiece(int x, int y, PlayingPiece piece) {
        if (board[x][y] == null) {
            board[x][y] = piece;
            return true;
        }
        return false;
    }

    public List<Cell> getFreeCells() {
        List<Cell> freeCells = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    freeCells.add(new Cell(i, j));
                }
            }
        }
        return freeCells;
    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + board[i][j].pieceType + " ");
                }
                System.out.print("|");
            }
            System.out.println();
        }
    }

}
