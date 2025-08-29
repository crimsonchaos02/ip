package edith;

/**
 * Thrown to indicate that the user has likely made an input not following the required format.
 *
 */

public class EdithException extends Exception{
    public EdithException(String msg) {
        super(msg);
    }
}
