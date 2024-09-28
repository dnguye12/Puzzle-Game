package views;

import models.BoardModel;
import models.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardView extends JComponent {
    private final int GAP = 2;
    private final int PADDING = 20;
    private BoardModel model;
    private Image image;

    public BoardView(BoardModel model) {
        this.model = model;
        this.image = this.model.getImage();

        this.setFocusable(true);
        this.requestFocusInWindow();

        this.handleSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        int frameW = this.getWidth();
        int frameH = this.getHeight();

        ArrayList<Cell> cells = this.model.getCells();
        for (Cell cell : cells) {
            this.drawCell(g2d, cell);
        }

        if (model.getSelectedCell() != null) {
            Cell draggedCell = model.getDraggedCell();
            this.drawCell(g2d, draggedCell);
        }
    }

    private void handleSize() {
        int width = 420;
        int height = 420;
        if (this.image != null) {
            Dimension helper = this.model.getImageSize();

            int difValue = this.model.getDifficulty().getValue() - 1;
            width = (int) (helper.getWidth() + 2 * PADDING + difValue * GAP);
            height = (int) (helper.getHeight() + 2 * PADDING + difValue * GAP);
        }
        this.setPreferredSize(new Dimension(width, height));
    }

    private void drawImage(Graphics2D g2d, int frameW, int frameH) {
        Dimension d = this.model.getImageSize();
        int helperX = (int) ((frameW - d.getWidth()) / 2);
        int helperY = (int) ((frameH - d.getHeight()) / 2);
        Image image = this.model.getImage();

        if (image != null) {
            g2d.drawImage(image, helperX, helperY, this);
        } else {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(helperX, helperY, (int) d.getWidth(), (int) d.getHeight());
        }
    }

    private void drawCell(Graphics2D g2d, Cell cell) {
        if (cell.isSelected()) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(cell.getPos().x, cell.getPos().y, cell.getImage().getWidth(null), cell.getImage().getHeight(null));
        } else {
            g2d.drawImage(cell.getImage(), cell.getPos().x, cell.getPos().y, null);
        }
    }
}
