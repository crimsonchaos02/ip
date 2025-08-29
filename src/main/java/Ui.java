import java.util.Scanner;

public class Ui {
    private final String GREETING_MESSAGE;
    private final String EXIT_MESSAGE;
    private Scanner scanner;

    public Ui() {
        this.GREETING_MESSAGE = "=================================="
                + "\nhello! this is edith :D"
                + "\nwhat do we need today?"
                + "\n==================================";

        this.EXIT_MESSAGE = "=================================="
                + "\njiayousss bye have a great time"
                + "\n==================================";

        this.scanner = new Scanner(System.in);
    }

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

    public String getInput() {
        return scanner.nextLine();
    }

    public void handleError(String s) {
        System.out.println(pad(s));
    }

    public void printMsg(String s) {
        System.out.println(pad(s));
    }

    public void greeting() {
        System.out.println(GREETING_MESSAGE);
    }

    public void exit() {
        System.out.println(EXIT_MESSAGE);
    }

}
