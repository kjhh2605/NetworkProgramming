package server;

import server.core.GameLoop;
import server.core.GameState;
import server.handler.ClientHandler;
import server.handler.MonsterHandler;

import static java.lang.Thread.sleep;

public class GameServer {

    private static final int PORT = 12345;

    private final GameState gameState;
    private final GameLoop gameLoop;
    private final java.util.List<ClientHandler> clients;

    public GameServer() {
        gameState = new GameState();
        clients = new java.util.concurrent.CopyOnWriteArrayList<>();
        gameLoop = new GameLoop(gameState, clients);
    }

    public static void main(String[] args) {
        System.out.println("Starting Game Server on port: " + PORT);
        new GameServer().startServer(PORT);
    }

    public void startServer(int port) {
        new Thread(gameLoop).start();


        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(port)) {
            System.out.println("연결을 기다리는 중입니다...");

            while (true) {
                java.net.Socket clientSocket = serverSocket.accept();
                System.out.println("새로운 클라이언트가 연결되었습니다 : " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, gameState);
                clients.add(clientHandler);
                new Thread(clientHandler).start();

                for(int i = 0 ; i < 20 ; i++){
                    MonsterHandler monsterHandler = new MonsterHandler(gameState);
                    new Thread(monsterHandler).start();
                    sleep(100);
                }
            }


        } catch (java.io.IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
