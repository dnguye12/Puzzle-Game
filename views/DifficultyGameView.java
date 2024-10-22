package views;

import models.DifficultyGameModel;
import models.PopupFormModel;

import java.awt.*;
import javax.swing.*;

public class DifficultyGameView extends JComponent {
	
	private DifficultyGameModel model;
	
	public DifficultyGameView(DifficultyGameModel model) {
        this.model = model;
        setPreferredSize(new Dimension(290, 120));
        setMinimumSize(new Dimension(290, 120));
        setBackground(Color.WHITE);
    }
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Creation of boxes
        for (int i = 0; i < model.getDifficultyBoxes().size(); i++) {
            Rectangle box = model.getDifficultyBoxes().get(i);
            if (PopupFormModel.Difficulty.fromString(model.getDifficultyOptions()[i]).equals(model.getSelectedDifficulty())) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }
            
            g.fillRect(box.x, box.y, box.width, box.height);

            g.setColor(Color.BLACK);
            g.drawRect(box.x, box.y, box.width, box.height);

            g.setColor(Color.BLACK);
            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(model.getDifficultyOptions()[i]);
            int textHeight = metrics.getHeight();
            g.drawString(model.getDifficultyOptions()[i], 
                box.x + (model.getBoxSize() - textWidth) / 2, 
                box.y + (model.getBoxSize() + textHeight) / 2 - 5);
        }

        // Change color if square selected
        if (model.getIsCharacterClicked()) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GRAY);
        }
        g.fillRect(model.getCharacterX(), model.getCharacterY() - model.getJumpOffset(), model.getCharSize(), model.getCharSize());
    }

}
