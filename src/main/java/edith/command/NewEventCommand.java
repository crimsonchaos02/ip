package edith.command;


import edith.body.Storage;
import edith.body.TaskList;
import edith.task.Event;
import edith.util.EdithException;


/**
 * New Event Command. Creates an Event object and adds it to the TaskList. Writes to file.
 */
public class NewEventCommand extends Command {
    private Event event;

    /**
     * Constructor of NewEventCommand.
     * @param s The appropriate Storage instance.
     * @param t The appropriate TaskList instance.
     * @param event The new Event instance.
     */

    public NewEventCommand(Storage s, TaskList t, Event event) {
        super(s, t);
        this.event = event;
    }

    @Override
    public void run() {
        tasks.addTask(this.event);
        try {
            storage.saveToFile(tasks);
        } catch (EdithException e) {
            System.out.println("An error occurred. Please check your storage filepath.");
        }
    }

    @Override
    public String getMessage() {
        return "added new event:\n"
                + this.event.toString()
                + "\nyou have " + tasks.getSize() + " tasks left";
    }
}
