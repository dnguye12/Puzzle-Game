package models;

import java.awt.*;
import java.util.Random;

// Represents a single cell in the puzzle, including its position, rotation, and image
public class Cell {
    // Enum to define the possible rotation angles for the cell
    public enum Rotation {
        NORTH(0),
        EAST(90),
        SOUTH(180),
        WEST(270);

        private int angle; // The angle associated with the rotation (in degree)

        Rotation(int angle) {
            this.angle = angle;
        }

        public int getAngle() {
            return this.angle;
        }

        // Method to randomly pick a rotation value
        public static Rotation getRandom() {
            Rotation[] helper = Rotation.values();
            int pick = new Random().nextInt(helper.length);
            return helper[pick];
        }

        // Method to get the next rotation in the clockwise direction
        public Rotation next() {
            switch (this) {
                case NORTH -> {
                    return EAST;
                }
                case EAST -> {
                    return SOUTH;
                }
                case SOUTH -> {
                    return WEST;
                }
                case WEST -> {
                    return NORTH;
                }
            }
            return this;
        }

        // Method to get the previous rotation in the counterclockwise direction
        public Rotation back() {
            switch (this) {
                case NORTH -> {
                    return WEST;
                }
                case EAST -> {
                    return NORTH;
                }
                case SOUTH -> {
                    return EAST;
                }
                case WEST -> {
                    return SOUTH;
                }
            }
            return this;
        }

        // Method to convert an integer (0-3) to the corresponding Rotation
        public static Rotation intToRotation(int n) {
            switch(n) {
                case 0 -> {
                    return NORTH;
                }
                case 1 -> {
                    return WEST;
                }
                case 2 -> {
                    return SOUTH;
                }
                case 3 -> {
                    return EAST;
                }
            }
            return null;
        }
    }

    private int idx; // Index of the cell in the grid
    private Point pos; // Position of the cell on the board
    private Rotation rotation; // Current rotation of the cell
    private Image image;
    private boolean isSelected; // Flag to indicate if the cell is selected
    private boolean isDragged; // Flag to indicate if the cell is being dragged

    public Cell(int idx, Point pos, Image image) {
        this.idx = idx;
        this.pos = pos;
        this.rotation = Rotation.NORTH;
        this.image = image;
        this.isSelected = false;
        this.isDragged = false;
    }

    // Deep copy constructor to create a new cell as a copy of another cell
    public Cell(Cell other) {
        this.idx = other.idx;
        this.pos = other.pos;
        this.rotation = other.rotation;
        this.image = other.image;
        this.isSelected = false;
        this.isDragged = false;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public Point getPos() {
        return this.pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public void setRotation(Rotation r) {
        this.rotation = r;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isDragged() {
        return isDragged;
    }

    public void setDragged(boolean draggeed) {
        isDragged = draggeed;
    }

    public void moveCell(Point step) {
        this.pos.x += step.x;
        this.pos.y += step.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Cell other = (Cell) obj;
        return this.idx == other.idx && this.pos == other.pos && this.image == other.image;
    }
}
