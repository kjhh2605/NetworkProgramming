package client;

import client.view.GameFrame;

import javax.swing.SwingUtilities;

public class GameClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}
