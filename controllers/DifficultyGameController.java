package controllers;

import models.DifficultyGameModel;
import models.PopupFormModel;
import views.DifficultyGameView;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class DifficultyGameController {
	private DifficultyGameModel model;
    private DifficultyGameView view;
    
    public DifficultyGameController(DifficultyGameModel model, DifficultyGameView view) {
        this.model = model;
        this.view = view;
        
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (model.getCanMove()) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        model.moveLeft();
                        view.repaint();
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        model.moveRight();
                        view.repaint();
                    } else if (e.getKeyCode() == KeyEvent.VK_SPACE && !model.getIsJumping()) {
                        model.startJump();
                    }
                }
            }
        });
        view.setFocusable(true);
        view.requestFocusInWindow();
        
        Timer jumpTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.updateJump();
                model.checkCollision();
                view.repaint();
            }
        });
        jumpTimer.start();
        
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Rectangle characterRect = new Rectangle(model.getCharacterX(), model.getCharacterY() - model.getJumpOffset(), model.getCharSize(), model.getCharSize());
                if (characterRect.contains(e.getPoint())) {
                    model.setIsCharacterClicked(true);
                    model.setCanMove(true);
                } else {
                	model.setIsCharacterClicked(false);
                	model.setCanMove(false);
                }
                view.requestFocusInWindow();
                view.repaint();
            }
        });
    }
    
    public void setDifficultyBoxes(ArrayList<Rectangle> boxes) {
        model.setDifficultyBoxes(boxes);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Difficulty Game");
        DifficultyGameModel model = new DifficultyGameModel(PopupFormModel.Difficulty.MEDIUM);
        DifficultyGameView view = new DifficultyGameView(model);
        DifficultyGameController controller = new DifficultyGameController(model, view);

        frame.add(view);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        controller.setDifficultyBoxes(createDifficultyBoxes(view.getWidth()));

        view.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                controller.setDifficultyBoxes(createDifficultyBoxes(view.getWidth()));
            }
        });
    }

    private static ArrayList<Rectangle> createDifficultyBoxes(int width) {
        ArrayList<Rectangle> difficultyBoxes = new ArrayList<>();
        int boxSize = 60;
        int totalWidth = 4 * boxSize + 30;  // 4 options + 3 gaps of 10px
        int startX = (width - totalWidth) / 2;
        int y = 10;

        for (int i = 0; i < 4; i++) {
            int x = startX + (i * (boxSize + 10));
            difficultyBoxes.add(new Rectangle(x, y, boxSize, boxSize));
        }
        return difficultyBoxes;
    }
}
