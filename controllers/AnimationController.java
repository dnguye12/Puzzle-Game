package controllers;

import models.Cell;
import views.BoardView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AnimationController {
    private BoardView view;
    private final int FRAME_LENGTH = 1000 / 60; //60fps
    private final int ANIMATION_DURATION = 300; // 300ms
    private int frameCount = 0;
    private final int TOTAL_FRAMES = ANIMATION_DURATION / FRAME_LENGTH;
    private final double MAX_SCALE = 1.1;
    private final double SCALE_STEP = (MAX_SCALE - 1) / TOTAL_FRAMES;
    private final double ROTATE_STEP = 5;
    private Timer animationTimer;
    private int scalingIdx;
    private boolean isScaling;
    private double scalingFactor;
    private boolean scalingDirection; //true for increase, false for decreasing
    private ArrayList<Integer> swapingIdx;
    private ArrayList<Cell> swapingCells;
    private Point swapStep1, swapStep2;
    private Point oldPos1, oldPos2;
    private boolean isSwaping;
    private int rotatingIdx;
    private boolean isRotating;
    private boolean rotatingDirection; //true for right, false for left
    private double rotatingFactor;
    private double rotatingEnd;

    public AnimationController(BoardView view) {
        this.view = view;
        this.isScaling = false;
        this.scalingIdx = -1;

        this.isSwaping = false;
        this.swapingIdx = new ArrayList<>();
        this.swapingCells = new ArrayList<>();

        this.isRotating = false;
        this.rotatingIdx = -2;
        this.animationTimer = new Timer(FRAME_LENGTH, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isScaling) {
                    if (scalingDirection) {
                        scalingFactor += SCALE_STEP;
                        if (scalingFactor >= MAX_SCALE) {
                            isScaling = false;
                            animationTimer.stop();
                        }
                    } else {
                        scalingFactor -= SCALE_STEP;
                        if (scalingFactor <= 1.0) {
                            isScaling = false;
                            animationTimer.stop();
                        }
                    }
                } else if (isSwaping) {
                    Cell cell1 = swapingCells.get(0);
                    Cell cell2 = swapingCells.get(1);
                    if (swapStep1 == null || swapStep2 == null) {
                        frameCount = 0;
                        oldPos1 = new Point(cell1.getPos());
                        oldPos2 = new Point(cell2.getPos());

                        swapStep1 = new Point(
                                (oldPos2.x - oldPos1.x) / TOTAL_FRAMES,
                                (oldPos2.y - oldPos1.y) / TOTAL_FRAMES
                        );
                        swapStep2 = new Point(
                                (oldPos1.x - oldPos2.x) / TOTAL_FRAMES,
                                (oldPos1.y - oldPos2.y) / TOTAL_FRAMES
                        );
                    }
                    if(frameCount >= TOTAL_FRAMES) {
                        cell1.setPos(oldPos2);
                        cell2.setPos(oldPos1);
                        resetSwapping();
                        animationTimer.stop();
                    }else {
                        cell1.moveCell(swapStep1);
                        cell2.moveCell(swapStep2);
                        frameCount++;
                    }
                }else if(isRotating) {
                    if(rotatingDirection) {
                        rotatingFactor += ROTATE_STEP;
                        if(rotatingFactor >= rotatingEnd) {
                            resetRotating();
                            animationTimer.stop();
                        }
                    }else {
                        rotatingFactor -= ROTATE_STEP;
                        if(rotatingFactor <= rotatingEnd) {
                            resetRotating();
                            animationTimer.stop();
                        }
                    }

                }
                view.repaint();
            }
        });
    }

    public boolean isAnimating() {
        return isScaling || isRotating || isSwaping;
    }

    public boolean isScaling() {
        return isScaling;
    }

    public boolean isSwaping() {
        return isSwaping;
    }

    public void setScaling(boolean scaling) {
        isScaling = scaling;
    }

    public int getScalingIdx() {
        return scalingIdx;
    }

    public void setScalingIdx(int scalingIdx) {
        this.scalingIdx = scalingIdx;
    }

    public void startScaling(boolean scalingDirection) {
        this.isScaling = true;
        this.scalingDirection = scalingDirection;
        if (this.scalingDirection) {
            this.scalingFactor = 1;
        } else {
            this.scalingFactor = MAX_SCALE;
        }
        this.animationTimer.start();
    }

    public double getScalingFactor() {
        return scalingFactor;
    }

    public void setSwapingIdx(int i1, int i2) {
        this.swapingIdx.clear();
        this.swapingIdx.add(i1);
        this.swapingIdx.add(i2);
    }

    public ArrayList<Integer> getSwapingIdx() {
        return swapingIdx;
    }

    public void setSwapingCells(Cell c1, Cell c2) {
        this.swapingCells.clear();
        this.swapingCells.add(c1);
        this.swapingCells.add(c2);
        this.swapingIdx.add(c1.getIdx());
        this.swapingIdx.add(c2.getIdx());
    }

    public ArrayList<Cell> getSwapingCells() {
        return this.swapingCells;
    }

    public void startSwaping() {
        this.isSwaping = true;
        this.animationTimer.start();
    }

    private boolean isCloseEnough(Point pos1, Point pos2, int tolerance) {
        return Math.abs(pos1.x - pos2.x) <= tolerance && Math.abs(pos1.y - pos2.y) <= tolerance;
    }

    private void resetSwapping() {
        swapingIdx = new ArrayList<>();
        swapingCells = new ArrayList<>();
        isSwaping = false;
        swapStep1 = null;
        swapStep2 = null;
        oldPos1 = null;
        oldPos2 = null;
    }

    public void startRotating(Cell cell, boolean rotatingDirection) {
        this.isRotating = true;
        this.rotatingIdx = cell.getIdx();
        this.rotatingDirection = rotatingDirection;
        this.rotatingFactor = cell.getRotation().getAngle();
        if(this.rotatingDirection) {
            this.rotatingEnd = this.rotatingFactor + 90;
        }else {
            this.rotatingEnd = this.rotatingFactor - 90;
        }
        this.animationTimer.start();
    }

    public int getRotatingIdx() {
        return rotatingIdx;
    }

    public boolean isRotating() {
        return isRotating;
    }

    public void setRotatingIdx(int rotatingIdx) {
        this.rotatingIdx = rotatingIdx;
    }

    public double getRotatingFactor() {
        return this.rotatingFactor;
    }

    private void resetRotating() {
        rotatingIdx = -2;
        isRotating = false;
    }
}
