import java.awt.*;
import java.util.Random;

public class EnemyBackup {
    private int x, y;
    private Color color;  // Color of the enemy
    private static final Color[] COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE};  // Array of possible colors

    public EnemyBackup(int x, int y) {
        this.x = x;
        this.y = y;
        
        // Assign a random color from the COLORS array
        Random rand = new Random();
        this.color = COLORS[rand.nextInt(COLORS.length)];
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, 30, 30);  // Draw enemy as a rectangle
    }

    public void update() {
        y += 2;  // Move downwards
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 30);
    }
}
