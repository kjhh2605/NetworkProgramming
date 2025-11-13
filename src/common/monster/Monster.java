package common.monster;

import common.enums.Direction;

import java.util.Random;

public class Monster {
    private String id;
    private String name;
    private int hp;
    private String type;
    private int x;
    private int y;
    private String state;
    private Direction direction;

    public void move() {
        // Simple monster movement logic
        int currentX = getX();
        if (Direction.RIGHT.equals(getDirection())) {
            if (currentX > 300) {
                setDirection(Direction.LEFT);
                int dist = new Random().nextInt(10) + 1;
                setX(currentX - dist);
            } else {
                setX(currentX + 1);
            }
        } else { // LEFT
            if (currentX < 50) {
                setDirection(Direction.RIGHT);
                setX(currentX + 1);
            } else {
                setX(currentX - 1);
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public int getHp() {return hp;}

    public void setHp(int hp) {this.hp = hp;}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setDirection(String direction) {
        this.direction = Direction.fromString(direction);
    }
}
