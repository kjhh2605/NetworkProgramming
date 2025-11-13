package server.map;

import common.monster.Monster;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class GameMap {
    private final String mapId;
    private final String backgroundImagePath;
    private final List<Monster> monsters;
    private final int maxMonsters;
    private final Supplier<Monster> monsterFactory;

    public GameMap(String mapId, String backgroundImagePath, int maxMonsters, Supplier<Monster> monsterFactory) {
        this.mapId = mapId;
        this.backgroundImagePath = backgroundImagePath;
        this.maxMonsters = maxMonsters;
        this.monsterFactory = monsterFactory;
        this.monsters = new CopyOnWriteArrayList<>();
    }

    public void update() {
        // 맵에 속한 몬스터들의 움직임 등 업데이트
        for (Monster monster : monsters) {
            monster.move();
        }

        // 몬스터 개체 수 관리
        manageMonsters();
    }

    private void manageMonsters() {
        while (monsters.size() < maxMonsters) {
            monsters.add(monsterFactory.get());
            System.out.println(mapId + "에서 몬스터 생성. 현재: " + monsters.size() + "/" + maxMonsters);
        }
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public String getMapId() {
        return mapId;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }
}
