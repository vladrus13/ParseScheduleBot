package parser.table;

import bean.User;
import bean.UserSubjects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.TableUtils;

import java.io.IOException;
import java.util.*;

public class Table {

    private final Map<String, Group> groups;

    public Table() throws IOException {
        Element full = Jsoup.connect("https://docs.google.com/spreadsheets/d/e/2PACX-1vRsULJSN8TYmUYBkQ2oVv8QaxUFuYCDdPx_nsVwRPN6wpnl3sKjsre92QEPazy9LgG3eXsIxt2ZaKxw/pubhtml?gid=2143740747&single=true")
                .get()
                .getElementsByTag("table").get(0)
                .getElementsByTag("tbody").get(0);

        List<List<String>> table;

        ArrayList<Skip> skips = new ArrayList<>();
        table = new ArrayList<>();
        Elements trs = full.getElementsByTag("tr");
        int row = 0, column = 0;
        for (Element tr : trs) {
            table.add(new ArrayList<>());
            Elements tds = tr.getElementsByTag("td");
            for (Element td : tds) {
                while (true) {
                    boolean isExist = false;
                    for (Skip skip : skips) {
                        if (skip.startRow == row && skip.startColumn == column) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        break;
                    }
                    int finalRow1 = row;
                    int finalColumn1 = column;
                    @SuppressWarnings("OptionalGetWithoutIsPresent")
                    Skip skip = skips.stream().filter(element -> element.startRow == finalRow1 && element.startColumn == finalColumn1).findAny().get();
                    for (int i = 0; i < skip.sizeColumn; i++) {
                        table.get(row).add(skip.text);
                        column++;
                    }
                    skip.down();
                    if (skip.sizeRow == 0) {
                        skips.remove(skip);
                    }
                }
                String text = td.text();
                int futureRow = 1, futureColumn = 1;
                if (td.hasAttr("rowspan")) {
                    futureRow = Integer.parseInt(td.attr("rowspan"));
                }
                if (td.hasAttr("colspan")) {
                    futureColumn = Integer.parseInt(td.attr("colspan"));
                }
                if (futureRow == 1 && futureColumn == 1) {
                    table.get(row).add(text);
                    column++;
                } else {
                    Skip skip = new Skip(row, column, futureRow, futureColumn, text);
                    for (int i = 0; i < skip.sizeColumn; i++) {
                        table.get(row).add(skip.text);
                        column++;
                    }
                    skip.down();
                    if (skip.sizeRow != 0) {
                        skips.add(skip);
                    }
                }
            }
            row++;
            column = 0;
        }

        groups = new HashMap<>();

        int start = 6;
        String last = "";
        while (table.get(0).size() > start) {
            if (!(Objects.equals(table.get(0).get(start), last) || table.get(0).get(start).equals(""))) {
                last = table.get(0).get(start);
                groups.put(last, new Group(start, table));
            }
            start++;
        }
    }

    public String toString(String group, User user, UserSubjects userSubjects) {
        if (!groups.containsKey(group)) {
            return "Group not found!";
        }
        return TableUtils.accumulate(this, user, userSubjects, null);
    }

    public String toString(String group, int day, User user, UserSubjects userSubjects) {
        if (!groups.containsKey(group)) {
            return "Group not found!";
        }
        return TableUtils.accumulate(this, user, userSubjects, day);
    }

    public boolean haveGroup(String group) {
        return groups.containsKey(group);
    }

    public ArrayList<String> groups() {
        return new ArrayList<>(groups.keySet());
    }

    public Group get(String group) {
        return groups.get(group);
    }
}
