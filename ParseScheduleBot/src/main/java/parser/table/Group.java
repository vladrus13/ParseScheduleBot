package parser.table;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private final ArrayList<Day> days;

    public Group(int position, List<List<String>> full) {
        days = new ArrayList<>();
        int start = 2;
        days.add(new Day(position, start, full));
        start += 17;
        for (int i = 1; i < 6; i++) {
            days.add(new Day(position, start, full));
            start += 15;
        }
    }

    public ArrayList<Day> getDays() {
        return days;
    }
}
