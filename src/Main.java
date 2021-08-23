import javax.swing.*;

public class Main {

    public static void main(String[] args)
    {
        Game gamePanel = new Game();
        GameWindow gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.add(gamePanel);
        gamePanel.startAnimation();

    }

}