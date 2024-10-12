import controllers.BoardController;
import views.Menubar;
import views.Window;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        if (true) {
            JFrame test = new JFrame();
            test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            test.setLayout(new BorderLayout());
            BoardController con = new BoardController("src/Image/test.png", Menubar.Difficulty.MEDIUM);
            test.add(con.getView(), BorderLayout.CENTER);

            test.pack();
            test.setLocationRelativeTo(null);
            test.setVisible(true);
        } else {
            Window win = new Window();
        }
    }
}