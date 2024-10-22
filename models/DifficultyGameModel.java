package models;

import controllers.DifficultyGameController;

import java.awt.*;
import java.util.*;

public class DifficultyGameModel {
    private DifficultyGameController con;

    // Constant variables that is use to initiate the game
    private static final int BOX_SIZE = 60;
    private static final int CHAR_SIZE = 20;
    private static final int INITIAL_X = 50;
    private static final int INITIAL_Y = 90;
    private static final int MOVE_SPEED = 5;
    private static final int JUMP_HEIGHT = 40;

    private int characterX = INITIAL_X;
    private int characterY = INITIAL_Y;
    private boolean isJumping = false;
    private int jumpOffset = 0;

    private ArrayList<Rectangle> difficultyBoxes;
    private String[] difficultyOptions = {"Easy", "Medium", "Hard", "Custom"};
    private PopupFormModel.Difficulty selectedDifficulty = PopupFormModel.Difficulty.MEDIUM;

    private boolean canMove = false;
    private boolean isCharacterClicked = false;

    public DifficultyGameModel(PopupFormModel.Difficulty defaultDifficulty) {
        this.selectedDifficulty = defaultDifficulty;
        this.difficultyBoxes = new ArrayList<>();
    }

    // Functions to move
    public void moveLeft() {
        characterX -= MOVE_SPEED;
    }

    public void moveRight() {
        characterX += MOVE_SPEED;
    }

    public void startJump() {
        if (!isJumping) {
            isJumping = true;
        }
    }

    public void updateJump() {
        if (isJumping) {
            if (jumpOffset < JUMP_HEIGHT) {
                jumpOffset += 2;
            } else {
                isJumping = false;
            }
        } else {
            if (jumpOffset > 0) {
                jumpOffset -= 2;
            }
        }
    }

    // Function that is check if the red square is overlapping a difficulty boxes
    public void checkCollision() {
        Rectangle characterRect = new Rectangle(characterX, characterY - jumpOffset, CHAR_SIZE, CHAR_SIZE);
        for (int i = 0; i < difficultyBoxes.size(); i++) {
            if (characterRect.intersects(difficultyBoxes.get(i))) {
                selectedDifficulty = PopupFormModel.Difficulty.fromString(difficultyOptions[i]);
                con.getView().repaint();
                break;
            }
        }
    }

    // DifficultyBoxes getter and setter
    public ArrayList<Rectangle> getDifficultyBoxes() {
        return this.difficultyBoxes;
    }

    public void setDifficultyBoxes(ArrayList<Rectangle> boxes) {
        this.difficultyBoxes = boxes;
    }

    public void addDifficultyBoxes(Rectangle box) {
        this.difficultyBoxes.add(box);
    }

    // Character getter
    public int getCharacterX() {
        return characterX;
    }

    public int getCharacterY() {
        return characterY - jumpOffset;
    }

    // Character size getter
    public int getCharSize() {
        return CHAR_SIZE;
    }

    // Box size getter
    public int getBoxSize() {
        return BOX_SIZE;
    }

    // CanMove boolean getter and setter
    public boolean getCanMove() {
        return canMove;
    }

    public void setCanMove(boolean b) {
        canMove = b;
    }

    // IsJumping boolean getter
    public boolean getIsJumping() {
        return isJumping;
    }

    // isCharacterClicked boolean getter and setter
    public boolean getIsCharacterClicked() {
        return isCharacterClicked;
    }

    public void setIsCharacterClicked(boolean b) {
        isCharacterClicked = b;
    }

    // SelectedDifficulty getter
    public PopupFormModel.Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    // DifficultyOptions getter
    public String[] getDifficultyOptions() {
        return difficultyOptions;
    }

    // JumpOffset integer getter
    public int getJumpOffset() {
        return jumpOffset;
    }

    // DifficultyGameController setter
    public void setDifficultyGameController(DifficultyGameController con) {
        this.con = con;
    }



}
