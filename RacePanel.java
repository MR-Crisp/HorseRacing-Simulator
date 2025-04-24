import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RacePanel extends JPanel {

    private int lanes;
    private List<Horse> horses;

    public RacePanel(int lanes) {
        this.lanes = lanes;
        setPreferredSize(new Dimension(800, lanes * 100));  // Example size
    }

    public void updateHorses(List<Horse> horses) {
        this.horses = horses;
        repaint();  // Repaint when horses are updated
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the lanes
        for (int i = 0; i < lanes; i++) {
            g.setColor(Color.BLACK);
            g.fillRect(50, 100 * i + 10, getWidth() - 100, 80); // Draw lane
            g.setColor(Color.WHITE);
            g.drawRect(50, 100 * i + 10, getWidth() - 100, 80);
        }

        // Draw horses in each lane based on their distance
        if (horses != null) {
            for (int i = 0; i < horses.size(); i++) {
                Horse horse = horses.get(i);
                int laneY = 100 * i + 50;
                int distance = horse.getDistanceTravelled();
                g.setColor(Color.RED);  // For example, use red for horses
                g.fillOval(50 + distance * 10, laneY, 30, 30); // Horse position
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(horse.getSymbol()), 50 + distance * 10, laneY + 40);
            }
        }
    }
}
