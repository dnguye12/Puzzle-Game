package models;

import controllers.OptionsPanelController;

import java.util.Objects;

public class PopupFormModel {
    public enum Difficulty {
        EASY(5), MEDIUM(2), HARD(9), CUSTOM(0);

        private int value;

        Difficulty(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int n) {
            this.value = n;
        }

        public static Difficulty fromString(String str) {
            if(Objects.equals(str, "Easy")) {
                return EASY;
            }else if(Objects.equals(str, "Medium")) {
                return MEDIUM;
            }else if(Objects.equals(str, "Hard")) {
                return HARD;
            }
            return CUSTOM;
        }
    }
    private String selectedImagePath;
    private String playerName;
    private Difficulty difficulty;
    private OptionsPanelController optionsPanelController;

    public PopupFormModel(String selectedImagePath, String playerName, Difficulty difficulty, OptionsPanelController optionsPanelController) {
        this.selectedImagePath = selectedImagePath;
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.optionsPanelController = optionsPanelController;
    }

    public String getSelectedImagePath() {
        return selectedImagePath;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public OptionsPanelController getOptionsPanelController() {
        return optionsPanelController;
    }
}

