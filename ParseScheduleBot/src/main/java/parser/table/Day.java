package parser.table;

import java.util.ArrayList;
import java.util.List;

public class Day {

    private final ArrayList<UniversityPair> universityPairs;

    public Day(int column, int row, List<List<String>> full) {
        universityPairs = new ArrayList<>();
        int countPair = row == 2 ? 8 : 7;
        for (int pair = 0; pair < countPair; pair++) {
            universityPairs.add(new UniversityPair(
                    new Lesson(
                            getText(full, row + pair * 2, column) + " (" + getText(full, row + pair * 2, column + 1) + ")",
                            getText(full, row + pair * 2, column + 2),
                            getText(full, row + pair * 2, column + 3)
                    ),
                    new Lesson(
                            getText(full, row + pair * 2 + 1, column) + " (" + getText(full, row + pair * 2 + 1, column + 1) + ")",
                            getText(full, row + pair * 2 + 1, column + 2),
                            getText(full, row + pair * 2 + 1, column + 3)
                    )
            ));
        }
    }

    private String getText(List<List<String>> full, int row, int column) {
        return full.get(row).get(column);
    }


    public ArrayList<UniversityPair> getUniversityPairs() {
        return universityPairs;
    }
}
