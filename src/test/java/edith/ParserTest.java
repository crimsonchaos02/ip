package edith;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ParserTest {
    @Test
    public void parserTest1() {
        String parseDayTest1 = "fri";
        String parseDayTest2 = "thurs";
        assertEquals(Parser.parseDay(parseDayTest1), DayOfWeek.FRIDAY, "fri should be Friday");
        assertEquals(Parser.parseDay(parseDayTest2), DayOfWeek.THURSDAY, "thurs should be Thursday");
    }

    @Test
    public void parserTest2() {
        String parseDateTimeTest1 = "this sunday 2359";
        String parseDateTimeTest2 = "next wed 1230";
        LocalDateTime expected1 = LocalDateTime.of(2025, 8, 31, 23, 59);
        LocalDateTime expected2 = LocalDateTime.of(2025, 9, 3, 12, 30);
        try {
            assertEquals(Parser.parseDateTime(parseDateTimeTest1), expected1, "this sunday 2359");
            assertEquals(Parser.parseDateTime(parseDateTimeTest2), expected2, "next wed 1230");
        } catch (Exception e) {
            fail("parseDateTime failed on valid input.");
        }
    }

    @Test
    public void taskListTest1() {
        ArrayList<Task> tmp = new ArrayList<>();
        tmp.add(new Task("test1"));
        tmp.add(new Deadline("test2", LocalDateTime.of(2025,8,30,12,0)));
        LocalDateTime start = LocalDateTime.of(2025, 8, 31, 18, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 31, 22, 0);
        tmp.add(new Event("test3", start, end));

        TaskList toStrTest = new TaskList(tmp);

        String expected = "1. [T][ ] test1\n" +
                "2. [D][ ] test2, due by: saturday 1200\n" +
                "3. [E][ ] test3 (from: sunday 1800 to: 2200)";

        assertEquals(toStrTest.toString(), expected);
    }

    @Test
    public void taskListTest2() {
        ArrayList<Task> tmp = new ArrayList<>();
        tmp.add(new Task("test1"));
        tmp.add(new Deadline("test2", LocalDateTime.of(2025,8,30,12,0)));
        LocalDateTime start = LocalDateTime.of(2025, 8, 31, 18, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 31, 22, 0);
        tmp.add(new Event("test3", start, end));

        TaskList markDoneTest = new TaskList(tmp);

        String expected = "good job buddy you finished task:\n" + "[T][X] test1";

        try {
            assertEquals(markDoneTest.markDone(0), expected);
        } catch (Exception e) {
            fail("parseDateTime failed on valid input.");
        };
    }

}
