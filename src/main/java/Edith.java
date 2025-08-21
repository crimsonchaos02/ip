import java.util.*;

public class Edith {
    public static String pad(String s) {
        return "==================================\n"
                + s
                + "\n==================================";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String greeting = "=================================="
                + "\nhello! this is edith :D"
                + "\nwhat do we need today?"
                + "\n==================================";
        String out_message = "=================================="
                + "\njiayousss bye have a great time"
                + "\n==================================";

        System.out.println(greeting);

        ArrayList<Task> tasks = new ArrayList<>();

        while (true) {
            try {
                String inp = scanner.nextLine();
                String[] inps = inp.split(" ");

                if (inp.equals("bye")) {
                    System.out.println(out_message);
                    break;

                } else if (inp.equals("list")) {
                    System.out.println("==================================");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.print(i + 1);
                        System.out.println(". " + tasks.get(i).toString());
                    }
                    System.out.println("==================================");

                } else if (inp.equals("cmds") || inp.equals("cmd")) {
                    String out_msg = "commands list:"
                                    + "\nuse list to show your current tasks"
                                    + "\nuse mark i to mark task i as done"
                                    + "\nuse unmark i to mark task i as undone"
                                    + "\nuse todo to add a task"
                                    + "\nuse deadline to add a deadline (/by to specify due date)"
                                    + "\nuse event to add an event (/from and /by to specify details)"
                                    + "\nuse bye to exit the chatbot";
                    System.out.println(pad(out_msg));
                } else if (inps[0].equals("mark")) {
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

                } else if (inps[0].equals("unmark")) {
                    if (!inps[1].matches("-?\\d+")) {
                        throw new EdithException("please enter index of the task to mark done (use list to find index)");
                    }
                    int index = Integer.parseInt(inps[1]) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        throw new EdithException("enter a valid index -- that's not in the to do list range");
                    }
                    tasks.get(index).markAsUndone();
                    String msg = "alright then we reopening task:\n"
                            + tasks.get(index).toString();
                    System.out.println(pad(msg));

                } else if (inps[0].equals("todo")) {
                    if (inps.length == 1) {
                        throw new EdithException("include a task description");
                    }
                    tasks.add(new Task(inp));
                    System.out.println(pad("added new todo task:\n" + tasks.get(tasks.size() - 1).toString()
                            + "\nyou have " + tasks.size() + " tasks left"));

                } else if (inps[0].equals("deadline")) {
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

                } else if (inps[0].equals("event")) {
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

                } else {
                    throw new EdithException("get your formatting right thanks (type cmds for valid commands)");
                }
            } catch (EdithException e) {
                System.out.println(pad("woi please " + e.getMessage()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
