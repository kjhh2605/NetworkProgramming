package server.map;

import common.ImagePath;
import common.monster.GreenSlime;

public class MapCreator {
    public static GameMap Hennessis() {
        return new GameMap(
            "hennessis",
            ImagePath.HENNESSIS_IMAGE_PATH,
            10,
            GreenSlime::new
        );
    }

    public static GameMap Robby() {
        // todo : 다른 맵
        return null;
    }
}
