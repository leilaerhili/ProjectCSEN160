import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanelBackup extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Player player;
    private List<MusicNote> enemies;
    private List<Bullet> bullets;
    private int elapsedSeconds = 0;  // Track total time elapsed in seconds
    private boolean levelComplete = false;  // Flag to indicate level completion
    private int timeCounter = 0;  // Counts milliseconds

    public GamePanelBackup() {
        this.setFocusable(true);
        this.addKeyListener(this);
        this.timer = new Timer(30, this);

        player = new Player(400, 500);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    public void startGame() {
        timer.start();
        spawnEnemies();  // Initial wave of enemies
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (levelComplete) {
            // Display "Level Complete" message
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.GREEN);
            g.drawString("Level Complete!", 250, 300);
            return;
        }

        player.draw(g);

        for (MusicNote enemy : enemies) {
            enemy.draw(g);
        }

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    private void spawnEnemies() {
        // Create enemies at random positions
        for (int i = 0; i < 5; i++) {
            enemies.add(new MusicNote((int) (Math.random() * 800), 0));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (levelComplete) {
            // Stop game updates once level is complete
            return;
        }

        // Increment timeCounter by 30 ms each tick
        timeCounter += 30;
        if (timeCounter >= 1000) {
            // Every 1000 ms (1 second), increment elapsedSeconds
            elapsedSeconds++;
            timeCounter = 0; // Reset counter every second
        }

        // Every 20 seconds, spawn a new wave of enemies
        if (elapsedSeconds % 3 == 0 && timeCounter == 0 && elapsedSeconds > 0) {
            spawnEnemies();
        }

        // Stop spawning enemies and mark the level as complete after 3 minutes (180 seconds)
        if (elapsedSeconds >= 20) {
            levelComplete = true;
            timer.stop();  // Optionally stop the timer as well
        }

        player.update();

        // Update enemies
        for (MusicNote enemy : enemies) {
            enemy.update();
        }

        // Update bullets
        for (Bullet bullet : bullets) {
            bullet.update();
        }

        checkCollisions();  // Check for collisions between bullets and enemies

        repaint();
    }

    private void checkCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<MusicNote> enemiesToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            for (MusicNote enemy : enemies) {
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            bullets.add(new Bullet(player.getX(), player.getY()));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
