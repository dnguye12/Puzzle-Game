package models;

import controllers.DifficultyGameController;
import controllers.OptionsPanelController;
import views.DifficultyGameView;
import views.OptionsPanelView;

import java.awt.*;
import java.util.*;

public class PopupFormFunModel {
    private String playerName;
    private PopupFormModel.Difficulty difficulty;
    private String selectedImagePath;
    private StringBuilder playerNameBuilder;
    private boolean isUpperCase;
    private int counter;
    private OptionsPanelModel optionsPanelModel;
    private OptionsPanelView optionsPanelView;
    private OptionsPanelController optionsPanelController;
    private DifficultyGameModel gameModel;
    private DifficultyGameView gameView;
    private DifficultyGameController gameController;


    public PopupFormFunModel(String selectedImagePath, String playerName, PopupFormModel.Difficulty difficulty, OptionsPanelController optionsPanelController) {
        this.selectedImagePath = selectedImagePath;
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.playerNameBuilder = new StringBuilder(playerName);
        this.isUpperCase = true;
        if(difficulty == PopupFormModel.Difficulty.CUSTOM) {
            this.counter  = difficulty.getValue();
        }else {
            this.counter = 0;
        }

        this.optionsPanelController = optionsPanelController;

        gameModel = new DifficultyGameModel(this.difficulty);
        gameView = new DifficultyGameView(gameModel);
        gameController = new DifficultyGameController(gameModel, gameView);
    }

    public ArrayList<Rectangle> createDifficultyBoxes(int width) {
        ArrayList<Rectangle> difficultyBoxes = new ArrayList<>();
        int boxSize = getGameModel().getBoxSize();
        int startX = 70;
        int y = 10;

        for (int i = 0; i < 4; i++) {
            int x = startX + (i * (boxSize + 10));
            difficultyBoxes.add(new Rectangle(x, y, boxSize, boxSize));
        }
        return difficultyBoxes;
    }

    // SelectedImagePath getter
    public String getSelectedImagePath() {
        return selectedImagePath;
    }

    // PlayerName getter and setter
    public String getPlayerName() {
        return playerNameBuilder.toString();
    }

    public void addCharacterToPlayerName(char letter) {
        playerNameBuilder.append(letter);
    }

    public void removeLastCharacterFromPlayerName() {
        playerNameBuilder.deleteCharAt(playerNameBuilder.length() - 1);
    }

    // Custom difficulty setter
    public void setCustomDifficulty(PopupFormModel.Difficulty customDifficulty) {
        this.difficulty = customDifficulty;
        this.counter = customDifficulty.getValue();
    }

    // UpperCase getter and setter
    public boolean getIsUpperCase() {
        return isUpperCase;
    }

    public void toggleUpperCase() {
        this.isUpperCase = !this.isUpperCase;
    }

    public void setUpperCase(boolean b) {
        this.isUpperCase = b;
    }

    // Counter getter and setter
    public int getCounter() {
        return counter;
    }

    public void incrementCounterBy(int value) {
        this.counter += value;
    }

    // OptionsPanel getter
    public OptionsPanelModel getOptionsPanelModel() {
        return optionsPanelModel;
    }

    public OptionsPanelView getOptionsPanelView() {
        return optionsPanelView;
    }

    public OptionsPanelController getOptionsPanelController() {
        return optionsPanelController;
    }

    // DifficultyGame getter and setter
    public DifficultyGameModel getGameModel() {
        return gameModel;
    }

    public DifficultyGameView getGameView() {
        return gameView;
    }

    public DifficultyGameController getGameController() {
        return gameController;
    }

    // Difficulty getter and setter
    public void setDifficulty(PopupFormModel.Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public PopupFormModel.Difficulty getDifficulty() {
        return difficulty;
    }
}
