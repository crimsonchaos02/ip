package edith;

/**
 * Handles the program logic.
 */
public class Logic {
    private Storage storage;
    private TaskList tasks;

    /**
     * Constructs a new Logic instance.
     * @param s relevant Storage object
     * @param t relevant TaskList object
     */
    public Logic(Storage s, TaskList t) {
        this.storage = s;
        this.tasks = t;
    }

    /**
     * Used to obtain the appropriate response given a user input.
     *
     * @param inp the user input.
     * @return the appropriate response from Edith.
     */
    public String getResponse(String inp) throws EdithException {
        String out = "";
        try {
            String[] inps = inp.split(" ");
            Command cmd = Parser.fromString(inps[0]);

            if (cmd == Command.BYE) {
                out = "=================================="
                        + "\njiayousss bye have a great time"
                        + "\n==================================";
            } else if (cmd == Command.LIST) {
                out = this.tasks.toString();
            } else if (cmd == Command.CMDS) {
                out = "commands list:"
                        + "\nuse list to show your current tasks"
                        + "\nuse mark i to mark task i as done"
                        + "\nuse unmark i to mark task i as undone"
                        + "\nuse todo to add a task"
                        + "\nuse deadline to add a deadline (/by to specify due date)"
                        + "\nuse event to add an event (/from and /by to specify details)"
                        + "\nuse bye to exit the chatbot";
            } else if (cmd == Command.MARK) {
                if (!inps[1].matches("-?\\d+")) {
                    throw new EdithException("please enter index of the task to mark done (use list to check)");
                }
                int index = Integer.parseInt(inps[1]) - 1;
                out = tasks.markDone(index);
                storage.saveToFile(tasks);
            } else if (cmd == Command.UNMARK) {
                if (!inps[1].matches("-?\\d+")) {
                    throw new EdithException("please enter index of the task to mark done (use list to check)");
                }
                int index = Integer.parseInt(inps[1]) - 1;
                out = tasks.markUndone(index);
                storage.saveToFile(tasks);
            } else if (cmd == Command.TODO || cmd == Command.DEADLINE || cmd == Command.EVENT) {
                Task newTask = Parser.parseTaskInput(cmd, inp);
                out = tasks.addTask(newTask);
                storage.saveToFile(tasks);
            } else if (cmd == Command.DELETE) {
                if (!inps[1].matches("-?\\d+")) {
                    throw new EdithException("please enter index of the task to delete (use list to find index)");
                }
                int index = Integer.parseInt(inps[1]) - 1;
                out = tasks.removeTask(index);
                storage.saveToFile(tasks);
            } else if (cmd == Command.FIND) {
                String searchKeywords = inp.substring(5);
                TaskList requiredTasks = tasks.searchTasks(searchKeywords);
                out = requiredTasks.toString();
            } else {
                throw new EdithException("get your formatting right thanks (type cmd/cmds for valid commands)");
            }
            return out;
        } catch (EdithException e) {
            return e.getMessage();
        }
    }
}
