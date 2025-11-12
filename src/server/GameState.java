package server;

import common.Monster;
import common.Player;
import common.skills.Skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    private final Map<String, Player> players;
    private final List<Monster> monsters;
    private final List<Skill> skills;

    public GameState() {
        this.players = new ConcurrentHashMap<>();
        this.monsters = new CopyOnWriteArrayList<>();
        this.skills = new CopyOnWriteArrayList<>();
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
        // 모든 스킬 업데이트
        for (Skill skill : skills) {
            skill.update();
        }
        // 비활성화된 스킬 제거
        skills.removeIf(s -> !s.isActive());
    }
}

