package edith;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

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

    public String markDone (int i) throws EdithException {
        if (i < 0 || i >= tasks.size()) {
            throw new EdithException("enter a valid index -- that's not in the to do list range");
        }
        tasks.get(i).markAsDone();

        return "good job buddy you finished task:\n" + tasks.get(i).toString();
    }

    public String markUndone (int i) throws EdithException {
        if (i < 0 || i >= tasks.size()) {
            throw new EdithException("enter a valid index -- that's not in the to do list range");
        }
        tasks.get(i).markAsUndone();
        return "alright then we reopening task:\n" + tasks.get(i).toString();
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


