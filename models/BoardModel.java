package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class BoardModel {
    private final int PADDING = 20;
    private final int GAP = 2;
    private Cell selectedCell;
    private Cell draggedCell;

    public enum Difficulty {
        EASY(5), MEDIUM(7), HARD(9);

        private int value;

        Difficulty(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    ; //5x5, 7x7, 9x9
    private Image image;
    private ArrayList<Cell> cells;
    private ArrayList<Image> correctImages;
    private Difficulty difficulty;

    public BoardModel(String path) {
        if (!Objects.equals(path, "")) {
            try {
                this.image = ImageIO.read(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.difficulty = Difficulty.MEDIUM;
        this.initCells();
    }

    public Image getImage() {
        return this.image;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public ArrayList<Cell> getCells() {
        return this.cells;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setSelectedCell(Cell selectedCell) {
        this.selectedCell = selectedCell;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void setDraggedCell(Cell draggedCell) {
        this.draggedCell = draggedCell;
    }

    public Cell getDraggedCell() {
        return draggedCell;
    }

    public Dimension getImageSize() {
        if (this.image != null) {
            return new Dimension(this.image.getWidth(null), this.image.getHeight(null));
        } else {
            return new Dimension(420, 420);
        }
    }

    private void initCells() {
        this.cells = new ArrayList<>();
        this.correctImages = new ArrayList<>();

        ArrayList<Image> helperImage = new ArrayList<>();
        ArrayList<Point> helperPoint = new ArrayList<>();

        Dimension imageSize = this.getImageSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize = new Dimension(screenSize.width - PADDING * 4, screenSize.height - PADDING * 7);
        double ratioImage = 1.0 * imageSize.width / imageSize.height;
        if(imageSize.width > screenSize.width) {
            this.image = this.image.getScaledInstance(screenSize.width, (int) (screenSize.width / ratioImage), Image.SCALE_SMOOTH);
            imageSize = this.getImageSize();
        }
        if(imageSize.height > screenSize.height) {
            ratioImage = 1.0 * imageSize.width / imageSize.height;
            this.image = this.image.getScaledInstance((int) (screenSize.height * ratioImage), screenSize.height, Image.SCALE_SMOOTH);
            imageSize = this.getImageSize();
        }
        ratioImage = 1.0 * imageSize.width / imageSize.height;

        int cellWidthCount = this.difficulty.getValue();
        int cellHeightCount = (int) (cellWidthCount / ratioImage);
        int cellWidth = imageSize.width / cellWidthCount;
        int cellHeight = imageSize.height / cellHeightCount;
        if(cellWidth > cellHeight) {
            cellWidth = cellHeight;
        }else if(cellHeight > cellWidth) {
            cellHeight = cellWidth;
        }

        this.image = this.image.getScaledInstance(cellWidth * cellWidthCount, cellHeight * cellHeightCount, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = this.imageToBufferedImage(this.image);

        imageSize = this.getImageSize();
        int i = 0;
        int j = 0;
        for (int x = 0; x < imageSize.width; x += cellWidth) {
            for (int y = 0; y < imageSize.height; y += cellHeight) {
                Image helper = bufferedImage.getSubimage(x, y, cellWidth, cellHeight);
                Point point = new Point(x + i * GAP + PADDING, y + j * GAP + PADDING);
                helperImage.add(helper);
                correctImages.add(helper);
                helperPoint.add(point);
                j++;
            }
            i++;
            j = 0;
        }
        Collections.shuffle(helperImage);

        for (i = 0; i < helperImage.size(); i++) {
            Cell cell = new Cell(i, helperPoint.get(i), helperImage.get(i));
            cell.setRotation(Cell.Rotation.getRandom());
            this.cells.add(cell);
        }
    }

    private BufferedImage imageToBufferedImage(Image img) {
        BufferedImage bufferedImage = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return bufferedImage;
    }

    public boolean endGame() {
        for (int i = 0; i < this.cells.size(); i++) {
            if (this.cells.get(i).getImage() != this.correctImages.get(i)) {
                return false;
            }
        }
        return true;
    }

    public Cell clickOnCell(Point point) {
        int x1, y1, x2, y2;
        Point pos;
        Image img;
        for (Cell cell : this.cells) {
            pos = cell.getPos();
            x1 = pos.x;
            y1 = pos.y;
            img = cell.getImage();
            x2 = x1 + img.getWidth(null);
            y2 = y1 + img.getHeight(null);
            if (x1 <= point.x && point.x <= x2 && y1 <= point.y && point.y <= y2) {
                return cell;
            }
        }
        return null;
    }

    public void replaceCell(Cell current, Cell other) {
        int currentIdx = current.getIdx();
        int otherIdx = other.getIdx();
        other.setIdx(currentIdx);
        current.setIdx(otherIdx);
        this.cells.set(currentIdx, other);
        this.cells.set(otherIdx, current);
    }
}
