package views;

import controllers.BoardController;
import controllers.MenubarController;
import controllers.OptionsPanelController;
import models.MenubarModel;
import models.OptionsPanelModel;
import models.PopupFormModel;

import javax.swing.*;
import java.awt.*;
/*
public class Window extends JFrame {
    private Menubar menu;
    private OptionsPanel option;
    private BoardController boardController;

    public Window() {
        this.setTitle("PuzzleGame");
        this.setPreferredSize(new Dimension(1000, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.menu = new Menubar(this);
        this.setJMenuBar(menu);
        this.option = new OptionsPanel();
        this.add(option, BorderLayout.EAST);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void initBoardController() {
        this.boardController = new BoardController(this.menu.getSelectedImagePath(), this.menu.getDifficultyLevel());
        BoardView view = this.boardController.getView();

        Dimension menuSize = this.menu.getPreferredSize();
        Dimension toolSize = this.option.getPreferredSize();
        Dimension viewSize = view.getPreferredSize();

        int newWidth = viewSize.width + toolSize.width + 20;
        int newHeight = viewSize.height + menuSize.height + 40;
        this.setPreferredSize(new Dimension(newWidth, newHeight));
        this.add(view, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }
}*/
public class Window extends JFrame {

    private MenubarController menu;
    private OptionsPanelController option;
    private BoardController boardController;
    private OptionsPanelView optionsPanelView;
    private OptionsPanelModel optionsPanelModel;
    private MenubarModel menubarModel;
    private MenubarView menubarView;

    public Window() {
        this.setTitle("PuzzleGame");
        this.setPreferredSize(new Dimension(1000, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.optionsPanelModel = new OptionsPanelModel();
        this.optionsPanelView = new OptionsPanelView();
        this.option = new OptionsPanelController(optionsPanelModel, optionsPanelView);
        this.add(optionsPanelView, BorderLayout.EAST);

        this.menubarModel = new MenubarModel();
        this.menubarView = new MenubarView();
        menu = new MenubarController(this.menubarModel, this.menubarView, option, this);
        this.setJMenuBar(this.menubarView);

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void initBoardController(String imgPath, PopupFormModel.Difficulty difficulty) {
        if(this.boardController != null) {
            BoardView view = this.boardController.getView();
            this.remove(view);
            this.optionsPanelModel.setMoveCount(0);
            this.optionsPanelView.updateCounterLabel(0);
        }
        this.boardController = new BoardController(imgPath, difficulty, this.optionsPanelView, this);
        this.option.setBoardController(this.boardController);
        BoardView view = this.boardController.getView();

        Dimension menuSize = this.menu.getView().getPreferredSize();
        Dimension toolSize = this.option.getView().getPreferredSize();
        Dimension viewSize = view.getPreferredSize();

        int newWidth = viewSize.width + toolSize.width + 20;
        int newHeight = viewSize.height + menuSize.height + 40;
        this.setPreferredSize(new Dimension(newWidth, newHeight));
        this.add(view, BorderLayout.CENTER);

        view.revalidate();
        view.repaint();

        view.requestFocusInWindow();

        this.pack();
        this.setLocationRelativeTo(null);
        this.revalidate();
        this.repaint();
    }

    public void showEndGameDialog(int moveCount) {
        String message = "Game Over! You made " + moveCount + " moves. What would you like to do?";

        int response = JOptionPane.showOptionDialog(this,
                message,
                "End Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"New Game", "Quit"},
                "New Game");

        if (response == JOptionPane.YES_OPTION) {
            BoardView view = this.boardController.getView();
            this.remove(view);
            this.boardController.checkView();
        } else if (response == JOptionPane.NO_OPTION) {
            // Quit the game
            System.exit(0);
        }
    }

}