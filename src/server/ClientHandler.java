package server;

import common.dto.PlayerUpdateDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final GameState gameState;
    private PrintWriter out;
    private BufferedReader in;
    private String playerId;

    public ClientHandler(Socket socket, GameState gameState) {
        this.clientSocket = socket;
        this.gameState = gameState;
        this.playerId = "player_" + Integer.toHexString(hashCode());
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            common.Player newPlayer = new common.Player();
            newPlayer.setId(this.playerId);
            newPlayer.setX(50); // Default starting position
            newPlayer.setY(200);
            gameState.addPlayer(newPlayer);
            System.out.println("Player " + this.playerId + " connected.");

            // Send a welcome message to the client with their new ID
            String welcomeMessage = String.format("{\"type\":\"WELCOME\",\"payload\":{\"id\":\"%s\"}}", this.playerId);
            sendMessage(welcomeMessage);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from " + playerId + ": " + inputLine);
                if (inputLine.contains("\"type\":\"PLAYER_UPDATE\"")) {
                    try {
                        String payload = inputLine.substring(inputLine.indexOf("payload\":{") + 10, inputLine.lastIndexOf("}"));
                        String[] parts = payload.split(",");
                        int x = Integer.parseInt(parts[0].split(":")[1]);
                        int y = Integer.parseInt(parts[1].split(":")[1]);
                        String state = parts[2].split(":")[1].replace("\"", "");
                        String direction = parts[3].split(":")[1].replace("\"", "");

                        common.dto.PlayerUpdateDTO dto = new common.dto.PlayerUpdateDTO();
                        dto.setX(x);
                        dto.setY(y);
                        dto.setState(state);
                        dto.setDirection(direction);
                        gameState.updatePlayer(playerId, dto);
                    } catch (Exception e) {
                        System.err.println("Failed to parse PLAYER_UPDATE: " + inputLine);
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Player " + playerId + " disconnected.");
        } finally {
            gameState.removePlayer(this.playerId);
            closeResources();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
