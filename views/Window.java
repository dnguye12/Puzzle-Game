package views;

import controllers.BoardController;

import javax.swing.*;
import java.awt.*;

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
}
