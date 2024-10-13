package controllers;

import models.PopupFormFunModel;
import models.PopupFormModel;
import views.PopupFormFunView;
import views.PopupFormView;
import views.Window;

import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;

public class PopupFormController {
    private PopupFormModel model;
    private PopupFormView view;
    private Window window;

    public PopupFormController(PopupFormModel model, PopupFormView view, Window window) {
        this.model = model;
        this.view = view;
        this.window = window;

        initView();
        initController();
    }

    private void initView() {
        view.updateImagePreview(model.getSelectedImagePath());
    }

    private void initController() {
        view.getDifficultyComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDifficultyChange();
            }
        });

        view.getStartGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStartGame();
            }
        });

        view.getFunButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFunButton();
            }
        });
    }

    private void handleDifficultyChange() {
        String selectedDifficulty = (String) view.getDifficultyComboBox().getSelectedItem();
        if ("Custom".equals(selectedDifficulty)) {
            view.getCustomDifficultyLabel().setVisible(true);
            view.getCustomDifficultyField().setVisible(true);
        } else {
            view.getCustomDifficultyLabel().setVisible(false);
            view.getCustomDifficultyField().setVisible(false);
        }
    }

    private void handleStartGame() {
        String playerName = view.getPlayerNameField().getText();
        PopupFormModel.Difficulty difficulty = PopupFormModel.Difficulty.fromString((String) view.getDifficultyComboBox().getSelectedItem());
        int customDifficulty = -1;
        String helper = "";
        if (difficulty == PopupFormModel.Difficulty.CUSTOM) {
            helper = view.getCustomDifficultyField().getText();
            if (!Objects.equals(helper, "")) {
                customDifficulty = Integer.parseInt(helper);
                difficulty.setValue(customDifficulty);
            }
        }

        model.setPlayerName(playerName);
        model.setDifficulty(difficulty);

        boolean canStart = true;
        if (model.getDifficulty() == PopupFormModel.Difficulty.CUSTOM) {
            if (helper.equals("")) {
                JOptionPane.showMessageDialog(view, "Please enter a custom difficulty !", "Error", JOptionPane.ERROR_MESSAGE);
                canStart = false;
            } else {
                if (customDifficulty <= 0) {
                    JOptionPane.showMessageDialog(view, "Custom difficulty cannot be 0 !", "Error", JOptionPane.ERROR_MESSAGE);
                    canStart = false;
                }else {
                    canStart = true;
                }
            }
        }
        if (model.getPlayerName().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter a player name!", "Error", JOptionPane.ERROR_MESSAGE);
            canStart = false;
        }else {
            canStart = true;
        }

        if(canStart) {
            System.out.println("Starting game for " + playerName + " with difficulty " + difficulty + " and image: " + model.getSelectedImagePath());
            model.getOptionsPanelController().startGame(playerName);
            view.close();
            window.initBoardController(model.getSelectedImagePath(), difficulty);
        }
    }

    private void handleFunButton() {
        model.setPlayerName(view.getPlayerNameField().getText());

        PopupFormModel.Difficulty difficulty = PopupFormModel.Difficulty.fromString((String) view.getDifficultyComboBox().getSelectedItem());
        model.setDifficulty(difficulty);
        if (difficulty == PopupFormModel.Difficulty.CUSTOM) {
            difficulty.setValue(Integer.parseInt(view.getCustomDifficultyField().getText()));
        }

        view.dispose();
        PopupFormFunModel funModel = new PopupFormFunModel(model.getSelectedImagePath(), model.getPlayerName(), model.getDifficulty(), model.getOptionsPanelController());
        PopupFormFunView funView = new PopupFormFunView(funModel);
        PopupFormFunController popupFormFunController = new PopupFormFunController(funModel, funView, window);
        popupFormFunController.showPopup();
    }

    public void showPopup() {
        view.display();
    }
}
