package edith;

/**
 * Represents a event on the user's task list.
 * A specific type of Task that has a start time and end time.
 */

public class Event extends Task {
    protected String start;
    protected String end;

    /**
     * Constructs a new Deadline object. Automatically assumes task is initially not done.
     *
     * @param description The event description.
     * @param start The string representation for start time of the event.
     * @param end The end representation for start time of the event.
     */

    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        String icon = this.isDone ? "X" : " ";
        return "[E][" + icon + "] " + this.description
                + " (from: " + this.start + " to: " + this.end + ")";

    }
}
