package edith;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event on the user's task list.
 * A specific type of Task that has a start time and end time.
 */

public class Event extends Task {
    protected LocalDateTime start;
    protected LocalDateTime end;

    /**
     * Constructs a new Deadline object. Automatically assumes task is initially not done.
     *
     * @param description The event description.
     * @param start The string representation for start time of the event.
     * @param end The end representation for start time of the event.
     */

    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns an appropriate string representation of the start and end times of the event.
     *
     * @param start LocalDateTime object representing event start.
     * @param end LocalDateTime object representing event end.
     * @return Appropriate string representation.
     */

    public String convertToString(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now();
        String startTime = start.toLocalTime().format(DateTimeFormatter.ofPattern("HHmm"));
        String endTime = end.toLocalTime().format(DateTimeFormatter.ofPattern("HHmm"));

        DayOfWeek today = now.getDayOfWeek();

        DayOfWeek from = start.getDayOfWeek();
        String fromStr;
        if (now.toLocalDate().equals(start.toLocalDate())) {
            fromStr = "today " + startTime;
        } else if (from.getValue() <= today.getValue()) {
            fromStr = "next " + from.toString().toLowerCase() + " " + startTime;
        } else {
            fromStr = from.toString().toLowerCase() + " " + startTime;
        }

        String toStr;
        if (start.toLocalDate().equals(end.toLocalDate())) {
            toStr = endTime;
        } else {
            toStr = end.getDayOfWeek().toString().toLowerCase() + " " + endTime;
        }
        return " (from: " + fromStr + " to: " + toStr + ")";
    }

    @Override
    public String toString() {
        String icon = this.isDone ? "X" : " ";
        return "[E][" + icon + "] " + this.description + convertToString(this.start, this.end);
    }
}
