package views;

import javax.swing.*;
import java.awt.*;

public class OptionsPanelView extends JPanel {

    private JLabel playerNameLabel;
    private JButton pausePlayButton;
    private JButton helpButton;
    private JLabel timerLabel;
    private JLabel counterLabel;

    public OptionsPanelView() {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 7), BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label playerName creation
        playerNameLabel = new JLabel("PlayerName");
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(playerNameLabel, gbc);

        // Button Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        pausePlayButton = new JButton();
        pausePlayButton.setIcon(new ImageIcon("src/Image/pause.png"));
        pausePlayButton.setPreferredSize(new Dimension(40, 40));

        helpButton = new JButton("Help");
        helpButton.setPreferredSize(new Dimension(70, 40));

        buttonsPanel.add(pausePlayButton);
        buttonsPanel.add(helpButton);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        topPanel.add(buttonsPanel, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Counter and timer panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        timerLabel = new JLabel("Timer: 00:00", SwingConstants.CENTER);
        centerPanel.add(timerLabel, gbc);

        gbc.gridy = 1;
        centerPanel.add(Box.createVerticalStrut(10), gbc);

        gbc.gridy = 2;
        counterLabel = new JLabel("Moves: 0", SwingConstants.CENTER);
        centerPanel.add(counterLabel, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void updateTimerLabel(String time) {
        timerLabel.setText("Timer: " + time);
    }

    public void updateCounterLabel(int moves) {
        counterLabel.setText("Moves: " + moves);
    }

    public JButton getPausePlayButton() {
        return pausePlayButton;
    }

    public JButton getHelpButton() {
        return helpButton;
    }

    public void setPlayerName(String name) {
        playerNameLabel.setText(name);
    }

    public void setPauseIcon() {
        pausePlayButton.setIcon(new ImageIcon("src/Image/pause.png"));
    }

    public void setPlayIcon() {
        pausePlayButton.setIcon(new ImageIcon("src/Image/play.png"));
    }

    public void toggleHelpButtonColor(boolean isHelp) {
        if (isHelp) {
            helpButton.setBackground(Color.GREEN);
        } else {
            helpButton.setBackground(UIManager.getColor("CheckBox.background"));
        }
    }
}
