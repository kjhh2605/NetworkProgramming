package server.core;

import server.handler.ClientHandler;
import server.util.GameStateSerializer;

public class GameLoop implements Runnable {

    private final GameState gameState;
    private final java.util.List<ClientHandler> clients;
    private volatile boolean running = true;

    public GameLoop(GameState gameState, java.util.List<ClientHandler> clients) {
        this.gameState = gameState;
        this.clients = clients;
    }

    @Override
    public void run() {


        while (running) {
            try {
                updateGame();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 클라이언트에게 메세지 전송
            broadcastState();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                running = false;
                Thread.currentThread().interrupt();
                System.err.println("Game loop was interrupted.");
            }
        }
    }

    private void updateGame() throws InterruptedException {
        gameState.update();
    }

    private void broadcastState() {
        String gameStateJson = GameStateSerializer.toJson(
            gameState.getCurrentMap(),
            gameState.getAllPlayers(),
            gameState.getAllMonsters(),
            gameState.getAllSkills()
        );
        for (ClientHandler client : clients) {
            client.sendMessage(gameStateJson);
        }
    }

    public void stop() {
        running = false;
    }
}
