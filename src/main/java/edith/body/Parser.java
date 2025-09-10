package edith.body;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

import edith.task.Deadline;
import edith.task.Event;
import edith.task.Task;
import edith.util.CommandType;
import edith.util.EdithException;

/**
 * This class parses user input.
 */

public class Parser {

    /**
     * Returns a CommandType object from user input.
     *
     * @param s CommandType string input by user.
     * @return Corresponding CommandType object.
     */
    public static CommandType getCommandTypeFromString(String s) {
        //CHECKSTYLE.OFF: Indentation
        return switch (s) {
            case "bye" -> CommandType.BYE;
            case "list", "ls" -> CommandType.LIST;
            case "help" -> CommandType.HELP;
            case "mark" -> CommandType.MARK;
            case "unmark" -> CommandType.UNMARK;
            case "todo" -> CommandType.TODO;
            case "deadline" -> CommandType.DEADLINE;
            case "event" -> CommandType.EVENT;
            case "delete", "del" -> CommandType.DELETE;
            case "find" -> CommandType.FIND;
            default -> null;
        };
    }

    /**
     * Returns DayOfWeek object given an input string. Helper function for parseDateTime
     *
     * @param s Input string. Allows sensible abbreviations.
     * @return Corresponding DayOfWeek object.
     */

    public static DayOfWeek parseDay(String s) {
        if (s == null) {
            return null;
        }
        return switch (s.trim().toUpperCase()) {
            case "MONDAY", "MON", "MOND" -> DayOfWeek.MONDAY;
            case "TUESDAY", "TUE", "TUES" -> DayOfWeek.TUESDAY;
            case "WEDNESDAY", "WEDS", "WED" -> DayOfWeek.WEDNESDAY;
            case "THURSDAY", "THURS", "THU", "THUR" -> DayOfWeek.THURSDAY;
            case "FRIDAY", "FRI" -> DayOfWeek.FRIDAY;
            case "SATURDAY", "SAT" -> DayOfWeek.SATURDAY;
            case "SUNDAY", "SUN" -> DayOfWeek.SUNDAY;
            default -> null;
        };
    }

    /**
     * Helper method for parseDateTime. Handles the "relative" date case.
     * @param s input String.
     * @return Corresponding date time.
     * @throws EdithException if format is wrong.
     */
    public static LocalDateTime parseDateTimeRelative(String s) throws EdithException {
        //CHECKSTYLE.OFF: AbbreviationAsWordInName
        //CHECKSTYLE.OFF: LocalVariableName
        String ERROR_MESSAGE = "syntax error. please use 'this'/'next' followed by a day of the week, "
                + "optionally with time (HHmm)."
                + "\n If omitted, time will be set to a default of noon.";
        String[] strList = s.split(" ");

        if (strList[0].equals("this")) {
            DayOfWeek day = parseDay(strList[1]);
            if (day == null) {
                throw new EdithException(ERROR_MESSAGE);
            }
            LocalTime time = LocalTime.of(12, 0);
            if (strList.length == 3) {
                try {
                    time = LocalTime.parse(strList[2], DateTimeFormatter.ofPattern("HHmm"));
                } catch (DateTimeParseException e) {
                    throw new EdithException("boi please check your date time format");
                }
            }
            LocalDateTime now = LocalDateTime.now();
            return now.with(TemporalAdjusters.nextOrSame(day)).with(time);

        } else if (strList[0].equals("next")) {
            DayOfWeek day = parseDay(strList[1]);
            if (day == null) {
                throw new EdithException(ERROR_MESSAGE);
            }
            LocalTime time = LocalTime.of(12, 0);
            if (strList.length == 3) {
                try {
                    time = LocalTime.parse(strList[2], DateTimeFormatter.ofPattern("HHmm"));
                } catch (DateTimeParseException e) {
                    throw new EdithException("boi please check your date time format");
                }
            }
            LocalDateTime now = LocalDateTime.now();
            return now.with(TemporalAdjusters.next(day)).with(time);
        } else {
            throw new EdithException(ERROR_MESSAGE);
        }
    }

    /**
     * Helper function for parseDateTime. Handles the case where precise Date and Time is entered.
     * @param s input String.
     * @return appropriate DateTime object.
     * @throws EdithException if input format is wrong.
     */
    public static LocalDateTime parseDateTimeFormatted(String s) throws EdithException {
        String[] dateTime = s.split("[/T:]");
        String ERROR_MESSAGE = "syntax error. please use 'dd/MM/yyyy/HHmm' with optional 24-hour time (HHmm). ";
        if (dateTime.length == 3) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                return LocalDateTime.parse(s, format);
            } catch (DateTimeParseException e) {
                throw new EdithException(ERROR_MESSAGE);
            }
        } else if (dateTime.length == 4) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy/HHmm");
            try {
                return LocalDateTime.parse(s, format);
            } catch (DateTimeParseException e) {
                throw new EdithException(ERROR_MESSAGE);
            }
        } else {
            throw new EdithException(ERROR_MESSAGE);
        }
    }

    /**
     * Returns a LocalDateTime object from user input.
     *
     * @param s User input. Can either be relative (limited to "this" or "next")
     *          or "dd/mm/yyyy/HHmm". Time follows 24-hour time format.
     * @return Corresponding LocalDateTime object.
     * @throws EdithException if format is not followed
     */

    public static LocalDateTime parseDateTime(String s) throws EdithException {
        String[] relative = s.split(" ");
        String[] dateTime = s.split("[/T:]");
        assert (relative.length > 1 || dateTime.length > 1) : "error in DateTime input!";

        try {
            if (relative.length > 1) {
                return parseDateTimeRelative(s);
            } else {
                return parseDateTimeFormatted(s);
            }
        } catch (EdithException e) {
            throw new EdithException(e.getMessage());
        }
    }

    /**
     * Helper function for parseTaskFromFile. Handles Task types.
     * @param s user input.
     * @return new Task object.
     */

    public static Task parseTodo(String s) {
        String desc = s.split("\\] ")[1];
        return new Task(desc);
    }

    /**
     * Helper function for parseTaskFromFile. Handles Deadline types.
     * @param s user input.
     * @return new Deadline object.
     * @throws EdithException if input format is wrong.
     */
    public static Deadline parseDeadline(String s) throws EdithException {
        String[] tmp = s.split(", due by: ");
        assert tmp.length == 2 : "error in due date format";
        String dueDate = tmp[1];
        if (dueDate.split(" ")[0].equals("today")) {
            String day = LocalDateTime.now().getDayOfWeek().toString();
            dueDate = "this " + day + dueDate.substring(5);
        }
        if (dueDate.split(" ").length == 2) {
            dueDate = "this " + dueDate;
        }
        try {
            LocalDateTime due = parseDateTime(dueDate);
            String desc = tmp[0].split("\\] ")[1];
            return new Deadline(desc, due);
        } catch (EdithException e) {
            throw new EdithException(e.getMessage());
        }
    }

    /**
     * Helper function for parseTaskFromFile. Handles Event types.
     * @param s user input.
     * @return new Event object.
     * @throws EdithException if input format is wrong.
     */

    public static Event parseEvent(String s) throws EdithException {
        String[] tmp = s.split("from: | to: ");
        assert tmp.length >= 2 : "error: from/to input String is not in the right format";
        LocalDateTime from = parseDateTime(tmp[1]);

        String[] endParser = tmp[2].split(" ");
        LocalDateTime to;
        if (endParser.length == 1) {
            try {
                to = from.with(LocalTime.parse(tmp[2], DateTimeFormatter.ofPattern("HHmm")));
            } catch (DateTimeParseException e) {
                throw new EdithException("please use HHmm format for your times");
            }
        } else if (endParser.length == 2) {
            to = parseDateTime("this " + tmp[2]);
        } else if (endParser.length == 3) {
            to = parseDateTime(tmp[2]);
        } else {
            throw new EdithException("please fix your end time format");
        }
        String desc = s.split("\\] ")[1].substring(0, s.split("\\] ")[1].indexOf('(') - 1);
        return new Event(desc, from, to);
    }

    /**
     * Returns a Task object from a string. Used for reading saved task lists from external files.
     *
     * @param s A String representing a single task.
     * @return A corresponding Task object.
     */

    public static Task parseTaskFromFile(String s) throws EdithException {
        Task out;
        char type = s.split("\\. ")[1].charAt(1);
        char done = s.split("\\. ")[1].charAt(4);

        assert (type == 'T' || type == 'D' || type == 'E') : "error: Task String error";

        if (type == 'T') {
            out = parseTodo(s);
        } else if (type == 'D') {
            out = parseDeadline(s);
        } else {
            out = parseEvent(s);
        }

        if (done == 'X') {
            out.markAsDone();
        }
        return out;
    }

    /**
     * Helper function for parseTaskFromInput. Handles regular Tasks.
     * @param inp user input.
     * @return appropriate readable string.
     * @throws EdithException if format is wrong.
     */
    public static String parseTodoFromInput(String inp) throws EdithException {
        String[] inps = inp.split(" ");
        if (inps.length == 1) {
            throw new EdithException("please include a task description");
        }
        return String.join(" ", Arrays.copyOfRange(inps, 1, inps.length));
    }

    /**
     * Helper function for parseTaskFromInput. Handles Deadline tasks.
     * @param inp user input.
     * @return appropriate readable string
     * @throws EdithException if format is wrong.
     */
    public static String parseDeadlineFromInput(String inp) throws EdithException {
        String[] tmp = inp.split(" /by ");
        if (tmp.length == 1) {
            throw new EdithException("woi please use '/by' indicating the deadline");
        }
        if (tmp[0].split(" ").length == 1) {
            throw new EdithException("woi include a task description");
        }
        String description = String.join(" ",
                Arrays.copyOfRange(tmp[0].split(" "), 1, (tmp[0].split(" ").length)));
        LocalDateTime dueDate = parseDateTime(tmp[1]);

        return description + " " + dueDate.toString();
    }

    /**
     * Helper function for parseTaskFromInput. Handles Event tasks.
     * @param inp user input.
     * @return appropriate readable string
     * @throws EdithException if format is wrong.
     */
    public static String parseEventFromInput(String inp) throws EdithException {
        String[] tmp = inp.split(" /from | /to ");
        if (tmp.length == 1) {
            throw new EdithException("use '/from' and '/to' indicating event period");
        }

        if (tmp[0].split(" ").length == 1) {
            throw new EdithException("include a task description");
        }

        String description = String.join(" ",
                Arrays.copyOfRange(tmp[0].split(" "), 1, (tmp[0].split(" ").length)));

        LocalDateTime start = Parser.parseDateTime(tmp[1]);

        String[] endParser = tmp[2].split(" ");
        LocalDateTime end;
        if (endParser.length == 1) {
            try {
                end = start.with(LocalTime.parse(tmp[2], DateTimeFormatter.ofPattern("HHmm")));
            } catch (DateTimeParseException e) {
                throw new EdithException("please use HHmm format for your times");
            }
        } else if (endParser.length == 2) {
            end = Parser.parseDateTime("this " + tmp[2]);
        } else if (endParser.length == 3) {
            end = Parser.parseDateTime(tmp[2]);
        } else {
            throw new EdithException("please fix your end time format");
        }
        return description + " " + start.toString() + " " + end.toString();
    }
    /**
     * Used to obtain readable Task details from user input. Helper function for parseInput. Only
     * applies to creation of new Tasks.
     *
     * @param c CommandType object indicating which type of task to be input.
     * @param inp User input with relevant details.
     * @return Appropriate String representation.
     * @throws EdithException if there are formatting errors in the user input.
     */

    public static String parseTaskFromInput(CommandType c, String inp) throws EdithException {
        assert c == CommandType.TODO || c == CommandType.DEADLINE || c == CommandType.EVENT : "wrong usage of method!";
        if (c == CommandType.TODO) {
            return parseTodoFromInput(inp);
        } else if (c == CommandType.DEADLINE) {
            return parseDeadlineFromInput(inp);
        } else {
            return parseEventFromInput(inp);
        }
    }

    /**
     * Parses user input into a readable format for Logic class to handle.
     * @param inp user input
     * @return a String representation of user input such that it can be parsed into a new Command.
     * @throws EdithException if there is input format issue.
     */
    public static String parseInput(String inp) throws EdithException {
        String[] inps = inp.split(" ");
        CommandType cmd = Parser.getCommandTypeFromString(inps[0]);
        if (cmd == null) {
            throw new EdithException("please enter a valid command (type 'help' to see all commands)");
        }

        StringBuilder out = new StringBuilder();
        out.append(inps[0]);

        if (cmd == CommandType.MARK || cmd == CommandType.UNMARK || cmd == CommandType.DELETE) {
            if (inps.length < 2) {
                throw new EdithException("please enter a valid task index");
            }
            if (!inps[1].matches("-?\\d+")) {
                throw new EdithException("please enter a valid integer task index");
            }
            out.append(inps[1]);
        }

        if (cmd == CommandType.TODO || cmd == CommandType.DEADLINE || cmd == CommandType.EVENT) {
            out.append(parseTaskFromInput(cmd, inp));
        }

        if (cmd == CommandType.FIND) {
            if (inps.length == 1) {
                throw new EdithException("please enter valid keywords to search");
            }
            out.append(String.join(" ",
                    Arrays.copyOfRange(inps, 1, inps.length)));
        }
        return out.toString();
    }

}
