package edith.command;

import edith.body.Storage;
import edith.body.TaskList;
import edith.task.Task;
import edith.util.EdithException;

/**
 * Mark Command. Marks a task as done. Writes to file.
 */
public class MarkCommand extends Command {
    private Task task;

    /**
     * Constructor of MarkCommand. Returns a MarkCommand instance.
     * @param s The appropriate Storage instance.
     * @param t The appropriate TaskList instance.
     * @param i The index of the Task to mark as done.
     * @throws EdithException if index out of bounds.
     */

    public MarkCommand(Storage s, TaskList t, Integer i) throws EdithException {
        super(s, t);
        try {
            this.task = t.getTask(i);
        } catch (EdithException e) {
            throw new EdithException(e.getMessage());
        }
    }

    @Override
    public void run() {
        tasks.markDone(this.task);
        try {
            storage.saveToFile(tasks);
        } catch (EdithException e) {
            System.out.println("An error occurred. Please check your storage filepath.");
        }
    }

    @Override
    public String getMessage() {
        return "good job buddy you finished task:\n" + this.task.toString();
    }
}
