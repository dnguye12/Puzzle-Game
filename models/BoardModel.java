package models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

// Model class that represents the game board and manages the cells, images, and difficulty level
public class BoardModel {
    private final int PADDING = 50; // Padding around the game board
    private final int GAP = 2; // Gap between the cells
    private Cell selectedCell; // Currently selected cell
    private Cell draggedCell; // Currently dragged cell
    private Image image; // Original image for the puzzle
    private ArrayList<Cell> cells; // List of cells representing the puzzle pieces
    private ArrayList<Image> correctImages; // List of images in the correct order
    private PopupFormModel.Difficulty difficulty;  // Difficulty level of the game
    private int cellWidthCount; // Number of cells horizontally
    private int cellHeightCount; // Number of cells vertically

    public BoardModel(String path, PopupFormModel.Difficulty difficulty) {
        if (!Objects.equals(path, "")) {
            try {
                this.image = ImageIO.read(new File(path)); // Load image from file path
            } catch (IOException e) {
                e.printStackTrace(); // Print the error if the image can't be loaded
            }
        }
        this.difficulty = difficulty;
        this.initCells(); // Initialize the cells for the game
    }

    public Image getImage() {
        return this.image;
    }

    public ArrayList<Cell> getCells() {
        return this.cells;
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

    public int getCellWidthCount() {
        return cellWidthCount;
    }

    public int getCellHeightCount() {
        return cellHeightCount;
    }

    public ArrayList<Image> getCorrectImages() {
        return correctImages;
    }

    public Dimension getImageSize() {
        if (this.image != null) {
            return new Dimension(this.image.getWidth(null), this.image.getHeight(null));
        } else {
            return new Dimension(420, 420); // Return default size if no image is loaded
        }
    }

    public PopupFormModel.Difficulty getDifficulty() {
        return difficulty;
    }

    // Initialize the cells for the game board by splitting the image into pieces
    private void initCells() {
        this.cells = new ArrayList<>();
        this.correctImages = new ArrayList<>();

        ArrayList<Image> helperImage = new ArrayList<>(); // List of shuffled cell images
        ArrayList<Point> helperPoint = new ArrayList<>(); // List of positions for the cells

        // Get image size and adjust for screen size and aspect ratio
        Dimension imageSize = this.getImageSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //screensize
        screenSize = new Dimension(screenSize.width - PADDING * 2, screenSize.height - PADDING * 2); //adjust for padding
        double ratioImage = 1.0 * imageSize.width / imageSize.height;

        // Scale the image if it's too wide for the screen
        if(imageSize.width > screenSize.width) {
            this.image = this.image.getScaledInstance(screenSize.width - 500, (int) ((screenSize.width - 500) / ratioImage), Image.SCALE_SMOOTH);
            imageSize = this.getImageSize();
        }
        if(imageSize.height > screenSize.height) {
            ratioImage = 1.0 * imageSize.width / imageSize.height;
            this.image = this.image.getScaledInstance((int) ((screenSize.height - 500) * ratioImage), screenSize.height - 500, Image.SCALE_SMOOTH);
            imageSize = this.getImageSize();
        }
        ratioImage = 1.0 * imageSize.width / imageSize.height;

        // Calculate the number of cells based on the difficulty
        this.cellWidthCount = this.difficulty.getValue();
        this.cellHeightCount = (int) (this.cellWidthCount / ratioImage);

        // Determine the width and height of each cell
        int cellWidth, cellHeight;
        if(this.cellWidthCount > 0) {
            cellWidth = imageSize.width / this.cellWidthCount;
        }else {
            cellWidth = imageSize.width;
        }
        if(this.cellHeightCount > 0) {
            cellHeight = imageSize.height / this.cellHeightCount;
        }else {
            cellHeight = imageSize.height;
        }

        // Ensure cells are square by adjusting cell dimensions if necessary
        if(cellWidth > cellHeight) {
            cellWidth = cellHeight;
        }else if(cellHeight > cellWidth) {
            cellHeight = cellWidth;
        }

        // Scale the image to match the total size of the grid of cells
        int scaledImageWidth, scaledImageHeight;
        if(this.cellWidthCount > 0) {
            scaledImageWidth = cellWidth * this.cellWidthCount;
        }else {
            scaledImageWidth = cellWidth;
        }
        if(this.cellHeightCount > 0) {
            scaledImageHeight = cellHeight * this.cellHeightCount;
        }else {
            scaledImageHeight = cellHeight;
        }

        // Convert the image to a BufferedImage to allow subimage extraction
        this.image = this.image.getScaledInstance(scaledImageWidth, scaledImageHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = this.imageToBufferedImage(this.image);

        // Split the image into individual cells and store their positions and images
        imageSize = this.getImageSize();
        int i = 0;
        int j = 0;
        for (int x = 0; x < imageSize.width; x += cellWidth) {
            for (int y = 0; y < imageSize.height; y += cellHeight) {
                Image helper = bufferedImage.getSubimage(x, y, cellWidth, cellHeight); // Extract subimage
                Point point = new Point(x + i * GAP + PADDING, y + j * GAP + PADDING); // Calculate the position
                helperImage.add(helper); // Add the image to the shuffled list
                correctImages.add(helper); // Add the image to the correct order list
                helperPoint.add(point); // Add the position to the list
                j++;
            }
            i++;
            j = 0;
        }
        // Shuffle the images to randomize the puzzle
        Collections.shuffle(helperImage);

        // Create cell objects with their image, position, and random rotation
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

    // Check if the puzzle has been solved (all cells in the correct position and rotation)
    public boolean endGame() {
        Cell helper;
        for (int i = 0; i < this.cells.size(); i++) {
            helper = this.cells.get(i);
            if (helper.getRotation() != Cell.Rotation.NORTH || helper.getImage() != this.correctImages.get(i)) {
                return false;
            }
        }
        return true;
    }

    // Check if a cell has been clicked, based on the point of the mouse click
    public Cell clickOnCell(Point point) {
        int x1, y1, x2, y2;
        Point pos;
        Image img;
        // Loop through all cells and check if the clicked point is inside any cell's boundaries
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
