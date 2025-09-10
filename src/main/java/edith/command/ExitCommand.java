package edith.command;

import edith.body.Storage;
import edith.body.TaskList;

/**
 * The exit command. No further actions are required.
 */

public class ExitCommand extends Command {

    public ExitCommand(Storage s, TaskList t) {
        super(s, t);
    }

    @Override
    public void run() {}

    public String getMessage() {
        return "=================================="
                + "\njiayousss bye have a great time"
                + "\n==================================";
    }
}
