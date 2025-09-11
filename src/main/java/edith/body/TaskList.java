package edith.body;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edith.task.Deadline;
import edith.task.Event;
import edith.task.Task;
import edith.util.EdithException;

/**
 * This class handles issues related to the task list -- marking/unmarking, adding/deleting tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs new Tasklist object.
     * @param tasks List of tasks.
     */

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns a Task object corresponding to its index on the TaskList.
     * @param i Input index.
     * @throws EdithException if index out of bounds.
     */

    public Task getTask(Integer i) throws EdithException {
        if (i < 0 || i >= tasks.size()) {
            throw new EdithException("please use valid task number!");
        }
        return tasks.get(i);
    }

    public int getSize() {
        return this.tasks.size();
    }

    /**
     * Adds a task to the task list, and returns the appropriate message to the user.
     * @param t The task to be added.
     */
    public void addTask(Task t) {
        this.tasks.add(t);
    }

    /**
     * Removes a task from the task list, and returns appropriate message for user.
     * @param i Index of task to be removed.
     */

    public void removeTask(int i) {
        this.tasks.remove(i);
    }

    /**
     * Marks a task as done, and returns appropriate message for user.
     * @param t index of task to be marked done.
     */

    public void markDone(Task t) {
        t.markAsDone();
    }

    /**
     * Marks a task as undone, and returns appropriate message for user.
     * @param t Task to be marked undone.
     */

    public void markUndone(Task t) {
        t.markAsUndone();
    }

    /**
     * Returns a new TaskList with the relevant keywords
     * @param keyWords the search term entered by the user.
     * @return a new TaskList object with tasks containing these words.
     */
    public String searchTasks(String keyWords) {
        assert !keyWords.isEmpty() : "error: cannot search for empty string";
        ArrayList<Task> outList = new ArrayList<>();

        for (Task t : tasks) {
            String description = t.getDescription();
            if (description.toLowerCase().contains(keyWords.toLowerCase())) {
                outList.add(t);
            }
        }
        return new TaskList(outList).toString();
    }

    /**
     * Helper function for view function. Handles the formatting of the tasks.
     * @param s StringBuilder object containing output text.
     * @param t Task to be added to this object.
     * @param index Index of the task to be added.
     */
    public static void formatTaskForOutput(StringBuilder s, Task t, Integer index) {
        s.append(index);
        s.append(". ");
        s.append(t.toString());
        s.append("\n");
    }

    /**
     * Returns a list of tasks due either on a certain date or up to it.
     * @param date The required date.
     * @param decision Based on user input, choose whether up to a date or before it.
     * @return The required output to the user.
     */

    public String viewScheduled(LocalDateTime date, String decision) {
        StringBuilder scheduled = new StringBuilder();
        StringBuilder undated = new StringBuilder();
        int scheduledIndex = 1;
        int undatedIndex = 1;

        for (Task t : tasks) {
            if (!(t instanceof Deadline) && !(t instanceof Event)) {
                formatTaskForOutput(undated, t, undatedIndex);
                undatedIndex += 1;
            }
            assert decision.equals("for") || decision.equals("before") : "decision format wrong";
            if (decision.equals("for") && t.isOn(date)) {
                formatTaskForOutput(scheduled, t, scheduledIndex);
                scheduledIndex += 1;
            } else if (t.isBefore(date)) {
                formatTaskForOutput(scheduled, t, scheduledIndex);
                scheduledIndex += 1;
            }
        }
        return "Tasks due " + decision + " "
                + date.format(DateTimeFormatter.ofPattern("dd MMM yyyy HHmm"))
                + ":\n"
                + scheduled.toString()
                + "You have the following undated tasks:\n"
                + undated.toString().trim();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            out.append(i + 1);
            out.append(". ");
            out.append(tasks.get(i).toString());
            out.append("\n");
        }
        if (!out.isEmpty()) {
            return out.deleteCharAt(out.length() - 1).toString();
        } else {
            return out.toString();
        }
    }
}


