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

        ArrayList<String> inputs = new ArrayList<>();

        while (true) {
            String inp = scanner.nextLine();

            if (inp.equals("bye")) {
                System.out.println(out_message);
                break;
            } else if (inp.equals("list")) {
                System.out.println("==================================");
                for (int i = 0; i < inputs.size(); i++){
                    System.out.print(i+1);
                    System.out.println(". " + inputs.get(i));
                }
                System.out.println("==================================");
                
            } else {
                inputs.add(inp);
                System.out.println(pad("added: " + inp));
            }

        }


    }
}
