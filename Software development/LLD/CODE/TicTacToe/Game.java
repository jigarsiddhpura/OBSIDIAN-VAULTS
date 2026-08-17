package TicTacToe;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import TicTacToe.Model.Board;
import TicTacToe.Model.Cell;
import TicTacToe.Model.Player;
import TicTacToe.Model.PlayingPiece;
import TicTacToe.Model.PlayingPieceO;
import TicTacToe.Model.PlayingPieceX;

public class Game {
    Board gameBoard;
    Deque<Player> players;
    
    public void initializeGame() {
        players = new LinkedList<>();

        PlayingPieceX pieceX = new PlayingPieceX();
        PlayingPieceO pieceO = new PlayingPieceO();

        Player player1 = new Player("Player 1", pieceX);
        Player player2 = new Player("Player 2", pieceO);

        players.add(player1);
        players.add(player2);

        gameBoard = new Board(3);
    }

    public String startGame() {
        boolean noWinner = true;
        while(noWinner) {
            Player playerTurn = players.pollFirst();
            
            gameBoard.printBoard();
            List<Cell> freeCells = gameBoard.getFreeCells();
            if (freeCells.isEmpty()) {
                noWinner = false;
                continue;
            }

            System.out.println(playerTurn.name + " turn. Please enter the coordinates of the cell to place your piece: ");
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            // sc.close();
            String[] coordinates = s.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);

            boolean playerAddedSuccessfully = gameBoard.addPiece(x, y, playerTurn.playingPiece);

            if (!playerAddedSuccessfully) {
                System.out.println("Cell is already occupied. Please try again.");
                players.addFirst(playerTurn);
                continue;
            }

            players.addLast(playerTurn);

            boolean winner = isThereWinner(x, y, playerTurn.playingPiece);
            if (winner) {
                return playerTurn.name;
            }
        }
        return "tie";
    }

    public boolean isThereWinner(int x, int y, PlayingPiece piece) {
        boolean rowMatch = true;
        boolean columnMatch = true;
        boolean diagonalMatch = true;
        boolean antiDiagonalMatch = true;

        // check row
        for(int i = 0; i < gameBoard.size; i++) {
            if (gameBoard.board[x][i] == null || gameBoard.board[x][i].pieceType != piece.pieceType) {
                rowMatch = false;
            }
        }

        // check column
        for(int i = 0; i < gameBoard.size; i++) {
            if (gameBoard.board[i][y] == null || gameBoard.board[i][y].pieceType != piece.pieceType) {
                columnMatch = false;
            }
        }

        // check diagonal
        for(int i = 0; i < gameBoard.size; i++) {
            if (gameBoard.board[i][i] == null || gameBoard.board[i][i].pieceType != piece.pieceType) {
                diagonalMatch = false;
            }
        }

        // check anti-diagonal
        for(int i = 0; i < gameBoard.size; i++) {
            if (gameBoard.board[i][gameBoard.size - i - 1] == null || gameBoard.board[i][gameBoard.size - i - 1].pieceType != piece.pieceType) {
                antiDiagonalMatch = false;
            }
        }
        return rowMatch || columnMatch || diagonalMatch || antiDiagonalMatch;
    }

}
