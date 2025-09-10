package edith.command;

import edith.body.Storage;
import edith.body.TaskList;

/**
 * Find Command. Prints the corresponding tasks to screen. No further actions required.
 */
public class FindCommand extends Command {
    private String keyWord;

    /**
     * Constructs a new FindCommand instance.
     * @param s The appropriate Storage instance.
     * @param t The appropriate TaskList instance.
     * @param keyWord The keywords that the user wishes to search for.
     */
    public FindCommand(Storage s, TaskList t, String keyWord) {
        super(s, t);
        this.keyWord = keyWord;
    }

    @Override
    public void run() {}

    @Override
    public String getMessage() {
        return this.tasks.searchTasks(this.keyWord);
    }

}
