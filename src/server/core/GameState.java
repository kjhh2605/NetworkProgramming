package server.core;

import common.monster.Monster;
import common.player.Player;
import common.skills.Skill;
import server.map.GameMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {

    private final Map<String, Player> players;
    private final List<Skill> skills;
    private GameMap currentMap;

    public GameState(GameMap initialMap) {
        this.currentMap = initialMap;
        this.players = new ConcurrentHashMap<>();
        this.skills = new CopyOnWriteArrayList<>();
    }

    public void update() throws InterruptedException {
        currentMap.update();
        updateSkills();
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public void updatePlayer(String playerId, common.dto.PlayerUpdateDTO dto) {
        Player player = players.get(playerId);
        if (player != null) {
            player.setX(dto.getX());
            player.setY(dto.getY());
            player.setState(dto.getState());
            player.setDirection(dto.getDirection());
        }
    }

    public List<Player> getAllPlayers() {
        return new CopyOnWriteArrayList<>(players.values());
    }

    public List<Monster> getAllMonsters() {
        return currentMap.getMonsters();
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void removeSkill(String skillId) {
        skills.removeIf(s -> s.getId().equals(skillId));
    }

    public List<Skill> getAllSkills() {
        return this.skills;
    }

    public void updateSkills() throws InterruptedException {
        for (Skill skill : skills) {
            skill.update();
        }
        skills.removeIf(s -> !s.isActive());
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(GameMap newMap) {
        this.currentMap = newMap;
    }
}

