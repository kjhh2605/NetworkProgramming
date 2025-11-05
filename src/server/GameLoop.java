package server;

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
        // Add a dummy monster for testing
        common.Monster slime = new common.Monster();
        slime.setId("slime1");
        slime.setType("slime");
        slime.setX(100);
        slime.setY(200);
        slime.setDirection("right");
        gameState.addMonster(slime);

        while (running) {
            updateGame();
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

    private void updateGame() {
        // Simple monster movement logic
        for (common.Monster monster : gameState.getAllMonsters()) {
            int currentX = monster.getX();
            if ("right".equals(monster.getDirection())) {
                if (currentX > 300) {
                    monster.setDirection("left");
                    monster.setX(currentX - 1);
                } else {
                    monster.setX(currentX + 1);
                }
            } else { // "left"
                if (currentX < 50) {
                    monster.setDirection("right");
                    monster.setX(currentX + 1);
                } else {
                    monster.setX(currentX - 1);
                }
            }
        }
    }

    private void broadcastState() {
        // This needs a proper JSON library like Gson, but we'll build a string for now.
        String gameStateJson = buildGameStateJson();
        for (ClientHandler client : clients) {
            client.sendMessage(gameStateJson);
        }
    }

    private String buildGameStateJson() {
        // Manual JSON building - replace with a library later
        StringBuilder sb = new StringBuilder();
        sb.append("{\"type\":\"GAME_STATE\",\"payload\":{");
        sb.append("\"players\":[");
        for (common.Player p : gameState.getAllPlayers()) {
            sb.append(String.format("{\"id\":\"%s\",\"x\":%d,\"y\":%d}", p.getId(), p.getX(), p.getY()));
            sb.append(",");
        }
        if (!gameState.getAllPlayers().isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("],\"monsters\":[");
        for (common.Monster m : gameState.getAllMonsters()) {
            sb.append(String.format("{\"id\":\"%s\",\"type\":\"%s\",\"x\":%d,\"y\":%d}", m.getId(), m.getType(), m.getX(), m.getY()));
            sb.append(",");
        }
        if (!gameState.getAllMonsters().isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]}}");
        return sb.toString();
    }

    public void stop() {
        running = false;
    }
}
