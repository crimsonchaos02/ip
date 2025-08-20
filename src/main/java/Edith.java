import java.util.Scanner;

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

        while (true) {
            String inp = scanner.nextLine();

            if (inp.equals("bye")) {
                System.out.println(out_message);
                break;
            } else {
                System.out.println(pad(inp));
            }
        }


    }
}
