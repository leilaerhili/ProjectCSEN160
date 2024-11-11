import java.awt.*;

public class Bullet {
    private int x, y;
    
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 5, 10);  // Simple rectangle as bullet
    }
    
    public void update() {
        y -= 25;  // Move upwards
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, 5, 10);
    }
}
