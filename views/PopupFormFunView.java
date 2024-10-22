package views;

import models.PopupFormFunModel;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class PopupFormFunView extends JDialog {
	private PopupFormFunModel model;
	private JLabel playerNameLabel;
    private JSlider letterSlider;
    private JButton lowercaseButton;
    private JButton uppercaseButton;
    private JButton validateButton;
    private JButton removeButton;
    private JLabel counterLabel;
    private JButton counterButton;
    private JButton notFunButton;
    private JButton startGameButton;
    
    public PopupFormFunView(PopupFormFunModel model) {
        this.model = model;
        setLocationRelativeTo((Frame) null);
        setModal(true);
        setTitle("Fun New Game Settings");
        setLayout(new BorderLayout(10, 10));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;

        // Input PlayerName
        JPanel inputPanel = new JPanel(new GridBagLayout());

        playerNameLabel = new JLabel("Player Name: " + model.getPlayerName(), SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(playerNameLabel, gbc);

        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        letterSlider = new JSlider(0, 25, 0);
        letterSlider.setMajorTickSpacing(1);
        letterSlider.setPaintTicks(true);
        letterSlider.setSnapToTicks(true);
        letterSlider.setLabelTable(createLetterLabelTable());
        letterSlider.setPaintLabels(true);
        letterSlider.setPreferredSize(new Dimension(400, 50));
        letterSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, letterSlider.getPreferredSize().height));

        sliderPanel.add(letterSlider);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(sliderPanel, gbc);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lowercaseButton = new JButton("a");
        buttonRow.add(lowercaseButton);

        uppercaseButton = new JButton("A");
        buttonRow.add(uppercaseButton);

        validateButton = new JButton("Valider");
        buttonRow.add(validateButton);

        removeButton = new JButton("Remove");
        buttonRow.add(removeButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(buttonRow, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Input difficulty
        model.getGameController().setDifficultyBoxes(model.createDifficultyBoxes(model.getGameView().getWidth()));
        
        inputPanel.add(model.getGameView(), gbc);

        counterLabel = new JLabel("Counter: " + model.getCounter());
        counterButton = new JButton("Add 2 to Counter");

        counterLabel.setVisible(false);
        counterButton.setVisible(false);

        gbc.gridy = 4;
        inputPanel.add(counterLabel, gbc);
        gbc.gridy = 5;
        inputPanel.add(counterButton, gbc);

        centerPanel.add(inputPanel);
        add(centerPanel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel notFunButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notFunButton = new JButton("Not Fun");
        notFunButtonPanel.add(notFunButton);

        JPanel startButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        startGameButton = new JButton("Start Playing");
        startButtonPanel.add(startGameButton);

        buttonPanel.add(notFunButtonPanel, BorderLayout.WEST);
        buttonPanel.add(startButtonPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void display() {
        setVisible(true);
    }

    public void close() {
        dispose();
    }

    public void updatePlayerNameLabel(String playerName) {
        playerNameLabel.setText("Player Name: " + playerName);
    }

    public void updateCounterLabel(int counter) {
        counterLabel.setText("Counter: " + counter);
    }

    public JLabel getPlayerNameLabel() {
        return playerNameLabel;
    }

    public JSlider getLetterSlider() {
        return letterSlider;
    }

    public JButton getValidateButton() {
        return validateButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }
    
    public JButton getLowercaseButton() {
        return lowercaseButton;
    }

    public JButton getUppercaseButton() {
        return uppercaseButton;
    }

    public JLabel getCounterLabel() {
        return counterLabel;
    }

    public JButton getCounterButton() {
        return counterButton;
    }
    
    public JButton getNotFunButton() {
        return notFunButton;
    }

    public JButton getStartGameButton() {
        return startGameButton;
    }
    
    private Hashtable<Integer, JLabel> createLetterLabelTable() {
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 0; i < 26; i++) {
            labelTable.put(i, new JLabel(String.valueOf((char) ('A' + i))));
        }
        return labelTable;
    }
    
    public void updateSliderLabels(JSlider letterSlider) {
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            if (!model.getIsUpperCase()) {
                letter = Character.toLowerCase(letter);
            }
            labelTable.put(i, new JLabel(String.valueOf(letter)));
        }
        letterSlider.setLabelTable(labelTable);
        letterSlider.repaint();
    }
}
