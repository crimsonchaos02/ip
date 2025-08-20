public class Deadline extends Task {
    protected String dueDate;

    public Deadline(String description, String dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        String icon = this.isDone ? "X" : " ";
        return "[D][" + icon + "] " + this.description + ", due by: " + dueDate;
    }
}
