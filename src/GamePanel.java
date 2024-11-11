import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer gameTimer;
    private Timer spawnTimer;
    private Player player;
    private List<MusicNote> enemies;
    private List<Bullet> bullets;
    private boolean gameComplete = false;
    private boolean gameLost = false;
    
    // Player lives
    private int lives = 3;

    // Define the sequence of notes (enemies) to spawn
    private final String[] spawnSequence = {"E", "D", "C", "E", "D", "C", "C", "C", "C", "D", "D", "D", "D", "E", "D", "C"};
    private int spawnIndex = 0;  // Track which enemy to spawn next

    public GamePanel() {
        this.setFocusable(true);
        this.addKeyListener(this);

        // Set a light purple background color
        setBackground(new Color(230, 230, 250));

        // Timer for game updates (30 ms)
        this.gameTimer = new Timer(30, this);
        
        // Timer for enemy spawning every 1.5 seconds (1500 ms)
        this.spawnTimer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnNextEnemy();
            }
        });

        player = new Player(400, 500);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    public void startGame() {
        gameTimer.start();
        spawnTimer.start();  // Start spawning enemies every 1.5 seconds
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameComplete) {
            // Display "You Won" or "You Lost" message
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.GREEN);
            if (gameLost) {
                g.drawString("You Lost", 250, 300);
            } else {
                g.drawString("You Won!", 250, 300);
            }
            return;
        }

        // Draw the floor at the bottom of the screen
        g.setColor(Color.BLACK);
        g.fillRect(0, getHeight() - 10, getWidth(), 10);  // 10 pixels high black floor

        player.draw(g);

        // Draw lives at the top-right corner
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.RED);
        g.drawString("Lives: " + lives, getWidth() - 100, 30);

        for (MusicNote enemy : enemies) {
            enemy.draw(g);
        }

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    private void spawnNextEnemy() {
        if (spawnIndex < spawnSequence.length) {
            // Spawn the next enemy in the sequence
            String note = spawnSequence[spawnIndex];
            
            // Calculate a random x position within the screen width
            int panelWidth = getWidth();
            int enemyWidth = 30;  // Assuming each enemy is 30 pixels wide
            int x = (int) (Math.random() * (panelWidth - (enemyWidth+10)));  // Ensures x is within screen bounds
            
            enemies.add(new MusicNote(note, x, 0));  // Spawn at calculated x position at the top
            spawnIndex++;
        } else {
            // Stop the spawn timer if all enemies in the sequence have been spawned
            spawnTimer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameComplete) {
            // Stop game updates once game is complete
            return;
        }

        player.update();

        // Update enemies and check if any reach the bottom
        List<MusicNote> enemiesToRemove = new ArrayList<>();
        for (MusicNote enemy : enemies) {
            enemy.update();
            if (enemy.getY() > getHeight()) {  // Check if the enemy reaches the bottom
                enemiesToRemove.add(enemy);
                decreaseLives();  // Decrease lives when an enemy reaches the bottom
            }
        }
        enemies.removeAll(enemiesToRemove);

        // Update bullets
        for (Bullet bullet : bullets) {
            bullet.update();
        }

        checkCollisions();  // Check for collisions between bullets and enemies

        // Check if the player won or lost the game
        if (lives <= 0) {
            gameComplete = true;
            gameLost = true;
            gameTimer.stop();
            spawnTimer.stop();
        } else if (enemies.isEmpty() && spawnIndex >= spawnSequence.length) {
            gameComplete = true;
            gameLost = false;
            gameTimer.stop();
        }

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

                    // Play sound based on enemy note
                    enemy.playSound();
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);
    }

    private void decreaseLives() {
        lives--;
        if (lives <= 0) {
            gameComplete = true;
            gameLost = true;
            gameTimer.stop();
            spawnTimer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            int bulletStartX = player.getX() + 35;  // Center horizontally
            int bulletStartY = player.getY();       // Top edge of character
            bullets.add(new Bullet(bulletStartX, bulletStartY));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
