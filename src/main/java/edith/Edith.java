package edith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Scanner;

/**
 * Main code of the Edith chatbot.
 *
 */

public class Edith {

    /**
     * Returns the same string, padded above and below for formatting. Helper function.
     *
     * @param s Message that is to be padded.
     * @return Padded message.
     */
    public static String pad(String s) {
        return "==================================\n"
                + s
                + "\n==================================";
    }

    /**
     * Saves the task list to an external txt file.
     *
     * @param tasks The ArrayList of tasks to be saved.
     * @param filename The name of the external file to which the task list will be saved.
     */

    public static void saveToFile(ArrayList<Task> tasks, String filename) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            out.append(i + 1);
            out.append(". ");
            out.append(tasks.get(i).toString());
            out.append("\n");
        }

        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(out.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a Task object from a string. Used for reading saved task lists from external files.
     *
     * @param s A String representing a single task.
     * @return A corresponding Task object.
     */

    public static Task parseTask(String s) {
        Task out;
        char type = s.charAt(4);
        char done = s.charAt(7);

        if (type == 'T') {
            String desc = s.substring(10);
            out =  new Task(desc);
        } else if (type == 'D') {
            String[] tmp = s.split(", due by: ");
            String due = tmp[1];
            String desc = tmp[0].substring(10);
            out = new Deadline(desc, due);

        } else {
            String[] tmp = s.split("from: | to: " );
            String from = tmp[1];
            String to = tmp[2].substring(0, tmp[2].length()-1);
            String desc = s.substring(10, s.indexOf('(')-1);
            out = new Event(desc, from, to);
        }
        if (done == 'X') {
            out.markAsDone();
        }
        return out;
    }

    /**
     * Body code for the chatbot.
     *
     * @param args User input.
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String greeting = "=================================="
                + "\nhello! this is edith :D"
                + "\nwhat do we need today?"
                + "\n==================================";
        String exitMessage = "=================================="
                + "\njiayousss bye have a great time"
                + "\n==================================";

        ArrayList<Task> tasks = new ArrayList<>();

        try {
            File file = new File("output.txt");
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                String next = s.nextLine();
                if (next.equals("\n")) {
                    break;
                }
                Task t = parseTask(next);
                tasks.add(t);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Note -- no saved task list found.");
        }

        System.out.println(greeting);

        while (true) {
            try {
                String inp = scanner.nextLine();
                String[] inps = inp.split(" ");

                Command cmd = Command.fromString(inps[0]);

                if (cmd == Command.BYE) {
                    System.out.println(exitMessage);
                    break;

                } else if (cmd == Command.LIST) {
                    System.out.println("==================================");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.print(i + 1);
                        System.out.println(". " + tasks.get(i).toString());
                    }
                    System.out.println("==================================");

                } else if (cmd == Command.CMDS) {
                    String out_msg = "commands list:"
                                    + "\nuse list to show your current tasks"
                                    + "\nuse mark i to mark task i as done"
                                    + "\nuse unmark i to mark task i as undone"
                                    + "\nuse todo to add a task"
                                    + "\nuse deadline to add a deadline (/by to specify due date)"
                                    + "\nuse event to add an event (/from and /by to specify details)"
                                    + "\nuse bye to exit the chatbot";
                    System.out.println(pad(out_msg));

                } else if (cmd == Command.MARK) {
                    if (!inps[1].matches("-?\\d+")) {
                        throw new EdithException("please enter index of the task to mark done (use list to find index)");
                    }
                    int index = Integer.parseInt(inps[1]) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        throw new EdithException("enter a valid index -- that's not in the to do list range");
                    }
                    tasks.get(index).markAsDone();
                    String msg = "good job buddy you finished task:\n"
                            + tasks.get(index).toString();
                    System.out.println(pad(msg));
                    saveToFile(tasks, "output.txt");

                } else if (cmd == Command.UNMARK) {
                    if (!inps[1].matches("-?\\d+")) {
                        throw new EdithException("please enter index of the task to mark undone (use list to find index)");
                    }
                    int index = Integer.parseInt(inps[1]) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        throw new EdithException("enter a valid index -- that's not in the to do list range");
                    }
                    tasks.get(index).markAsUndone();
                    String msg = "alright then we reopening task:\n"
                            + tasks.get(index).toString();
                    System.out.println(pad(msg));
                    saveToFile(tasks, "output.txt");

                } else if (cmd == Command.TODO) {
                    if (inps.length == 1) {
                        throw new EdithException("include a task description");
                    }
                    String description = String.join(" ",
                            Arrays.copyOfRange(inps, 1, inps.length));
                    //basically just removing the command word from the description phrase
                    tasks.add(new Task(description));
                    System.out.println(pad("added new todo task:\n" + tasks.get(tasks.size() - 1).toString()
                            + "\nyou have " + tasks.size() + " tasks left"));
                    saveToFile(tasks, "output.txt");

                } else if (cmd == Command.DEADLINE) {
                    String[] tmp = inp.split(" /by ");
                    if (tmp.length == 1) {
                        throw new EdithException("use '/by' indicating the deadline");
                    }
                    if (tmp[0].split(" ").length == 1) {
                        throw new EdithException("include a task description");
                    }
                    String description = String.join(" ",
                            Arrays.copyOfRange(tmp[0].split(" "), 1, (tmp[0].split(" ").length)));
                    //basically just removing the command word from the description phrase
                    tasks.add(new Deadline(description, tmp[1]));
                    System.out.println(pad("added new deadline:\n" + tasks.get(tasks.size() - 1).toString()
                            + "\nyou have " + tasks.size() + " tasks left"));
                    saveToFile(tasks, "output.txt");

                } else if (cmd == Command.EVENT) {
                    String[] tmp = inp.split(" /from | /to ");
                    if (tmp.length == 1) {
                        throw new EdithException("use '/from' and '/to' indicating event period");
                    }

                    if (tmp[0].split(" ").length == 1) {
                        throw new EdithException("include a task description");
                    }

                    String description = String.join(" ",
                            Arrays.copyOfRange(tmp[0].split(" "), 1, (tmp[0].split(" ").length)));
                    tasks.add(new Event(description, tmp[1], tmp[2]));
                    System.out.println(pad("added new event:\n" + tasks.get(tasks.size() - 1).toString()
                            + "\nyou have " + tasks.size() + " tasks left"));
                    saveToFile(tasks, "output.txt");

                } else if (cmd == Command.DELETE) {
                    if (!inps[1].matches("-?\\d+")) {
                        throw new EdithException("please enter index of the task to delete (use list to find index)");
                    }
                    int index = Integer.parseInt(inps[1]) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        throw new EdithException("enter a valid index -- that's not in the to do list range");
                    }
                    String task_descr = tasks.get(index).toString();
                    tasks.remove(index);

                    String out_msg = "okay we removed\n"
                                    + task_descr
                                    + "\nyou have " + tasks.size() + " tasks left";
                    System.out.println(out_msg);
                    saveToFile(tasks, "output.txt");

                } else {
                    throw new EdithException("get your formatting right thanks (type cmd/cmds for valid commands)");
                }

            } catch (EdithException e) {
                System.out.println(pad("woi please " + e.getMessage()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}