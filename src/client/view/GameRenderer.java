package client.view;

import client.util.SpriteManager;
import client.model.Monster;
import client.model.Player;
import common.skills.Skill;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

public class GameRenderer {

    public static void render(Graphics g, BufferedImage background, String errorMessage,
                             List<Monster> monsters, List<Skill> skills, List<Player> players,
                             String myPlayerId, int width, int height) {
        if (background != null) {
            g.drawImage(background, 0, 0, width, height, null);
        }

        if (errorMessage != null) {
            g.setColor(Color.RED);
            g.drawString(errorMessage, 50, 50);
            return;
        }

        renderMonsters(g, monsters);
        renderSkills(g, skills);
        renderPlayers(g, players, myPlayerId);
    }

    private static void renderMonsters(Graphics g, List<Monster> monsters) {
        Image monsterSprite = SpriteManager.getSprite("monster");
        for (Monster monster : monsters) {
            if (monsterSprite != null) {
                g.drawImage(monsterSprite, monster.getX(), monster.getY(), 30, 30, null);
            } else {
                g.setColor(Color.RED);
                g.fillRect(monster.getX(), monster.getY(), 30, 30);
            }
            g.drawString(monster.getId(), monster.getX(), monster.getY() - 5);
        }
    }

    private static void renderSkills(Graphics g, List<Skill> skills) {
        for (Skill skill : skills) {
            Image sprite = skill.getSprite();
            if (sprite != null) {
                if (common.enums.Direction.LEFT.equals(skill.getDirection())) {
                    g.drawImage(sprite, skill.getX() + skill.getWidth(), skill.getY(),
                               -skill.getWidth(), skill.getHeight(), null);
                } else {
                    g.drawImage(sprite, skill.getX(), skill.getY(),
                               skill.getWidth(), skill.getHeight(), null);
                }
            } else {
                g.setColor(Color.YELLOW);
                g.fillRect(skill.getX(), skill.getY(), skill.getWidth(), skill.getHeight());
            }
        }
    }

    private static void renderPlayers(Graphics g, List<Player> players, String myPlayerId) {
        Image playerSprite = SpriteManager.getSprite("player");
        for (Player player : players) {
            if (playerSprite != null) {
                g.drawImage(playerSprite, player.getX(), player.getY(), 30, 30, null);
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
}