package server;

import common.Monster;
import common.Player;
import common.dto.PlayerUpdateDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    private final Map<String, Player> players;
    private final List<Monster> monsters;

    public GameState() {
        this.players = new ConcurrentHashMap<>();
        this.monsters = new CopyOnWriteArrayList<>();
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

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public void removeMonster(String monsterId) {
        monsters.removeIf(m -> m.getId().equals(monsterId));
    }

    public List<Monster> getAllMonsters() {
        return this.monsters;
    }
}

