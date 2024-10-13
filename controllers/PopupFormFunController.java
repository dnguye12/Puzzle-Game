package controllers;

import models.PopupFormFunModel;
import models.PopupFormModel;
import views.PopupFormFunView;
import views.PopupFormView;
import views.Window;

import java.awt.event.*;
import javax.swing.*;

public class PopupFormFunController {
	private PopupFormFunModel model;
    private PopupFormFunView view;
    private Window window;
    public PopupFormFunController(PopupFormFunModel model, PopupFormFunView view, Window window) {
        this.model = model;
        this.view = view;
        this.window = window;
        
        initView();
        initController();
    }
    
    private void initView() {
        view.updatePlayerNameLabel(model.getPlayerName());
        view.updateCounterLabel(model.getCounter());
    }
    
    private void initController() {
    	view.getValidateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char currentLetter = (char) ('A' + view.getLetterSlider().getValue());
                if (!model.getIsUpperCase()) {
                    currentLetter = Character.toLowerCase(currentLetter);
                }
                if (model.getPlayerName().length() < 15) {
                    model.addCharacterToPlayerName(currentLetter);
                    view.updatePlayerNameLabel(model.getPlayerName());
                } else {
                    JOptionPane.showMessageDialog(view, "Player name cannot exceed 15 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    	view.getRemoveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getPlayerName().length() > 0) {
                	model.removeLastCharacterFromPlayerName();
                    view.updatePlayerNameLabel(model.getPlayerName());
                }
            }
        });
        
        view.getLowercaseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setUpperCase(false);
                view.updateSliderLabels(view.getLetterSlider());
            }
        });
        
        view.getUppercaseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	model.setUpperCase(true);
                view.updateSliderLabels(view.getLetterSlider());
            }
        });

        view.getCounterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.incrementCounterBy(2);
                view.updateCounterLabel(model.getCounter());
            }
        });

        Timer difficultyChecker = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getGameModel().getSelectedDifficulty() == PopupFormModel.Difficulty.CUSTOM) {
                    view.getCounterLabel().setVisible(true);
                    view.getCounterButton().setVisible(true);
                } else {
                    view.getCounterLabel().setVisible(false);
                    view.getCounterButton().setVisible(false);
                }
            }
        });
        difficultyChecker.start();
        
        view.getNotFunButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setDifficulty(model.getGameModel().getSelectedDifficulty());
                if(model.getGameModel().getSelectedDifficulty() == PopupFormModel.Difficulty.CUSTOM) {
                    PopupFormModel.Difficulty helper = PopupFormModel.Difficulty.CUSTOM;
                    helper.setValue(model.getCounter());
                    model.setDifficulty(helper);
                }
                
                view.close();
                PopupFormModel popupFormModel = new PopupFormModel(model.getSelectedImagePath(), model.getPlayerName(), model.getDifficulty(), model.getOptionsPanelController());
                PopupFormView popupFormView = new PopupFormView(popupFormModel);
                PopupFormController popupFormController = new PopupFormController(popupFormModel, popupFormView, window);
                popupFormController.showPopup();
            }
        });
        
        view.getStartGameButton().addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {

                model.setDifficulty(model.getGameModel().getSelectedDifficulty());
                if(model.getGameModel().getSelectedDifficulty() == PopupFormModel.Difficulty.CUSTOM) {
                    PopupFormModel.Difficulty helper = PopupFormModel.Difficulty.CUSTOM;
                    helper.setValue(model.getCounter());
                    model.setDifficulty(helper);
                }
                
            	if (model.getDifficulty() == PopupFormModel.Difficulty.CUSTOM) {
                    if (model.getDifficulty().getValue() == -1) {
                    	JOptionPane.showMessageDialog(view, "Please enter a custom difficulty !", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int customValue = model.getDifficulty().getValue();
                        if (customValue <= 0) {
                        	JOptionPane.showMessageDialog(view, "Custom difficulty cannot be 0 !", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                } else if (model.getPlayerName().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Please enter a player name!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("Starting game for " + model.getPlayerName() + " with difficulty " + model.getDifficulty() + " and image: " + model.getSelectedImagePath());
                    model.getOptionsPanelController().startGame(model.getPlayerName());
                    view.close();
                    window.initBoardController(model.getSelectedImagePath(), model.getDifficulty());
                }
            }
        });
    }

    public void showPopup() {
        view.display();
    }

    public void close() {
        view.close();
    }
}
