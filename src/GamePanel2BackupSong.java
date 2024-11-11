import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel2BackupSong extends JPanel implements ActionListener, KeyListener {
    private Timer gameTimer;
    private Timer spawnTimer;
    private Player player;
    private List<MusicNote> enemies;
    private List<Bullet> bullets;
    private boolean gameComplete = false;  // Flag to indicate game completion
    
    // Define the sequence of notes (enemies) to spawn
    private final String[] spawnSequence = {"E", "D", "C", "E", "D", "C", "C", "C", "C", "D", "D", "D", "D", "E", "D", "C"};
    private int spawnIndex = 0;  // Track which enemy to spawn next

    public GamePanel2BackupSong() {
        this.setFocusable(true);
        this.addKeyListener(this);

        // Timer for game updates (30 ms)
        this.gameTimer = new Timer(30, this);
        
        // Timer for enemy spawning every 3 seconds (3000 ms)
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
        spawnTimer.start();  // Start spawning enemies every 3 seconds
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameComplete) {
            // Display "Game Over" message
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

    private void spawnNextEnemy() {
        if (spawnIndex < spawnSequence.length) {
            // Spawn the next enemy in the sequence
            String note = spawnSequence[spawnIndex];
            enemies.add(new MusicNote(note, (int) (Math.random() * 800), 0));
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

        // Update enemies
        for (MusicNote enemy : enemies) {
            enemy.update();
        }

        // Update bullets
        for (Bullet bullet : bullets) {
            bullet.update();
        }

        checkCollisions();  // Check for collisions between bullets and enemies

        // End game if all enemies are destroyed after sequence is completed
        if (enemies.isEmpty() && spawnIndex >= spawnSequence.length) {
            gameComplete = true;
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
