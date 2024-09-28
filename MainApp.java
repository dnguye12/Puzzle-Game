import controllers.BoardController;

import javax.swing.*;
import java.awt.*;

public class MainApp {
    private JFrame frame;

    public MainApp() {
        this.frame = new JFrame();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.frame.setLayout(new BorderLayout());
        BoardController con = new BoardController("src/Image/test.png");
        this.frame.add(con.getView(), BorderLayout.CENTER);

        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem btnOpen = new JMenuItem("Open");

        return fileMenu;
    }
}
