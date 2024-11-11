import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private int x, y;
    private int dx;  // Horizontal movement speed
    private final int speed = 10;  // Movement speed
    private Image stickFigureImage;  // Image for the stick figure

    public Player(int x, int y) {
        this.x = x;
        this.y = 470;
        this.dx = 0;

        // Load and scale the stick figure image
        try {
            Image originalImage = ImageIO.read(new File("/Users/leila/Desktop/Fall2024/CSEN160/Lab/Lab2/Project/src/dfqe3x4-147e62f9-2be6-46a0-838c-3c818ec3fee6.png"));  
            stickFigureImage = originalImage.getScaledInstance(70, 100, Image.SCALE_SMOOTH);  // Scale to 70x100 pixels
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        // Draw the stick figure image instead of a rectangle
        if (stickFigureImage != null) {
            g.drawImage(stickFigureImage, x, y, null);
        } else {
            // Fallback in case image fails to load
            g.setColor(Color.BLUE);
            g.fillRect(x, y, 50, 50);  // Placeholder rectangle
        }
    }

    public void update() {
        x += dx;  // Update x position based on dx

        // Prevent the player from moving off the screen
        if (x < 0) {
            x = 0;
        } else if (x + 70 > 500) {  // Adjusted to match the character's width
            x = 500 - 70;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        // Set movement direction based on key press
        if (key == KeyEvent.VK_LEFT) {
            dx = -speed;
        } else if (key == KeyEvent.VK_RIGHT) {
            dx = speed;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        // Stop movement when the key is released
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
