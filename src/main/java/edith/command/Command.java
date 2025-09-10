package edith.command;

import edith.body.Storage;
import edith.body.TaskList;

/**
 * Main class for Command object.
 */

public abstract class Command {
    protected Storage storage;
    protected TaskList tasks;

    /**
     * Constructor for a Command object.
     * @param s The appropriate Storage instance.
     * @param t The appropriate TaskList instance
     */

    public Command(Storage s, TaskList t) {
        this.storage = s;
        this.tasks = t;
    }

    public abstract void run();
    public abstract String getMessage();
}
