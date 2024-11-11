import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Enemy2Backeupsong {
    private int x, y;
    private Color color;
    private String note;  // Represents the note (E, D, or C)

    public Enemy2Backeupsong(String note, int x, int y) {
        this.note = note;
        this.x = x;
        this.y = y;

        // Assign color based on note
        switch (note) {
            case "E":
                this.color = Color.RED;
                break;
            case "D":
                this.color = Color.GREEN;
                break;
            case "C":
                this.color = Color.BLUE;
                break;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, 30, 30);  // Simple rectangle as enemy
    }

    public void update() {
        y += 2;  // Move downwards
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 30);
    }

    public String getNote() {
        return note;
    }

    // Method to play a sound based on the note
    public void playSound() {
        String soundFile = "";

        // Determine the sound file based on note
        switch (note) {
            case "E":
                soundFile = "/Users/leila/Desktop/Fall2024/CSEN160/Lab/Lab2/Project/src/E.wav";
                break;
            case "D":
                soundFile = "/Users/leila/Desktop/Fall2024/CSEN160/Lab/Lab2/Project/src/D.wav";
                break;
            case "C":
                soundFile = "/Users/leila/Desktop/Fall2024/CSEN160/Lab/Lab2/Project/src/C.wav";
                break;
        }

        // Play the sound
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
