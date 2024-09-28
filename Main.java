import controllers.BoardController;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame test = new JFrame();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        test.setLayout(new BorderLayout());
        BoardController con = new BoardController("src/Image/test.png");
        test.add(con.getView(), BorderLayout.CENTER);

        test.pack();
        test.setLocationRelativeTo(null);
        test.setVisible(true);
    }
}