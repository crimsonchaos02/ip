package edith;

import java.util.Scanner;

/**
 * Ui class handles user input and output messages.
 */

public class Ui {
    private final String GREETING_MESSAGE;
    private final String EXIT_MESSAGE;
    private Scanner scanner;

    /**
     * Constructs a ui instance.
     */
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

    /**
     * Reads input from user.
     * @return
     */

    public String getInput() {
        return scanner.nextLine();
    }

    /**
     * Handles errors thrown during interaction with the user.
     * @param s Error message.
     */

    public void handleError(String s) {
        System.out.println(pad(s));
    }

    /**
     * Prints the formatted message.
     * @param s Message to be printed.
     */

    public void printMsg(String s) {
        System.out.println(pad(s));
    }

    /**
     * Initial output message to user.
     */

    public void greeting() {
        System.out.println(GREETING_MESSAGE);
    }

    /**
     * Closing output message to user.
     */

    public void exit() {
        System.out.println(EXIT_MESSAGE);
    }
}
