package edith;

/**
 * Represents a task, or a to-do item on the user's task list.
 * A Task has a description and may be marked as done.
 */

public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a new Task object. Automatically assumes task is initially not done.
     *
     * @param description The task description.
     */

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Sets isDone to be true.
     */

    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Sets isDone to be false.
     */

    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns task description.
     *
     * @return the task description
     */

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        String icon = isDone ? "X" : " ";
        return "[T][" + icon + "] " + description;
    }
}
