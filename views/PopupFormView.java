package views;

import models.PopupFormModel;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.text.*;

public class PopupFormView extends JDialog {
    private PopupFormModel model;
    private JTextField playerNameField;
    private JComboBox<String> difficultyComboBox;
    private JTextField customDifficultyField;
    private JLabel customDifficultyLabel;
    private JLabel imagePreviewLabel;
    private JButton funButton;
    private JButton startGameButton;

    public PopupFormView(PopupFormModel model) {
        this.model = model;
        setTitle("New Game Settings");
        setLocationRelativeTo((Frame) null);
        setModal(true);
        setLayout(new BorderLayout(10, 10));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Player Name:"));
        playerNameField = new JTextField(model.getPlayerName());
        inputPanel.add(playerNameField);

        inputPanel.add(new JLabel("Difficulty:"));
        String[] difficulties = {"Easy", "Medium", "Hard", "Custom"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setSelectedItem(model.getDifficulty());
        inputPanel.add(difficultyComboBox);

        customDifficultyLabel = new JLabel("Custom Difficulty:");
        if (model.getDifficulty() == PopupFormModel.Difficulty.CUSTOM) {
            customDifficultyLabel.setVisible(true);
        }else {
            customDifficultyLabel.setVisible(false);
        }
        inputPanel.add(customDifficultyLabel);

        customDifficultyField = new JTextField(model.getDifficulty().getValue());
        if (model.getDifficulty() == PopupFormModel.Difficulty.CUSTOM) {
            ((AbstractDocument) customDifficultyField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
            customDifficultyField.setVisible(true);
        }else {
            customDifficultyField.setVisible(false);
        }
        inputPanel.add(customDifficultyField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(inputPanel, gbc);

        JPanel imagePanel = new JPanel(new GridBagLayout());
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;

        JLabel imageLabel = new JLabel("Selected Image:");
        imagePanel.add(imageLabel, gbc);

        imagePreviewLabel = new JLabel("", SwingConstants.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 200));
        imagePreviewLabel.setMaximumSize(new Dimension(200, 200));
        gbc.gridx = 1;
        imagePanel.add(imagePreviewLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(imagePanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel funButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        funButton = new JButton("Fun");

        funButtonPanel.add(funButton);

        JPanel startButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        startGameButton = new JButton("Start Playing");

        startButtonPanel.add(startGameButton);

        buttonPanel.add(funButtonPanel, BorderLayout.WEST);
        buttonPanel.add(startButtonPanel, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void updateImagePreview(String selectedImagePath) {
        if (selectedImagePath != null) {
            try {
                BufferedImage originalImage = ImageIO.read(new File(selectedImagePath));
                int maxDimension = 200;

                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();
                double scaleFactor = Math.min((double) maxDimension / originalWidth, (double) maxDimension / originalHeight);
                int newWidth = (int) (originalWidth * scaleFactor);
                int newHeight = (int) (originalHeight * scaleFactor);

                Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                imagePreviewLabel.setIcon(new ImageIcon(resizedImage));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void display() {
        setVisible(true);
    }

    public void close() {
        dispose();
    }

    public JTextField getPlayerNameField() {
        return playerNameField;
    }

    public JComboBox<String> getDifficultyComboBox() {
        return difficultyComboBox;
    }

    public JTextField getCustomDifficultyField() {
        return customDifficultyField;
    }

    public JLabel getCustomDifficultyLabel() {
        return customDifficultyLabel;
    }

    public JButton getFunButton() {
        return funButton;
    }

    public JButton getStartGameButton() {
        return startGameButton;
    }

    class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (isNumeric(string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (isNumeric(text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        private boolean isNumeric(String text) {
            return text.matches("\\d*");
        }
    }
}
