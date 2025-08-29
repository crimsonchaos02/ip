/**
 * Representing the possible commands that users can input to Edith.
 */

public enum Command {
    LIST, CMDS, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE;

    /**
     * Returns a Command object from user input.
     *
     * @param s Command string input by user.
     * @return Corresponding Command object.
     */

    public static Command fromString(String s) {
        switch (s) {
            case "bye": return BYE;
            case "list": case "ls": return LIST;
            case "cmd": return CMDS;
            case "mark": return MARK;
            case "unmark": return UNMARK;
            case "todo": return TODO;
            case "deadline": return DEADLINE;
            case "event": return EVENT;
            case "delete": case "del": return DELETE;
            default: return null;
        }
    }
}
