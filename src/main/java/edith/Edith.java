package edith;

import java.util.ArrayList;

/**
 * Main code of the Edith chatbot.
 *
 */

public class Edith {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates new Edith chatbot instance.
     *
     * @param fileName Output file from which task lists are loaded and saved.
     *
     */
    public Edith(String fileName) {
        this.ui = new Ui();
        this.storage = new Storage(fileName);
        try {
            this.tasks = new TaskList(storage.loadFromFile());
        } catch (EdithException e) {
            this.tasks = new TaskList(new ArrayList<>());
            System.out.println("load error: your task list is now re-initialised");
        }
    }

    /**
     * Main logic code of the chatbot.
     */

    public void run() {
        ui.greeting();

        while (true) {
            try {
                String inp = ui.getInput();
                String[] inps = inp.split(" ");

                Command cmd = Parser.fromString(inps[0]);

                if (cmd == Command.BYE) {
                    ui.exit();
                    break;

                } else if (cmd == Command.LIST) {
                    ui.printMsg(tasks.toString());
                } else if (cmd == Command.CMDS) {
                    String outMsg = "commands list:"
                            + "\nuse list to show your current tasks"
                            + "\nuse mark i to mark task i as done"
                            + "\nuse unmark i to mark task i as undone"
                            + "\nuse todo to add a task"
                            + "\nuse deadline to add a deadline (/by to specify due date)"
                            + "\nuse event to add an event (/from and /by to specify details)"
                            + "\nuse bye to exit the chatbot";
                    ui.printMsg(outMsg);

                } else if (cmd == Command.MARK) {
                    if (!inps[1].matches("-?\\d+")) {
                        throw new EdithException("please enter index of the task to mark done (use list to find index)");
                    }
                    int index = Integer.parseInt(inps[1]) - 1;
                    String out = tasks.markDone(index);
                    ui.printMsg(out);
                    storage.saveToFile(tasks);

                } else if (cmd == Command.UNMARK) {
                    if (!inps[1].matches("-?\\d+")) {
                        throw new EdithException("please enter index of the task to mark done (use list to find index)");
                    }
                    int index = Integer.parseInt(inps[1]) - 1;
                    String out = tasks.markUndone(index);
                    ui.printMsg(out);
                    storage.saveToFile(tasks);

                } else if (cmd == Command.TODO || cmd == Command.DEADLINE || cmd == Command.EVENT) {
                    Task newTask = Parser.parseTaskInput(cmd, inp);
                    String out = tasks.addTask(newTask);
                    ui.printMsg(out);
                    storage.saveToFile(tasks);
                } else if (cmd == Command.DELETE) {
                    if (!inps[1].matches("-?\\d+")) {
                        throw new EdithException("please enter index of the task to delete (use list to find index)");
                    }
                    int index = Integer.parseInt(inps[1]) - 1;
                    String out = tasks.removeTask(index);
                    ui.printMsg(out);
                    storage.saveToFile(tasks);
                } else {
                    throw new EdithException("get your formatting right thanks (type cmd/cmds for valid commands)");
                }
            } catch (EdithException e) {
                ui.handleError(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Runs an instance of the Edith chatbot.
     *
     * @param args User input.
     */

    public static void main(String[] args) {
        new Edith("output.txt").run();
    }
}