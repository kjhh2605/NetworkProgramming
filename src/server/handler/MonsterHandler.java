package server.handler;

import client.model.Monster;
import server.core.GameState;

public class MonsterHandler implements Runnable{
    private final GameState gameState;

    public MonsterHandler(GameState gameState) {
        this.gameState = gameState;
    }
    @Override
    public void run() {
        // Add a dummy monster for testing
        if(gameState.getAllMonsters().size() >= gameState.getMaxMonsterCnt()){
            return;
        }

        Monster slime = new Monster();
        slime.setId("slime" + gameState.getAllMonsters().size());
        slime.setType("slime");
        slime.setX(100);
        slime.setY(200);
        slime.setDirection(common.enums.Direction.RIGHT);
        gameState.addMonster(slime);
        System.out.println("Monster added: " + slime.getId() + " at (" + slime.getX() + ", " + slime.getY() + ")");
    }


}
