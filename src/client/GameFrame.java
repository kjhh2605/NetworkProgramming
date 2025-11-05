package client;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Mini MapleStory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);
        this.pack(); // Adjust frame size to fit the panel

        setLocationRelativeTo(null); // Center the window after packing
        gamePanel.requestFocusInWindow(); // Ensure the panel has focus to receive key events
    }
}
