package common.dto;

import common.enums.Direction;

public class SkillDTO {
    private String id;
    private String playerId;
    private String type;
    private int x;
    private int y;
    private Direction direction;
    private boolean active;

    public SkillDTO() {}

    public SkillDTO(String id, String playerId, String type, int x, int y, Direction direction, boolean active) {
        this.id = id;
        this.playerId = playerId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setDirection(String direction) {
        this.direction = Direction.fromString(direction);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
