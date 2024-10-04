import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class DonutPanel extends JPanel {

    private int hoveredSection = -1; // Stores the index of the hovered section
    private final int donutX = 50, donutY = 50, outerRadius = 200, innerRadius = 100;

    public DonutPanel() {
        setPreferredSize(new Dimension(300, 300));
        setOpaque(false); // Make the panel non-opaque to handle transparency

        // Add a mouse motion listener to detect hovering
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Check which section the mouse is over
                Point p = e.getPoint();
                hoveredSection = getHoveredSection(p);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Do not call super.paintComponent(g) to prevent clearing the background
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // If you have a background image, draw it here
        // Image backgroundImage = ...;
        // g2d.drawImage(backgroundImage, 0, 0, null);

        // Draw the four arcs representing the donut
        for (int i = 0; i < 4; i++) {
            drawDonutSection(g2d, i);
        }
    }

    private void drawDonutSection(Graphics2D g2d, int section) {
        // Define the angle for each section
        double startAngle = 90 * section;
        Arc2D.Double arc = new Arc2D.Double(donutX, donutY, outerRadius, outerRadius, startAngle, 90, Arc2D.PIE);

        // Create a new inner circle for the "hole" in the donut
        Ellipse2D innerCircle = new Ellipse2D.Double(
                donutX + outerRadius / 2 - innerRadius / 2,
                donutY + outerRadius / 2 - innerRadius / 2,
                innerRadius, innerRadius);

        // Create an Area from the arc
        Area area = new Area(arc);

        // Subtract the inner circle from the arc area to create the donut shape
        area.subtract(new Area(innerCircle));

        // Set the color based on whether this section is hovered
        if (section == hoveredSection) {
            g2d.setColor(new Color(255, 0, 0, 150));  // Highlight color with transparency
        } else {
            g2d.setColor(new Color(0, 0, 255, 150));  // Normal color with transparency
        }

        // Fill the area
        g2d.fill(area);
    }

    // Check which section is hovered based on the mouse coordinates
    private int getHoveredSection(Point p) {
        double x = p.getX() - (donutX + outerRadius / 2);
        double y = p.getY() - (donutY + outerRadius / 2);
        double distance = Math.sqrt(x * x + y * y);

        // Check if the point is within the donut's area
        if (distance < innerRadius / 2 || distance > outerRadius / 2) {
            return -1; // Not hovering over the donut
        }

        // Calculate the angle of the point relative to the center
        double angle = Math.toDegrees(Math.atan2(-y, x)) - 45;
        if (angle < 0) {
            angle += 360;
        }

        // Determine which section is hovered based on the angle
        return (int) (angle / 90);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Donut Shape Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DonutPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
