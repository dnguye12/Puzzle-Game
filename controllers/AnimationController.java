package controllers;

import views.BoardView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimationController {
    private BoardView view;
    private final int FRAME_LENGTH = 1000 / 60; //60fps
    private final int ANIMATION_DURATION = 300; // 300ms
    private final int TOTAL_FRAMES = ANIMATION_DURATION / FRAME_LENGTH;
    private final double MAX_SCALE = 1.1;
    private final double SCALE_STEP = (MAX_SCALE - 1) / TOTAL_FRAMES;
    private int scalingIdx;
    private int rotatingIdx;
    private boolean isScaling;
    private Timer scalingTimer;
    private double scalingFactor;
    private boolean scalingDirection; //true for increase, false for decreasing
    private boolean isRotating;

    public AnimationController(BoardView view) {
        this.view = view;
        this.isScaling = false;
        this.scalingIdx = -1;
        this.rotatingIdx = -1;
        this.scalingTimer = new Timer(FRAME_LENGTH, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(scalingDirection) {
                    scalingFactor += SCALE_STEP;
                    if (scalingFactor >= MAX_SCALE) {
                        isScaling = false;
                        scalingTimer.stop();
                    }
                }else {
                    scalingFactor -= SCALE_STEP;
                    if(scalingFactor <= 1.0) {
                        isScaling = false;
                        scalingTimer.stop();
                    }
                }
                view.repaint();
            }
        });

        this.isRotating = false;
    }

    public boolean isScaling() {
        return isScaling;
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

    public int getRotatingIdx() {
        return rotatingIdx;
    }

    public void setRotatingIdx(int rotatingIdx) {
        this.rotatingIdx = rotatingIdx;
    }

    public void startScaling(boolean scalingDirection) {
        this.isScaling = true;
        this.scalingDirection = scalingDirection;
        if(this.scalingDirection) {
            this.scalingFactor = 1;
        }else {
            this.scalingFactor = MAX_SCALE;
        }
        this.scalingTimer.start();
    }

    public double getScalingFactor() {
        return scalingFactor;
    }
}
