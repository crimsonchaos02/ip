package edith.body;

import java.util.ArrayList;

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
        ArrayList<Task> outList = new ArrayList<>();

        for (Task t : tasks) {
            String description = t.getDescription();
            if (description.toLowerCase().contains(keyWords.toLowerCase())) {
                outList.add(t);
            }
        }
        return new TaskList(outList).toString();
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


