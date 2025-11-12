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
        slime.setDirection(common.enums.Direction.RIGHT);
        gameState.addMonster(slime);

        while (running) {
            try {
                updateGame();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
        // Simple monster movement logic
        for (common.Monster monster : gameState.getAllMonsters()) {
            int currentX = monster.getX();
            if (common.enums.Direction.RIGHT.equals(monster.getDirection())) {
                if (currentX > 300) {
                    monster.setDirection(common.enums.Direction.LEFT);
                    monster.setX(currentX - 1);
                } else {
                    monster.setX(currentX + 1);
                }
            } else { // LEFT
                if (currentX < 50) {
                    monster.setDirection(common.enums.Direction.RIGHT);
                    monster.setX(currentX + 1);
                } else {
                    monster.setX(currentX - 1);
                }
            }
        }

        // Update skills
        gameState.updateSkills();
    }

    private void broadcastState() {
        String gameStateJson = GameStateSerializer.toJson(
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
