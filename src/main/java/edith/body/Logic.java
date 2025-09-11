package edith.body;


import java.time.LocalDateTime;
import java.util.Arrays;

import edith.command.Command;
import edith.command.DeleteCommand;
import edith.command.ExitCommand;
import edith.command.FindCommand;
import edith.command.HelpCommand;
import edith.command.ListCommand;
import edith.command.MarkCommand;
import edith.command.NewDeadlineCommand;
import edith.command.NewEventCommand;
import edith.command.NewTaskCommand;
import edith.command.UnmarkCommand;
import edith.command.ViewCommand;
import edith.task.Deadline;
import edith.task.Event;
import edith.task.Task;
import edith.util.CommandType;
import edith.util.EdithException;

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

    public Command getCommandFromString(String s) throws EdithException {
        String[] inps = s.split(" ");
        CommandType cmd = Parser.getCommandTypeFromString(inps[0]);

        if (cmd == null) {
            throw new EdithException("please use a valid command (type help for command list)");
        }
        //CHECKSTYLE.OFF: Indentation
        try {
            return switch (cmd) {
                case BYE -> new ExitCommand(this.storage, this.tasks);
                case LIST -> new ListCommand(this.storage, this.tasks);
                case HELP -> new HelpCommand(this.storage, this.tasks);
                case MARK -> new MarkCommand(this.storage, this.tasks, Integer.parseInt(inps[1]) - 1);
                case UNMARK -> new UnmarkCommand(this.storage, this.tasks, Integer.parseInt(inps[1]) - 1);
                case TODO -> new NewTaskCommand(this.storage, this.tasks, new Task(inps[1]));
                case DEADLINE -> new NewDeadlineCommand(
                        this.storage,
                        this.tasks,
                        new Deadline(inps[1], LocalDateTime.parse(inps[2])));
                case EVENT -> new NewEventCommand(
                        this.storage,
                        this.tasks,
                        new Event(inps[1], LocalDateTime.parse(inps[2]), LocalDateTime.parse(inps[3])));
                case DELETE -> new DeleteCommand(this.storage, this.tasks, Integer.parseInt(inps[1]) - 1);
                case FIND -> new FindCommand(
                        this.storage,
                        this.tasks,
                        String.join(" ", Arrays.copyOfRange(inps, 1, inps.length)));
                case VIEW -> new ViewCommand(
                        this.storage,
                        this.tasks,
                        inps[1],
                        LocalDateTime.parse(inps[2]));
            };
        } catch (EdithException e) {
            throw new EdithException(e.getMessage());
        }
    }

    /**
     * Used to obtain the appropriate response given a user input.
     *
     * @param inp the user input.
     * @return the appropriate response from Edith.
     */
    public String handleInput(String inp) throws EdithException {
        try {
            String readableCommand = Parser.parseInput(inp);
            Command newCommand = getCommandFromString(readableCommand);
            newCommand.run();
            return newCommand.getMessage();
        } catch (EdithException e) {
            return e.getMessage();
        }
    }
}
