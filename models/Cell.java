package models;

import java.awt.*;

public class Cell {
    private int idx;
    private Point pos;
    private Image image;
    private boolean isSelected;

    public Cell(int idx, Point pos, Image image) {
        this.idx = idx;
        this.pos = pos;
        this.image = image;
        this.isSelected = false;
    }

    public Cell(Cell other) {
        this.idx = other.idx;
        this.pos = other.pos;
        this.image = other.image;
        this.isSelected = false;
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
