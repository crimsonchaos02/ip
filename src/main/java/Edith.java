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
            String inp = scanner.nextLine();
            String[] inps = inp.split(" ");

            if (inp.equals("bye")) {
                System.out.println(out_message);
                break;

            } else if (inp.equals("list")) {
                System.out.println("==================================");
                for (int i = 0; i < tasks.size(); i++){
                    System.out.print(i+1);
                    System.out.println(". " + tasks.get(i).toString());
                }
                System.out.println("==================================");

            } else if (inps[0].equals("mark")) {
                int index = Integer.parseInt(inps[1]) - 1;
                tasks.get(index).markAsDone();
                String msg = "good job buddy you finished task:\n"
                        + tasks.get(index).toString();
                System.out.println(pad(msg));

            } else if (inps[0].equals("unmark")) {
                int index = Integer.parseInt(inps[1]) - 1;
                tasks.get(index).markAsUndone();
                String msg = "alright then we reopening task:\n"
                        + tasks.get(index).toString();
                System.out.println(pad(msg));

            } else {
                tasks.add(new Task(inp));
                System.out.println(pad("added new task: " + inp));
            }

        }


    }
}
