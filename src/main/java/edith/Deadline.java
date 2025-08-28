package edith;

/**
 * Represents a deadline on the user's task list.
 * A specific type of Task that has a due date.
 */

public class Deadline extends Task {
    protected String dueDate;

    /**
     * Constructs a new Deadline object. Automatically assumes task is initially not done.
     *
     * @param description The task description.
     * @param dueDate The String representation of the due date.
     */

    public Deadline(String description, String dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        String icon = this.isDone ? "X" : " ";
        return "[D][" + icon + "] " + this.description + ", due by: " + dueDate;
    }
}
