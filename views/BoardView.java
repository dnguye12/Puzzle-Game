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

public class BoardView extends JComponent {
    private final int GAP = 2;
    private final int PADDING = 50;
    private BoardController con;
    private BoardModel model;
    private Image image;
    private boolean isShowingDonutMenu;
    private int hoveredSection;
    private Point DonutMenuPos;
    private int innerRadius;
    private int outerRadius;
    private final int ARROWLENGTH = 30;
    private final int ARROWHEAD = 20;
    private AnimationController animationController;

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
        this.drawBackground(g2d);

        ArrayList<Cell> cells = this.model.getCells();

        for (Cell cell : cells) {
            if (cell.getIdx() != animationController.getRotatingIdx()) {
                this.drawCell(g2d, cell);
            }
        }

        if(this.con.isKeyboardMode()) {
            this.drawKeyboardSelection(g2d, cells.get(0));
        }

        if (model.getSelectedCell() != null) {
            Cell draggedCell = model.getDraggedCell();
            if (draggedCell != null) {
                this.drawDraggedCell(g2d, draggedCell);
            }
        }

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

        if (this.isShowingDonutMenu) {
            this.drawDonutMenu(g2d);
        }
    }

    private void handleSize() {
        int width = 420;
        int height = 420;
        Dimension helper = null;
        int difValue = this.model.getDifficulty().getValue() - 1;
        if (this.image != null) {
            helper = this.model.getImageSize();

            width = (int) (helper.getWidth() + 2 * PADDING + difValue * GAP);
            height = (int) (helper.getHeight() + 2 * PADDING + difValue * GAP);
        }

        if (helper != null) {
            int cellWidth = (int) (helper.getWidth() / difValue);
            this.innerRadius = cellWidth - 20;
            this.outerRadius = this.innerRadius + 80;
        }
        this.setPreferredSize(new Dimension(width, height));
        this.revalidate();
        this.repaint();
    }

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

    private void drawKeyboardSelection(Graphics2D g2d, Cell helper) {

        int width = helper.getImage().getWidth(null);
        int height = helper.getImage().getHeight(null);

        int row = con.getKeyboardRow();
        int col = con.getKeyboardCol();
        int posX = PADDING, posY = PADDING;
        if(row > 0) {
            posX = PADDING + row * width + row * GAP;
        }
        if(col > 0) {
            posY = PADDING + col * height + col * GAP;
        }

        g2d.setColor(new Color(255,0,0,50));
        g2d.fillRect(posX , posY , width, height);
    }

    private void drawCell(Graphics2D g2d, Cell cell) {
        if (!cell.isSelected()) {

            Cell.Rotation rotation = cell.getRotation();
            AffineTransform oldTransform = g2d.getTransform();

            double angle = Math.toRadians(rotation.getAngle());
            int x = cell.getPos().x;
            int y = cell.getPos().y;
            int imageWidth = cell.getImage().getWidth(null);
            int imageHeight = cell.getImage().getHeight(null);
            int centerX = x + imageWidth / 2;
            int centerY = y + imageHeight / 2;

            AffineTransform transform = new AffineTransform();
            transform.rotate(angle, centerX, centerY);
            g2d.setTransform(transform);

            g2d.drawImage(cell.getImage(), x, y, null);
            g2d.setTransform(oldTransform);
        }
    }

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

    private void drawDonutMenu(Graphics2D g2d) {
        for (int i = 0; i < 4; i++) {
            this.drawDonutSection(g2d, i);
        }
    }

    private void drawDonutSection(Graphics2D g2d, int section) {
        double startAngle = 45 + 90 * section + 1;
        Arc2D.Double arc = new Arc2D.Double(DonutMenuPos.x - this.outerRadius / 2.0, DonutMenuPos.y - this.outerRadius / 2.0, this.outerRadius, this.outerRadius, startAngle, 88, Arc2D.PIE);
        Ellipse2D innerCircle = new Ellipse2D.Double(
                DonutMenuPos.x - innerRadius / 2.0,
                DonutMenuPos.y - innerRadius / 2.0,
                innerRadius, innerRadius);
        Area area = new Area(arc);
        area.subtract(new Area(innerCircle));

        if (section == hoveredSection) {
            g2d.setColor(new Color(255, 0, 0, 150));
        } else {
            g2d.setColor(new Color(0, 0, 255, 150));
        }
        g2d.fill(area);

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
        g2d.fill(arrowHead);
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

        if (distance < innerRadius / 2.0 || distance > outerRadius / 2.0) {
            return -1;
        }
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
