package common.enums;

public enum Direction {
    LEFT("left"),
    RIGHT("right"),
    UP("up"),
    DOWN("down");

    private final String value;

    Direction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Direction fromString(String text) {
        if (text == null) return RIGHT; // default
        for (Direction d : Direction.values()) {
            if (d.value.equalsIgnoreCase(text)) {
                return d;
            }
        }
        return RIGHT; // default fallback
    }

    @Override
    public String toString() {
        return value;
    }
}
