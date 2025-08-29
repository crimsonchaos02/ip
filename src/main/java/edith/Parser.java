package edith;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

/**
 * This class parses user input.
 */

public class Parser {

    /**
     * Returns a Command object from user input.
     *
     * @param s Command string input by user.
     * @return Corresponding Command object.
     */

    public static Command fromString(String s) {
        return switch (s) {
            case "bye" -> Command.BYE;
            case "list", "ls" -> Command.LIST;
            case "cmd" -> Command.CMDS;
            case "mark" -> Command.MARK;
            case "unmark" -> Command.UNMARK;
            case "todo" -> Command.TODO;
            case "deadline" -> Command.DEADLINE;
            case "event" -> Command.EVENT;
            case "delete", "del" -> Command.DELETE;
            case "find" -> Command.FIND;
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
     * Returns a LocalDateTime object from user input.
     *
     * @param s User input. Can either be relative (limited to "this" or "next")
     *          or "dd/mm/yyyy/HHmm". Time follows 24-hour time format.
     * @return Corresponding LocalDateTime object.
     * @throws EdithException if format is not followed
     */

    public static LocalDateTime parseDateTime(String s) throws EdithException {
        String ERROR_MESSAGE = "syntax error. please use EITHER 'dd/MM/yyyy/HHmm' " +
                "with optional 24-hour time (HHmm), \n" +
                "OR 'this'/'next' followed by a day of the week, optionally with time (HHmm).\n" +
                "If omitted, time will be set to a default of noon.";

        String[] relative = s.split(" ");
        String[] dateTime = s.split("[/T:]");

        if (relative[0].equals("this")) {
            DayOfWeek day = parseDay(relative[1]);
            if (day == null) {
                throw new EdithException(ERROR_MESSAGE);
            }
            LocalTime time = LocalTime.of(12, 0);
            if (relative.length == 3) {
                try {
                    time = LocalTime.parse(relative[2], DateTimeFormatter.ofPattern("HHmm"));
                } catch (DateTimeParseException e) {
                    throw new EdithException("boi please check your date time format");
                }
            }
            LocalDateTime now = LocalDateTime.now();
            return now.with(TemporalAdjusters.nextOrSame(day)).with(time);

        } else if (relative[0].equals("next")) {
            DayOfWeek day = parseDay(relative[1]);
            if (day == null) {
                throw new EdithException(ERROR_MESSAGE);
            }
            LocalTime time = LocalTime.of(12, 0);
            if (relative.length == 3) {
                try {
                    time = LocalTime.parse(relative[2], DateTimeFormatter.ofPattern("HHmm"));
                } catch (DateTimeParseException e) {
                    throw new EdithException("boi please check your date time format");
                }
            }
            LocalDateTime now = LocalDateTime.now();
            return now.with(TemporalAdjusters.next(day)).with(time);

        } else if (dateTime.length == 3) {
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
     * Returns a Task object from a string. Used for reading saved task lists from external files.
     *
     * @param s A String representing a single task.
     * @return A corresponding Task object.
     */

    public static Task parseTask(String s) throws EdithException {
        Task out;
        char type = s.charAt(4);
        char done = s.charAt(7);

        if (type == 'T') {
            String desc = s.substring(10);
            out =  new Task(desc);
        } else if (type == 'D') {
            String[] tmp = s.split(", due by: ");
            String dueDate = tmp[1];
            if (dueDate.split(" ").length == 2) {
                dueDate = "this " + dueDate;
            }
            LocalDateTime due = parseDateTime(dueDate);
            String desc = tmp[0].substring(10);
            out = new Deadline(desc, due);

        } else {
            String[] tmp = s.split("from: | to: " );
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
            String desc = s.substring(10, s.indexOf('(')-1);
            out = new Event(desc, from, to);
        }
        if (done == 'X') {
            out.markAsDone();
        }
        return out;
    }

    /**
     * Returns a Task object from user input. Only called if user wishes to input tasks.
     *
     * @param c Command object indicating which type of task to be input.
     * @param inp User input with relevant details.
     * @return New corresponding Task object.
     * @throws EdithException if there are formatting errors in the user input.
     */

    public static Task parseTaskInput(Command c, String inp) throws EdithException {
        if (c == Command.TODO) {
            String[] inps = inp.split(" ");
            if (inps.length == 1) {
                throw new EdithException("please include a task description");
            }
            String description = String.join(" ",
                    Arrays.copyOfRange(inps, 1, inps.length));
            return new Task(description);
        } else if (c == Command.DEADLINE) {
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

            return new Deadline(description, dueDate);
        } else {
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
            return new Event(description, start, end);
        }
    }


}
