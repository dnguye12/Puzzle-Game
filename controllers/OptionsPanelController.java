package controllers;

import models.OptionsPanelModel;
import views.OptionsPanelView;
import views.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsPanelController {

    private OptionsPanelModel model;
    private OptionsPanelView view;
    private Timer timer;
    private BoardController boardController;

    public OptionsPanelController(OptionsPanelModel model, OptionsPanelView view) {
        this.model = model;
        this.view = view;

        view.getPausePlayButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePausePlay();
            }
        });

        view.getHelpButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleHelp();
            }
        });

        initTimer();
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    private void togglePausePlay() {
        if (model.getIsPaused()) {
            view.setPauseIcon();
            startTimer();
        } else {
            view.setPlayIcon();
            stopTimer();
        }
        model.togglePaused();
        this.boardController.setPaused(model.getIsPaused());
    }

    private void toggleHelp() {
        model.toggleHelp();
        view.toggleHelpButtonColor(model.getIsHelp());
        this.boardController.setShowingHelp(model.getIsHelp());
        this.boardController.checkView();
    }

    private void initTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.incrementElapsedTime();
                int minutes = model.getElapsedTime() / 60;
                int seconds = model.getElapsedTime() % 60;
                String timeString = String.format("%02d:%02d", minutes, seconds);
                view.updateTimerLabel(timeString);
            }
        });
    }

    public void startGame(String playerName) {
        view.setPlayerName(playerName);
        model.setIsPaused(false);
        view.setPauseIcon();
        model.incrementElapsedTime();
        resetTimer();
        startTimer();
    }

    private void startTimer() {
        timer.start();
    }

    private void resetTimer() {
        model.setElapsedTime(0);
        view.updateTimerLabel("00:00");
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    public OptionsPanelView getView() {
        return view;
    }
}
