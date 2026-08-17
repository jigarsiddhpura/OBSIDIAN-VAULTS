package TicTacToe;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.initializeGame();
        System.out.println("Game winner: " + game.startGame());
    }
}
