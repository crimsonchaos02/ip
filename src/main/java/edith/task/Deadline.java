package edith.task;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline on the user's task list.
 * A specific type of Task that has a due date.
 */

public class Deadline extends Task {
    protected LocalDateTime dueDate;

    /**
     * Constructs a new Deadline object. Automatically assumes task is initially not done.
     *
     * @param description The task description.
     * @param dueDate The String representation of the due date.
     */

    public Deadline(String description, LocalDateTime dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    /**
     * Returns string representation of due date.
     *
     * @param dueBy LocalDateTime object indicating the deadline.
     * @return Appropriate string representation.
     */

    public String parseDate(LocalDateTime dueBy) {
        LocalDateTime now = LocalDateTime.now();
        String dueTime = dueBy.toLocalTime().format(DateTimeFormatter.ofPattern("HHmm"));

        DayOfWeek today = now.getDayOfWeek();

        DayOfWeek dueDay = dueBy.getDayOfWeek();
        String out;
        if (now.toLocalDate().equals(dueBy.toLocalDate())) {
            out = "today " + dueTime;
        } else if (dueDay.getValue() <= today.getValue()) {
            out = "next " + dueDay.toString().toLowerCase() + " " + dueTime;
        } else {
            out = dueDay.toString().toLowerCase() + " " + dueTime;
        }
        return out;

    }

    @Override
    public String toString() {
        String icon = this.isDone ? "X" : " ";
        return "[D][" + icon + "] " + this.description + ", due by: " + parseDate(this.dueDate);
    }
}
