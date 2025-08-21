public enum Command {
    LIST, CMDS, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE;

    public static Command fromString(String s) throws EdithException {
        switch (s) {
            case "bye": return BYE;
            case "list": return LIST;
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
