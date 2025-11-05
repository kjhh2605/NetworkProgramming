package common.dto;

import common.Player;
import common.Monster;

import java.util.List;

public class GameStateDTO {
    private List<Player> players;
    private List<Monster> monsters;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }
}
