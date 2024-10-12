package views;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class Menubar extends JMenuBar {
    public enum Difficulty {
        EASY(5), MEDIUM(7), HARD(9), CUSTOM(0);

        private int value;

        Difficulty(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int n) {
            this.value = n;
        }
    }

    ; //5x5, 7x7, 9x9
    private Window window;
    private String selectedImagePath; // Variable to store the selected image path
    private Difficulty difficultyLevel;

    public Menubar(Window window) {
        this.window = window;
        // Create buttons
        JButton newGameButton = new JButton("New Game");
        JButton quitButton = new JButton("Quit");

        // Add action for 'New Game' button (opens a file chooser)
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooser();
            }
        });

        // Add action for 'Quit' button (quits the application)
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        quitButton.setBackground(Color.RED); // Set quit button background color to red

        // Add components to the menu bar
        this.add(newGameButton);
        this.add(Box.createHorizontalGlue()); // Push the next component to the right
        this.add(quitButton);
    }

    // Method to open the file chooser
    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser("src/Image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "gif", "jpeg"));
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            // Store the selected image path
            selectedImagePath = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("Selected file: " + selectedImagePath);
            openPopupForm(); // Open the popup form after selecting an image
        }
    }

    private void openPopupForm() {
        JDialog dialog = new JDialog((Frame) null, "New Game Settings", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Center panel for player name, difficulty, and image preview
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some margin between components

        // North panel for player name and difficulty selection
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Player name label and text field
        inputPanel.add(new JLabel("Player Name:"));
        JTextField playerNameField = new JTextField();
        inputPanel.add(playerNameField);

        // Difficulty label and combo box
        inputPanel.add(new JLabel("Difficulty:"));
        String[] difficulties = {"Easy", "Medium", "Hard", "Custom"};
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setSelectedItem("Medium");
        inputPanel.add(difficultyComboBox);

        // Custom difficulty label and input field
        JLabel customDifficultyLabel = new JLabel("Custom Difficulty:");
        customDifficultyLabel.setVisible(false);
        inputPanel.add(customDifficultyLabel);

        JTextField customDifficultyField = new JTextField();
        customDifficultyField.setVisible(false);
        ((AbstractDocument) customDifficultyField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        inputPanel.add(customDifficultyField);

        // Add inputPanel to the centerPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(inputPanel, gbc);

        // Panel for image label and image preview
        JPanel imagePanel = new JPanel(new GridBagLayout());
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;

        // Label "Selected Image:" on the left
        JLabel imageLabel = new JLabel("Selected Image:");
        imagePanel.add(imageLabel, gbc);

        // Image preview label on the right
        JLabel imagePreviewLabel = new JLabel("", SwingConstants.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(200, 200));
        imagePreviewLabel.setMaximumSize(new Dimension(200, 200));
        gbc.gridx = 1;
        imagePanel.add(imagePreviewLabel, gbc);

        // Add imagePanel to the centerPanel
        gbc.gridx = 0;
        gbc.gridy = 2; // Move to the next row
        gbc.gridwidth = 2;
        centerPanel.add(imagePanel, gbc);

        // Error label below the image preview
        JLabel errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 3; // Move to the next row
        centerPanel.add(errorLabel, gbc);

        // Load the image and resize it for preview
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
                imagePreviewLabel.setIcon(new ImageIcon(resizedImage)); // Set the resized image as an icon
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        dialog.add(centerPanel, BorderLayout.CENTER);

        // Panel for the buttons, positioned at the bottom of the dialog
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel for the Fun button, positioned on the left
        JPanel funButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton funButton = new JButton("Fun"); // Add Fun button

        // Action for the Fun button
        funButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Close the current dialog
                openFunPopupForm(); // Open the fun dialog
            }
        });

        funButtonPanel.add(funButton); // Add Fun button to the left panel

        // Panel for the Start Playing button, positioned on the right
        JPanel startButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton startGameButton = new JButton("Start Playing");

        // Action for the Start Playing button
        ActionListener startGameAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = playerNameField.getText();
                String difficulty = (String) difficultyComboBox.getSelectedItem();

                // Clear the error label
                errorLabel.setText("");

                // Handle custom difficulty input
                if (difficulty.equals("Easy")) {
                    difficultyLevel = Difficulty.EASY;
                } else if (difficulty.equals("Medium")) {
                    difficultyLevel = Difficulty.MEDIUM;
                } else if (difficulty.equals("Hard")) {
                    difficultyLevel = Difficulty.HARD;
                } else if (difficulty.equals("Custom")) {
                    String customDifficulty = customDifficultyField.getText();
                    if (customDifficulty.isEmpty()) {
                        errorLabel.setText("Please enter a custom difficulty!");
                        return;
                    } else {
                        try {
                            difficultyLevel = Difficulty.CUSTOM;
                            difficultyLevel.setValue(Integer.parseInt(customDifficulty));
                        } catch (NumberFormatException ex) {
                            errorLabel.setText("Custom difficulty must be a number!");
                            return;
                        }
                        System.out.println("Custom difficulty set to: " + customDifficulty);
                    }
                }

                if (playerName.isEmpty()) {
                    errorLabel.setText("Please enter a player name!");
                } else {
                    // Start the game with the selected player name, difficulty, and image
                    System.out.println("Starting game for " + playerName + " with difficulty " + difficulty + " and image: " + selectedImagePath);
                    window.initBoardController();
                    dialog.dispose();
                }
            }
        };

        startGameButton.addActionListener(startGameAction);
        startButtonPanel.add(startGameButton); // Add Start Playing button to the right panel

        // Add both panels to the buttonPanel
        buttonPanel.add(funButtonPanel, BorderLayout.WEST); // Fun button on the left
        buttonPanel.add(startButtonPanel, BorderLayout.EAST); // Start Playing button on the right

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set the default button to allow pressing "Enter" to start the game
        dialog.getRootPane().setDefaultButton(startGameButton);

        // Add an action listener to show/hide the custom difficulty field
        difficultyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (difficultyComboBox.getSelectedItem().equals("Custom")) {
                    customDifficultyLabel.setVisible(true);
                    customDifficultyField.setVisible(true);
                } else {
                    customDifficultyLabel.setVisible(false);
                    customDifficultyField.setVisible(false);
                }
            }
        });

        // Set dialog properties
        dialog.setSize(430, 430); // Set the desired size
        dialog.setResizable(false); // Disable resizing
        dialog.setLocationRelativeTo(null); // Center on screen
        dialog.setVisible(true); // Make dialog visible
    }

    // Method to open the fun dialog
    private void openFunPopupForm() {
        JDialog funDialog = new JDialog((Frame) null, "Fun Game Settings", true);
        funDialog.setLayout(new BorderLayout(10, 10));
        funDialog.setSize(430, 430); // Same size as the original dialog
        funDialog.setResizable(false); // Disable resizing
        funDialog.setLocationRelativeTo(null); // Center on screen

        // Add content to the fun dialog here
        // For now, we'll just use a label to indicate this is the fun dialog
        funDialog.add(new JLabel("This is a fun way to enter game settings!", SwingConstants.CENTER), BorderLayout.CENTER);

        funDialog.setVisible(true); // Make fun dialog visible
    }

    public String getSelectedImagePath() {
        return this.selectedImagePath;
    }

    public Difficulty getDifficultyLevel() {
        return this.difficultyLevel;
    }

    // DocumentFilter to allow only numeric input
    class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d*")) { // Allow only digits
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && text.matches("\\d*")) { // Allow only digits
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }
}
