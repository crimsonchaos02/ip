package edith.command;

import edith.body.Storage;
import edith.body.TaskList;
import edith.task.Task;
import edith.util.EdithException;

/**
 * New Task Command. Creates a Task object and adds it to the TaskList. Writes to file.
 */
public class NewTaskCommand extends Command {
    private Task task;
    private String dupCheck = "";

    /**
     * Constructor of NewTaskCommand.
     * @param s The appropriate Storage instance.
     * @param t The appropriate TaskList instance.
     * @param t The new Task object.
     */

    public NewTaskCommand(Storage s, TaskList t, Task task) {
        super(s, t);
        this.task = task;
    }

    @Override
    public void run() {
        this.dupCheck = tasks.searchTasks(this.task.getDescription());
        tasks.addTask(this.task);
        try {
            storage.saveToFile(tasks);
        } catch (EdithException e) {
            System.out.println("An error occurred. Please check your storage filepath.");
        }
    }

    @Override
    public String getMessage() {
        String out = "added new task:\n"
                + this.task.toString()
                + "\nyou have " + tasks.getSize() + " tasks left";
        if (!this.dupCheck.isEmpty()) {
            int index = Integer.parseInt(dupCheck.split("\\.")[0]);
            out += "\nWarning! There is already a duplicate at index " + index;
        }
        return out;
    }
}
