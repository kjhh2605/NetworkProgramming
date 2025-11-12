package server;

public class MessageParser {

    public static PlayerUpdateData parsePlayerUpdate(String message) {
        try {
            String payload = message.substring(message.indexOf("payload\":{") + 10,
                message.lastIndexOf("}"));
            String[] parts = payload.split(",");
            int x = Integer.parseInt(parts[0].split(":")[1]);
            int y = Integer.parseInt(parts[1].split(":")[1]);
            String state = parts[2].split(":")[1].replace("\"", "");
            String direction = parts[3].split(":")[1].replace("\"", "");

            return new PlayerUpdateData(x, y, state, direction);
        } catch (Exception e) {
            System.err.println("Failed to parse PLAYER_UPDATE: " + message);
            return null;
        }
    }

    public static String parseSkillType(String message) {
        try {
            return message.split("\"skillType\":\"")[1].split("\"")[0];
        } catch (Exception e) {
            System.err.println("Failed to parse SKILL_USE: " + message);
            return null;
        }
    }

    public static SkillUseData parseSkillUse(String message) {
        try {
            String skillType = message.split("\"skillType\":\"")[1].split("\"")[0];
            String direction = "right"; // default
            if (message.contains("\"direction\":")) {
                direction = message.split("\"direction\":\"")[1].split("\"")[0];
            }
            return new SkillUseData(skillType, direction);
        } catch (Exception e) {
            System.err.println("Failed to parse SKILL_USE: " + message);
            return null;
        }
    }

    public static class PlayerUpdateData {
        public final int x;
        public final int y;
        public final String state;
        public final String direction;

        public PlayerUpdateData(int x, int y, String state, String direction) {
            this.x = x;
            this.y = y;
            this.state = state;
            this.direction = direction;
        }
    }

    public static class SkillUseData {
        public final String skillType;
        public final String direction;

        public SkillUseData(String skillType, String direction) {
            this.skillType = skillType;
            this.direction = direction;
        }
    }
}
