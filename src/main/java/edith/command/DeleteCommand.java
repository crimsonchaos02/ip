package edith.command;

import edith.body.Storage;
import edith.body.TaskList;
import edith.task.Task;
import edith.util.EdithException;

/**
 * Unmark Command. Marks a task as done. Writes to file.
 */
public class DeleteCommand extends Command {
    private Task task;
    private Integer index;

    /**
     * Constructor of DeleteCommand.
     * @param s The appropriate Storage instance.
     * @param t The appropriate TaskList instance.
     * @param i The index of the Task to mark as done.
     * @throws EdithException if index out of bounds.
     */

    public DeleteCommand(Storage s, TaskList t, Integer i) throws EdithException {
        super(s, t);
        this.index = i;
        try {
            this.task = t.getTask(i);
        } catch (EdithException e) {
            throw new EdithException(e.getMessage());
        }
    }

    @Override
    public void run() {
        tasks.removeTask(this.index);
        try {
            storage.saveToFile(tasks);
        } catch (EdithException e) {
            System.out.println("An error occurred. Please check your storage filepath.");
        }
    }

    @Override
    public String getMessage() {
        return "okay we removed:\n"
                + this.task.toString()
                + "\nyou have " + tasks.getSize() + " tasks left";
    }
}
