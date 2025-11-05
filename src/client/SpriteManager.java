package client;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpriteManager {

    private static final String PLAYER_IMAGE_PATH = "../img/character.jpeg";
    private static final String MONSTER_IMAGE_PATH = "../img/monster.gif";

    private static final Map<String, Image> sprites = new HashMap<>();

    public static void loadSprites() {
        try {
            sprites.put("player", ImageIO.read(new File(PLAYER_IMAGE_PATH)));
            sprites.put("monster", new ImageIcon(MONSTER_IMAGE_PATH).getImage());
        } catch (IOException e) {
            System.err.println("Failed to load sprites.");
            e.printStackTrace();
        }
    }

    public static Image getSprite(String name) {
        return sprites.get(name);
    }
}