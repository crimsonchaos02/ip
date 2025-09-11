package edith.task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

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
        boolean isNextWeek = (dueBy.toLocalDate().toEpochDay() - now.toLocalDate().toEpochDay()) >= 7;

        String out;

        LocalDate nextSunday = now.toLocalDate()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .plusWeeks(1);
        LocalDateTime cutoff = nextSunday.plusDays(1).atStartOfDay();
        if (dueBy.isAfter(cutoff)) {
            out = dueBy.format(DateTimeFormatter.ofPattern("dd MMM yyyy HHmm"));
        } else if (now.toLocalDate().equals(dueBy.toLocalDate())) {
            out = "today " + dueTime;
        } else if (isNextWeek) {
            out = "next " + dueDay.toString().toLowerCase() + " " + dueTime;
        } else {
            out = "this " + dueDay.toString().toLowerCase() + " " + dueTime;
        }
        if (now.isAfter(dueBy)) {
            out += " (OVERDUE!!!)";
        }
        return out;
    }

    @Override
    public boolean isOn(LocalDateTime date) {
        return dueDate.toLocalDate().isEqual(date.toLocalDate());
    }

    @Override
    public boolean isBefore(LocalDateTime date) {
        return dueDate.toLocalDate().isBefore(date.toLocalDate()) || isOn(date);
    }

    @Override
    public String toString() {
        String icon = this.isDone ? "X" : " ";
        return "[D][" + icon + "] " + this.description + ", due by: " + parseDate(this.dueDate);
    }
}
