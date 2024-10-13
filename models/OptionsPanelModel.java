package models;

public class OptionsPanelModel {
    private boolean isPaused;
    private boolean isHelp;
    private int elapsedTime;
    private int moveCount;

    public OptionsPanelModel() {
        isPaused = false;
        isHelp = false;
        elapsedTime = 0;
        moveCount = 0;
    }

    public boolean getIsPaused() {
        return isPaused;
    }

    public void togglePaused() {
        this.isPaused = !this.isPaused;
    }

    public void setIsPaused(boolean b) {
        this.isPaused = b;
    }

    public boolean getIsHelp() {
        return isHelp;
    }

    public void toggleHelp() {
        this.isHelp = !this.isHelp;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int i) {
        this.elapsedTime = i;
    }

    public void incrementElapsedTime() {
        this.elapsedTime++;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void incrementMoveCount() {
        this.moveCount++;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }
}
