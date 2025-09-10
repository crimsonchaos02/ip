package edith.command;

import edith.body.Storage;
import edith.body.TaskList;

/**
 * Help Command. Prints the available commands to screen. No further actions required.
 */
public class HelpCommand extends Command {
    public HelpCommand(Storage s, TaskList t) {
        super(s, t);
    }

    @Override
    public void run() {}

    @Override
    public String getMessage() {
        return "commands list:"
                + "\nuse list to show your current tasks"
                + "\nuse mark i to mark task i as done"
                + "\nuse unmark i to mark task i as undone"
                + "\nuse todo to add a task"
                + "\nuse deadline to add a deadline (/by to specify due date)"
                + "\nuse event to add an event (/from and /by to specify details)"
                + "\nuse bye to exit the chatbot";
    }
}
