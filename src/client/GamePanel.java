package client;

import common.Monster;
import common.Player;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePanel extends JPanel implements KeyListener {

    private NetworkHandler networkHandler;
    private String errorMessage = null;
    private String myPlayerId = null; // Will be set by the WELCOME message

    // Game state
    private final List<Player> players = new CopyOnWriteArrayList<>();
    private final List<Monster> monsters = new CopyOnWriteArrayList<>();
    private BufferedImage background;

    // Player physics
    private double velocityY = 0;
    private boolean isJumping = false;
    private static final double GRAVITY = 0.5;
    private static final double JUMP_STRENGTH = -12.0;
    private static final int GROUND_Y = 500; // Assuming ground is at Y=500

    private static final String BACKGROUND_IMAGE_PATH = "../img/헤네시스.png";

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        addKeyListener(this);

        try {
            background = ImageIO.read(new File(BACKGROUND_IMAGE_PATH));
            SpriteManager.loadSprites();
            networkHandler = new NetworkHandler(this, "localhost", 12345);
            new Thread(networkHandler).start();
        } catch (Exception e) {
            showError("Failed to connect to the server or load assets.");
            e.printStackTrace();
        }

        // Client-side game loop
        Timer timer = new Timer(16, e -> update()); // Approx 60 FPS
        timer.start();
    }

    private void update() {
        Player myPlayer = getMyPlayer();
        if (myPlayer == null) return;

        if (isJumping) {
            velocityY += GRAVITY;
            int newY = myPlayer.getY() + (int) velocityY;

            if (newY >= GROUND_Y) {
                newY = GROUND_Y;
                isJumping = false;
                velocityY = 0;
            }
            myPlayer.setY(newY);
            sendPlayerUpdate();
        }
        repaint();
    }

    private Player getMyPlayer() {
        if (myPlayerId == null) return null;
        return players.stream().filter(p -> p.getId().equals(myPlayerId)).findFirst().orElse(null);
    }

    public void setMyPlayerId(String id) {
        this.myPlayerId = id;
    }

    public void updateGameState(String jsonState) {
        try {
            if (jsonState.contains("\"players\":[")) {
                players.clear();
                String playersJson = jsonState.split("\"players\":\\[")[1].split("\\]")[0];
                if (!playersJson.isEmpty()) {
                    for (String playerStr : playersJson.split("\\},\\{")) {
                        Player p = new Player();
                        // Simplified parsing, assumes fields are in order
                        String id = playerStr.split("\"id\":\"")[1].split("\"")[0];
                        int x = Integer.parseInt(playerStr.split("\"x\":")[1].split(",")[0]);
                        int y = Integer.parseInt(playerStr.split("\"y\":")[1].split("}")[0]);
                        p.setId(id);
                        p.setX(x);
                        p.setY(y);
                        players.add(p);
                    }
                }
            }

            if (jsonState.contains("\"monsters\":[")) {
                monsters.clear();
                String monstersJson = jsonState.split("\"monsters\":\\[")[1].split("\\]")[0];
                if (!monstersJson.isEmpty()) {
                    for (String monsterStr : monstersJson.split("\\},\\{")) {
                        Monster m = new Monster();
                        String id = monsterStr.split("\"id\":\"")[1].split("\"")[0];
                        int x = Integer.parseInt(monsterStr.split("\"x\":")[1].split(",")[0]);
                        int y = Integer.parseInt(monsterStr.split("\"y\":")[1].split("}")[0]);
                        m.setId(id);
                        m.setX(x);
                        m.setY(y);
                        monsters.add(m);
                    }
                }
            }
            this.errorMessage = null;
        } catch (Exception e) {
            System.err.println("Failed to parse game state: " + jsonState);
        }
    }

    public void showError(String message) {
        this.errorMessage = message;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        if (errorMessage != null) {
            g.setColor(Color.RED);
            g.drawString(errorMessage, 50, 50);
            return;
        }

        Image monsterSprite = SpriteManager.getSprite("monster");
        for (Monster monster : monsters) {
            if (monsterSprite != null) {
                g.drawImage(monsterSprite, monster.getX(), monster.getY(), 30, 30, this);
            } else {
                g.setColor(Color.RED);
                g.fillRect(monster.getX(), monster.getY(), 30, 30);
            }
            g.drawString(monster.getId(), monster.getX(), monster.getY() - 5);
        }

        Image playerSprite = SpriteManager.getSprite("player");
        for (Player player : players) {
            if (playerSprite != null) {
                g.drawImage(playerSprite, player.getX(), player.getY(), 30, 30, this);
            } else {
                g.setColor(Color.BLUE);
                g.fillRect(player.getX(), player.getY(), 30, 30);
            }

            if (player.getId().equals(myPlayerId)) {
                g.setColor(Color.GREEN);
                g.drawRect(player.getX() - 2, player.getY() - 2, 34, 34);
            }
            g.drawString(player.getId(), player.getX(), player.getY() - 5);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (myPlayerId == null) return;
        Player myPlayer = players.stream().filter(p -> p.getId().equals(myPlayerId)).findFirst().orElse(null);
        if (myPlayer == null) return;

        int x = myPlayer.getX();
        int y = myPlayer.getY();
        String direction = myPlayer.getDirection() != null ? myPlayer.getDirection() : "right";
        String state = "idle";

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                x -= 5;
                direction = "left";
                state = "move";
                break;
            case KeyEvent.VK_RIGHT:
                x += 5;
                direction = "right";
                state = "move";
                break;
            case KeyEvent.VK_UP:
                //todo:포탈
                break;
            case KeyEvent.VK_SPACE:
                if (!isJumping) {
                    isJumping = true;
                    velocityY = JUMP_STRENGTH;
                    state = "jump";
                }
                break;
        }

        myPlayer.setX(x);
        myPlayer.setY(y);
        myPlayer.setDirection(direction);
        myPlayer.setState(state);
        repaint();

        sendPlayerUpdate();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (myPlayerId == null) return;
        Player myPlayer = getMyPlayer();
        if (myPlayer == null) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                // Stop moving when key is released
                myPlayer.setState("idle");
                sendPlayerUpdate(); // Inform the server about the state change
                break;
        }
    }

    private void sendPlayerUpdate() {
        Player myPlayer = getMyPlayer();
        if (myPlayer == null) return;

        String updateMsg = String.format(
            "{\"type\":\"PLAYER_UPDATE\",\"payload\":{\"x\":%d,\"y\":%d,\"state\":\"%s\",\"direction\":\"%s\"}}",
            myPlayer.getX(), myPlayer.getY(), myPlayer.getState(), myPlayer.getDirection()
        );
        networkHandler.sendMessage(updateMsg);
    }}
