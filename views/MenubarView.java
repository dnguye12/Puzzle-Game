package views;


import javax.swing.*;
import java.awt.*;

public class MenubarView extends JMenuBar {
    private JButton newGameButton;
    private JButton quitButton;

    public MenubarView() {
        newGameButton = new JButton("New Game");
        quitButton = new JButton("Quit");

        quitButton.setBackground(Color.RED);

        this.add(newGameButton);
        this.add(Box.createHorizontalGlue());
        this.add(quitButton);
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }
}
