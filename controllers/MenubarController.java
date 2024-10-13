package controllers;

import models.MenubarModel;
import models.PopupFormModel;
import views.MenubarView;
import views.PopupFormView;
import views.Window;

import javax.swing.*;
import java.awt.event.*;

public class MenubarController {
    private MenubarModel model;
    private MenubarView view;
    private OptionsPanelController optionsPanelController;
    private Window window;

    public MenubarController(MenubarModel model, MenubarView view, OptionsPanelController optionsPanel, Window window) {
        this.model = model;
        this.view = view;
        this.optionsPanelController = optionsPanel;
        this.window = window;

        // Attach listeners to the view's buttons
        this.view.getNewGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooser();
            }
        });

        this.view.getQuitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser("src/gameImages");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "gif", "jpeg"));
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            model.setSelectedImagePath(fileChooser.getSelectedFile().getAbsolutePath());
            System.out.println("Selected file: " + model.getSelectedImagePath());

            PopupFormModel popupFormModel = new PopupFormModel(model.getSelectedImagePath(), "", PopupFormModel.Difficulty.MEDIUM, optionsPanelController);
            PopupFormView popupFormView = new PopupFormView(popupFormModel);
            PopupFormController popupFormController = new PopupFormController(popupFormModel, popupFormView, window);
            popupFormController.showPopup();
        }
    }

    public MenubarModel getModel() {
        return this.model;
    }

    public MenubarView getView() {
        return this.view;
    }
}
