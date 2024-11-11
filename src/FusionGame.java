import javax.swing.*;

public class FusionGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Guitar Shooter");
        GamePanel gamePanel = new GamePanel();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.add(gamePanel);
        frame.setVisible(true);
        
        gamePanel.startGame();
    }
}
