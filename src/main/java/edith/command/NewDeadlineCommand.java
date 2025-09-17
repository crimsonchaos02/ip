package edith.command;

import edith.body.Storage;
import edith.body.TaskList;
import edith.task.Deadline;
import edith.util.EdithException;


/**
 * New Deadline Command. Creates a Deadline object and adds it to the TaskList. Writes to file.
 */
public class NewDeadlineCommand extends Command {
    private Deadline deadline;

    /**
     * Constructor of NewDeadlineCommand.
     * @param s The appropriate Storage instance.
     * @param t The appropriate TaskList instance.
     * @param d The new Deadline instance.
     */

    public NewDeadlineCommand(Storage s, TaskList t, Deadline d) {
        super(s, t);
        this.deadline = d;
    }

    @Override
    public void run() {
        tasks.addTask(this.deadline);
        try {
            storage.saveToFile(tasks);
        } catch (EdithException e) {
            System.out.println("An error occurred. Please check your storage filepath.");
        }
    }

    @Override
    public String getMessage() {
        String out = "added new deadline:\n"
                + this.deadline.toString()
                + "\nyou have " + tasks.getSize() + " tasks left";

        String dupCheck = tasks.searchTasks(this.deadline.getDescription());
        if (!dupCheck.isEmpty()) {
            int index = Integer.parseInt(dupCheck.split("\\.")[0]);
            out += "\nWarning! There is already a duplicate at index " + index;
        }
        return out;
    }
}
