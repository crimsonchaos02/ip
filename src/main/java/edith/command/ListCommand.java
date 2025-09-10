package edith.command;

import edith.body.Storage;
import edith.body.TaskList;

/**
 * The List Command. Prints the current task list. No further actions.
 */

public class ListCommand extends Command {

    public ListCommand(Storage s, TaskList t) {
        super(s, t);
    }

    @Override
    public void run() {}

    @Override
    public String getMessage() {
        return tasks.toString();
    }

}
