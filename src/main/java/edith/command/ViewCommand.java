package edith.command;

import java.time.LocalDateTime;

import edith.body.Storage;
import edith.body.TaskList;


/**
 * View Command class. Used to obtain tasks occurring due up to or on a certain date.
 * No further actions necessary.
 */
public class ViewCommand extends Command {
    private String decision;
    private LocalDateTime date;

    /**
     * Constructs a ViewCommand object.
     * @param s Storage file
     * @param t TaskList file
     * @param decision whether user wants to view on or up to a certain date.
     * @param date the date in question.
     */
    public ViewCommand(Storage s, TaskList t, String decision, LocalDateTime date) {
        super(s, t);
        this.decision = decision;
        this.date = date;
    }

    @Override
    public void run() {}

    @Override
    public String getMessage() {
        return tasks.viewScheduled(date, decision);
    }
}
