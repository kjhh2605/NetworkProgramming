package server.core;

import client.model.Monster;
import client.model.Player;
import common.skills.Skill;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

//todo : 맵별로 분리 -> 일단으 하나로
public class GameState {
    private final Map<String, Player> players;
    private final List<Monster> monsters;
    private final List<Skill> skills;
    private int maxMonsterCnt;
    public GameState() {
        this.players = new ConcurrentHashMap<>();
        this.monsters = new CopyOnWriteArrayList<>();
        this.skills = new CopyOnWriteArrayList<>();
        this.maxMonsterCnt = 20; // 기본값 20으로 설정
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

    public void setMaxMonsterCnt(int maxMonsterCnt) {
        this.maxMonsterCnt = maxMonsterCnt;
    }
    public int getMaxMonsterCnt(){
        return this.maxMonsterCnt;
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

