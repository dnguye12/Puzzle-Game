package views;

import controllers.AnimationController;
import controllers.BoardController;
import models.BoardModel;
import models.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

// The BoardView class is responsible for rendering the game board and its cells, handling animations,
// and displaying the donut menu used for cell rotation.
public class BoardView extends JComponent {
    private final int GAP = 2; // Gap between the cells
    private final int PADDING = 50; // Padding around the board
    private BoardController con;
    private BoardModel model;
    private Image image;
    private boolean isShowingDonutMenu; // Flag to indicate if the donut menu is visible
    private int hoveredSection; // Indicates which section of the donut menu is being hovered
    private Point DonutMenuPos; // Position of the donut menu on the screen
    private int innerRadius; // Inner radius of the donut menu
    private int outerRadius; // Outer radius of the donut menu
    private final int ARROWLENGTH = 30; // Length of the arrows in the donut menu
    private final int ARROWHEAD = 20; // Size of the arrowheads in the donut menu
    private AnimationController animationController; // Reference to the animation controller

    public BoardView(BoardController con) {
        this.con = con;
        this.model = con.getModel();
        this.image = this.model.getImage();
        this.isShowingDonutMenu = false;
        this.hoveredSection = -1;
        this.handleSize();
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.drawBackground(g2d); // Draw the grid background

        ArrayList<Cell> cells = this.model.getCells();

        // Loop through the cells and draw them (skipping the cell that is rotating)
        for (Cell cell : cells) {
            if (cell.getIdx() != animationController.getRotatingIdx()) {
                this.drawCell(g2d, cell);
            }
        }

        // Highlight the cell selected via keyboard, if keyboard mode is active
        if(this.con.isKeyboardMode()) {
            this.drawKeyboardSelection(g2d, cells.get(0));
        }

        // If a cell is being dragged, render it separately
        if (model.getSelectedCell() != null) {
            Cell draggedCell = model.getDraggedCell();
            if (draggedCell != null) {
                this.drawDraggedCell(g2d, draggedCell);
            }
        }

        // Handle scaling and rotating animations
        int scalingIdx = this.animationController.getScalingIdx();
        int rotatingIdx = this.animationController.getRotatingIdx();
        if (scalingIdx != -1) {
            if (model.getDraggedCell() == null) {
                if (rotatingIdx != scalingIdx) {
                    this.drawScalingCell(g2d, cells.get(scalingIdx));
                } else {
                    this.drawRotatingCell(g2d, cells.get(rotatingIdx));
                }
            }
        }

        // Draw the donut menu if it is visible
        if (this.isShowingDonutMenu) {
            this.drawDonutMenu(g2d);
        }
    }

    // Adjust the size of the board view based on the puzzle image size and difficulty level
    private void handleSize() {
        int width = 420;
        int height = 420;
        Dimension helper = null;
        int difValue = this.model.getDifficulty().getValue() - 1;
        if (this.image != null) {
            helper = this.model.getImageSize();

            // Calculate the width and height of the board based on the image size and difficulty
            width = (int) (helper.getWidth() + 2 * PADDING + difValue * GAP);
            height = (int) (helper.getHeight() + 2 * PADDING + difValue * GAP);
        }

        if (helper != null) {
            int cellWidth = (int) (width / difValue); // Calculate cell width
            this.outerRadius = cellWidth ; // Set outer radius of the donut menu based on cell width
            if(this.outerRadius > 300) {
                this.outerRadius = 300; // Limit the maximum outer radius
            }
            this.innerRadius = this.outerRadius - 75;
        }
        this.setPreferredSize(new Dimension(width, height));
        this.revalidate();
        this.repaint();
    }

    // Draws the background grid lines for the board
    private void drawBackground(Graphics2D g2d) {
        int frameW = this.getWidth();
        int frameH = this.getHeight();
        g2d.setColor(Color.LIGHT_GRAY);

        for (int x = 0; x < frameW; x += 20) {
            g2d.drawLine(x, 0, x, frameH);
        }
        g2d.drawLine(frameW, 0, frameW, frameH);
        for (int y = 0; y < frameH; y += 20) {
            g2d.drawLine(0, y, frameW, y);
        }
        g2d.drawLine(0, frameH, frameW, frameH);
    }

    // Draw the cell highlighted by keyboard selection (keyboard mode)
    private void drawKeyboardSelection(Graphics2D g2d, Cell helper) {

        int width = helper.getImage().getWidth(null);
        int height = helper.getImage().getHeight(null);

        int row = con.getKeyboardRow();
        int col = con.getKeyboardCol();
        int posX = PADDING, posY = PADDING;

        // Calculate position of the selected cell
        if(row > 0) {
            posX = PADDING + row * width + row * GAP;
        }
        if(col > 0) {
            posY = PADDING + col * height + col * GAP;
        }

        g2d.setColor(new Color(0,0,255,50));
        g2d.fillRect(posX , posY , width, height);
    }

    // Draw a single cell, including its current rotation
    private void drawCell(Graphics2D g2d, Cell cell) {
        if (!cell.isSelected()) {

            Cell.Rotation rotation = cell.getRotation();
            AffineTransform oldTransform = g2d.getTransform();

            // Calculate rotation and position for the cell
            double angle = Math.toRadians(rotation.getAngle());
            int x = cell.getPos().x;
            int y = cell.getPos().y;
            int imageWidth = cell.getImage().getWidth(null);
            int imageHeight = cell.getImage().getHeight(null);
            int centerX = x + imageWidth / 2;
            int centerY = y + imageHeight / 2;

            // Apply rotation transformation
            AffineTransform transform = new AffineTransform();
            transform.rotate(angle, centerX, centerY);
            g2d.setTransform(transform);

            g2d.drawImage(cell.getImage(), x, y, null);
            g2d.setTransform(oldTransform);

            // If help mode is enabled, color the cell based on its correctness
            if(con.isShowingHelp()) {
                if(cell.getRotation() == Cell.Rotation.NORTH && cell.getImage() == this.model.getCorrectImages().get(cell.getIdx())) {
                    g2d.setColor(new Color(0,255,0,50));

                }else {
                    g2d.setColor(new Color(255,0,0,50));
                }
                g2d.fillRect(x, y, imageWidth, imageHeight);
            }
        }
    }

    // Draw the cell currently being dragged by the user
    private void drawDraggedCell(Graphics2D g2d, Cell cell) {
        AffineTransform old = g2d.getTransform();
        Image img = cell.getImage();
        Point pos = cell.getPos();
        double scaleFactor = this.animationController.getScalingFactor();
        Cell.Rotation rotation = cell.getRotation();
        double angle = Math.toRadians(rotation.getAngle());

        g2d.translate(pos.x + img.getWidth(null) / 2, pos.y + img.getHeight(null) / 2);
        g2d.rotate(angle);
        g2d.scale(scaleFactor, scaleFactor);
        g2d.translate(-img.getWidth(null) / 2, -img.getHeight(null) / 2);

        int shadowOffset = 5;
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillRect(-shadowOffset, -shadowOffset, img.getWidth(null) + shadowOffset * 2, img.getHeight(null) + shadowOffset * 2);

        g2d.drawImage(img, 0, 0, this);
        g2d.setTransform(old);
    }

    //Draw a cell being scaled based on its state of the animation
    private void drawScalingCell(Graphics2D g2d, Cell cell) {
        AffineTransform old = g2d.getTransform();
        Image img = cell.getImage();
        Point pos = cell.getPos();
        double scaleFactor = this.animationController.getScalingFactor();
        Cell.Rotation rotation = cell.getRotation();
        double angle = Math.toRadians(rotation.getAngle());

        g2d.translate(pos.x + img.getWidth(null) / 2, pos.y + img.getHeight(null) / 2);
        g2d.rotate(angle);
        g2d.scale(scaleFactor, scaleFactor);
        g2d.translate(-img.getWidth(null) / 2, -img.getHeight(null) / 2);

        int shadowOffset = 5;
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillRect(-shadowOffset, -shadowOffset, img.getWidth(null) + shadowOffset * 2, img.getHeight(null) + shadowOffset * 2);

        g2d.drawImage(img, 0, 0, this);
        g2d.setTransform(old);
    }

    //Draw a cell being rotated based on its state of the animation
    private void drawRotatingCell(Graphics2D g2d, Cell cell) {
        AffineTransform old = g2d.getTransform();
        Image img = cell.getImage();
        Point pos = cell.getPos();
        double scaleFactor = this.animationController.getScalingFactor();
        double angle = Math.toRadians(animationController.getRotatingFactor());

        g2d.translate(pos.x + img.getWidth(null) / 2, pos.y + img.getHeight(null) / 2);
        g2d.rotate(angle);
        g2d.scale(scaleFactor, scaleFactor);
        g2d.translate(-img.getWidth(null) / 2, -img.getHeight(null) / 2);

        int shadowOffset = 5;
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillRect(-shadowOffset, -shadowOffset, img.getWidth(null) + shadowOffset * 2, img.getHeight(null) + shadowOffset * 2);

        g2d.drawImage(img, 0, 0, this);
        g2d.setTransform(old);
    }

    //Rotate menu drawing
    private void drawDonutMenu(Graphics2D g2d) {
        for (int i = 0; i < 4; i++) {
            this.drawDonutSection(g2d, i);
        }
    }

    // Draw a single section of the donut menu
    private void drawDonutSection(Graphics2D g2d, int section) {
        double startAngle = 45 + 90 * section + 1; // Calculate the start angle for the section
        Arc2D.Double arc = new Arc2D.Double(DonutMenuPos.x - this.outerRadius / 2.0, DonutMenuPos.y - this.outerRadius / 2.0, this.outerRadius, this.outerRadius, startAngle, 88, Arc2D.PIE);
        Ellipse2D innerCircle = new Ellipse2D.Double(
                DonutMenuPos.x - innerRadius / 2.0,
                DonutMenuPos.y - innerRadius / 2.0,
                innerRadius, innerRadius);
        Area area = new Area(arc); // Create an area for the outer section
        area.subtract(new Area(innerCircle)); // Subtract the inner circle to create a donut shape

        // Highlight the section if it's being hovered
        if (section == hoveredSection) {
            g2d.setColor(new Color(255, 0, 0, 150)); // Red if hovered
        } else {
            g2d.setColor(new Color(0, 0, 255, 150)); // Blue if not hovered
        }
        g2d.fill(area); // Fill the donut section

        // Draw the rotation arrow for the section
        double angleRad = Math.toRadians(90 * section + 90 / 2.0 + 45);
        int endX = (int) (DonutMenuPos.x + (innerRadius / 2 + ARROWLENGTH) * Math.cos(angleRad));
        int endY = (int) (DonutMenuPos.y + (innerRadius / 2 + ARROWLENGTH) * Math.sin(angleRad));

        g2d.setColor(Color.BLACK);

        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(endX, endY);
        arrowHead.addPoint((int) (endX - ARROWHEAD * Math.cos(angleRad - Math.PI / 6)),
                (int) (endY - ARROWHEAD * Math.sin(angleRad - Math.PI / 6)));
        arrowHead.addPoint((int) (endX - ARROWHEAD * Math.cos(angleRad + Math.PI / 6)),
                (int) (endY - ARROWHEAD * Math.sin(angleRad + Math.PI / 6)));
        g2d.fill(arrowHead); // Draw the arrowhead
    }

    public boolean isShowingDonutMenu() {
        return this.isShowingDonutMenu;
    }

    public void setShowingDonutMenu(boolean showingDonutMenu) {
        this.isShowingDonutMenu = showingDonutMenu;
    }

    public void toggleShowingDonutMenu() {
        this.isShowingDonutMenu = !this.isShowingDonutMenu;
    }

    public void setDonutMenuPos(Point donutMenuPos) {
        DonutMenuPos = donutMenuPos;
    }

    public int getHoveredDonutSection(Point p) {
        double x = p.getX() - DonutMenuPos.x;
        double y = p.getY() - DonutMenuPos.y;
        double distance = Math.sqrt(x * x + y * y);

        // Check if the cursor is within the donut menu area
        if (distance < innerRadius / 2.0 || distance > outerRadius / 2.0) {
            return -1;
        }

        // Calculate which section of the donut menu is hovered based on the angle
        double angle = Math.toDegrees(Math.atan2(-y, x)) - 45;
        if (angle < 0) {
            angle += 360;
        }
        return (int) (angle / 90);
    }

    public int getHoveredSection() {
        return hoveredSection;
    }

    public void setHoveredSection(int hoveredSection) {
        this.hoveredSection = hoveredSection;
    }

    public void setAnimationController(AnimationController con) {
        this.animationController = con;
    }
}
