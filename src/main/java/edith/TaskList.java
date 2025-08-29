package edith;

import java.util.ArrayList;

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
     * Adds a task to the task list, and returns the appropriate message to the user.
     * @param t The task to be added.
     * @return Apprropriate message for user.
     */
    public String addTask (Task t) {
        this.tasks.add(t);
        StringBuilder out = new StringBuilder();
        out.append("added new ");
        out.append(t.toString().charAt(1) == 'T'
                ? "task:\n"
                : t.toString().charAt(1) == 'E'
                ? "event:\n"
                : "deadline:\n");

        out.append(t);
        out.append("\nyou have ");
        out.append(tasks.size());
        out.append(" tasks left");

        return out.toString();
    }

    /**
     * Removes a task from the task list, and returns apprropriate message for user.
     * @param i Index of task to be removed.
     * @return Apprropriate message for user.
     * @throws EdithException if index out of range.
     */

    public String removeTask (int i) throws EdithException {
        String taskDescr = "";
        if (i >= tasks.size() || i  < 0) {
            throw new EdithException("please use valid task number!");
        } else {
            taskDescr = this.tasks.get(i).toString();
            this.tasks.remove(i);
        }

        StringBuilder out = new StringBuilder();
        out.append("okay we removed:\n");
        out.append(taskDescr);
        out.append("\nyou have ");
        out.append(tasks.size());
        out.append(" tasks left");

        return out.toString();
    }

    /**
     * Marks a task as done, and returns apprropriate message for user.
     * @param i index of task to be marked done.
     * @return Apprropriate message for user.
     * @throws EdithException if index out of bounds.
     */

    public String markDone (int i) throws EdithException {
        if (i < 0 || i >= tasks.size()) {
            throw new EdithException("enter a valid index -- that's not in the to do list range");
        }
        tasks.get(i).markAsDone();

        return "good job buddy you finished task:\n" + tasks.get(i).toString();
    }

    /**
     * Marks a task as undone, and returns apprropriate message for user.
     * @param i index of task to be marked undone.
     * @return Apprropriate message for user.
     * @throws EdithException if index out of range.
     */

    public String markUndone (int i) throws EdithException {
        if (i < 0 || i >= tasks.size()) {
            throw new EdithException("enter a valid index -- that's not in the to do list range");
        }
        tasks.get(i).markAsUndone();
        return "alright then we reopening task:\n" + tasks.get(i).toString();
    }

    public TaskList searchTasks(String keyWords) {
        ArrayList<Task> outList = new ArrayList<>();

        for (Task t : tasks) {
            String description = t.getDescription();
            if (description.toLowerCase().contains(keyWords.toLowerCase())) {
                outList.add(t);
            }
        }
        return new TaskList(outList);
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
        if(!out.isEmpty()){
            return out.deleteCharAt(out.length()-1).toString();
        } else {
            return out.toString();
        }
    }
}


