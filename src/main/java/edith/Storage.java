package edith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles the saving and loading of the task list to the output file.
 */
public class Storage {
    private final String fileName;

    public Storage(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Saves the task list to an external txt file.
     *
     * @param tasks The ArrayList of tasks to be saved.
     */

    public void saveToFile(TaskList tasks) throws EdithException {
        String out = tasks.toString();
        try {
            FileWriter writer = new FileWriter(this.fileName);
            writer.write(out);
            writer.close();
        } catch (IOException e) {
            throw new EdithException(e.getMessage());
        }
    }

    /**
     * Loads the task list from the file.
     * @return The required task list.
     * @throws EdithException if the file is corrupted (wrong format) or missing.
     */
    public ArrayList<Task> loadFromFile() throws EdithException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            File file = new File("output.txt");
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                String next = s.nextLine();
                if (next.equals("\n")) {
                    break;
                }
                Task t = Parser.parseTask(next);
                tasks.add(t);
            }
        } catch (FileNotFoundException e) {
            throw new EdithException("file not found. starting with empty task list.");
        } catch (EdithException e) {
            throw new EdithException("load error: your task list is now re-initialised");
        }
        return tasks;
    }
}
