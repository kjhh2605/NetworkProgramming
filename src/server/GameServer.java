package server;

public class GameServer {

    private final GameState gameState;
    private final GameLoop gameLoop;
    private final java.util.List<ClientHandler> clients;

    public GameServer() {
        gameState = new GameState();
        clients = new java.util.concurrent.CopyOnWriteArrayList<>();
        gameLoop = new GameLoop(gameState, clients); // Pass clients to GameLoop
    }

    public static void main(String[] args) {
        int port = 12345;
        System.out.println("Starting Mini MapleStory Server on port: " + port);
        new GameServer().startServer(port);
    }

    public void startServer(int port) {
        new Thread(gameLoop).start();

        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(port)) {
            System.out.println("Server is listening for connections...");

            while (true) {
                java.net.Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, gameState);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }

        } catch (java.io.IOException e) {
            System.err.println("Error in server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
