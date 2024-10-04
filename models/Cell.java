package models;

import java.awt.*;
import java.util.Random;

public class Cell {
    public enum Rotation {
        NORTH(0),
        EAST(90),
        SOUTH(180),
        WEST(270);

        private int angle;

        Rotation(int angle) {
            this.angle = angle;
        }

        public int getAngle() {
            return this.angle;
        }

        public static Rotation getRandom() {
            Rotation[] helper = Rotation.values();
            int pick = new Random().nextInt(helper.length);
            return helper[pick];
        }

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

    private int idx;
    private Point pos;
    private Rotation rotation;
    private Image image;
    private boolean isSelected;
    private boolean isDragged;

    public Cell(int idx, Point pos, Image image) {
        this.idx = idx;
        this.pos = pos;
        this.rotation = Rotation.NORTH;
        this.image = image;
        this.isSelected = false;
        this.isDragged = false;
    }

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
