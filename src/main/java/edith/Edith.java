package edith;

import java.util.ArrayList;

import edith.body.Logic;
import edith.body.Parser;
import edith.body.Storage;
import edith.body.TaskList;
import edith.body.Ui;
import edith.util.CommandType;
import edith.util.EdithException;

/**
 * Main code of the Edith chatbot.
 *
 */

public class Edith {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Logic logic;

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
            this.logic = new Logic(storage, tasks);
        } catch (EdithException e) {
            this.tasks = new TaskList(new ArrayList<>());
            this.logic = new Logic(storage, tasks);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Given a user input, get the appropriate response. Used for GUI.
     * @param s
     * @return appropriate String
     * @throws EdithException
     */
    public String getResponse(String s) throws EdithException {
        try {
            return this.logic.handleInput(s);
        } catch (EdithException e) {
            return e.getMessage();
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
                String out = logic.handleInput(inp);
                ui.printMsg(out);
                if (Parser.getCommandTypeFromString(inp.split(" ")[0]) == CommandType.BYE) {
                    break;
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
     * @param args CLI user input.
     */

    public static void main(String[] args) {
        new Edith("output.txt").run();
    }
}
