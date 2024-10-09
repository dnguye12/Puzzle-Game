package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsPanel extends JPanel {
    private JLabel playerNameLabel;
    private JButton pausePlayButton;
    private JButton helpButton;
    private JLabel timerLabel;
    private JLabel counterLabel;

    private boolean isPaused = false;
    private boolean isHelp = false;

    public OptionsPanel() {
        // Set layout for the panel
        this.setLayout(new BorderLayout());

        // Add a light gray border around the panel with increased thickness
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 7), // Light gray border with thickness of 4
                BorderFactory.createEmptyBorder(20, 20, 20, 20) // Invisible margin of 10 pixels
        ));

        // Top panel for player name and buttons
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Player name label at top
        playerNameLabel = new JLabel("PlayerName");
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Increase font size here
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // Align left
        topPanel.add(playerNameLabel, gbc);

        // Pause/Play and Help buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Pause/Play button
        pausePlayButton = new JButton();
        pausePlayButton.setPreferredSize(new Dimension(40, 40)); // Width: 40, Height: 40
        setPauseIcon(); // Set initial icon for pause button

        // Help button as a JButton with the same size
        helpButton = new JButton("Help");
        helpButton.setPreferredSize(new Dimension(70, 40)); // Same size as the Pause/Play button

        // Add buttons to the panel
        buttonsPanel.add(pausePlayButton);
        buttonsPanel.add(helpButton);

        // Add buttons panel to top panel below player name
        gbc.gridy = 1; // Move to the next row
        gbc.anchor = GridBagConstraints.EAST; // Align right
        topPanel.add(buttonsPanel, gbc);

        // Add the top panel to the main panel
        this.add(topPanel, BorderLayout.NORTH);

        // Center panel for timer and counter using GridBagLayout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0; // Position on the x-axis
        gbc.gridy = 0; // Position on the y-axis
        gbc.anchor = GridBagConstraints.CENTER; // Center the components

        // Timer label
        timerLabel = new JLabel("Timer: 00:00", SwingConstants.CENTER);
        centerPanel.add(timerLabel, gbc);

        // Add vertical space
        gbc.gridy = 1; // Move to the next row
        centerPanel.add(Box.createVerticalStrut(10), gbc);

        // Counter label
        gbc.gridy = 2; // Move to the next row
        counterLabel = new JLabel("Moves: 0", SwingConstants.CENTER);
        centerPanel.add(counterLabel, gbc);

        // Add the center panel to the main panel
        this.add(centerPanel, BorderLayout.CENTER);

        // Add action listeners for buttons
        pausePlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePausePlay();
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleHelp();
            }
        });
    }

    // Method to toggle between Pause and Play
    private void togglePausePlay() {
        if (isPaused) {
            setPauseIcon();
            // Logic to resume the game
        } else {
            setPlayIcon();
            // Logic to pause the game
        }
        isPaused = !isPaused;
    }

    private void setPlayIcon() {
        pausePlayButton.setIcon(new ImageIcon("src/Image/play.png"));
    }

    private void setPauseIcon() {
        pausePlayButton.setIcon(new ImageIcon("src/Image/pause.png"));
    }

    private void toggleHelp() {
        isHelp = !isHelp;
        if (isHelp) {
            helpButton.setBackground(Color.GREEN);
        } else {
            helpButton.setBackground(UIManager.getColor("CheckBox.background"));
        }
    }

    public void updateTimer(String time) {
        timerLabel.setText("Timer: " + time);
    }

    public void updateCounter(int moves) {
        counterLabel.setText("Moves: " + moves);
    }
}
