package utils;

import bean.User;
import bean.UserSubjects;
import parser.table.Day;
import parser.table.Lesson;
import parser.table.Table;
import parser.table.UniversityPair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableUtils {

    private static final ArrayList<String> daysNames = new ArrayList<>(List.of(
            "Понедельник",
            "Вторник",
            "\uD83D\uDC38Wednesday",
            "Четверг",
            "Пятница",
            "Суббота",
            "Воскресенье"
    ));

    private static final ArrayList<String> timesStart = new ArrayList<>(List.of(
            "8:20",
            "10:00",
            "11:40",
            "13:30",
            "15:20",
            "17:00",
            "18:40",
            "20:20"
    ));

    private static final ArrayList<String> timesEnd = new ArrayList<>(List.of(
            "9:50",
            "11:30",
            "13:10",
            "15:00",
            "16:50",
            "18:30",
            "20:10",
            "21:50"
    ));

    private static void accumulate(User user, StringBuilder accumulator, Lesson lesson) {
        accumulator.append(lesson.getName()).append(" ").append(lesson.getPlace()).append(user.isShowTeacher() ? " " + lesson.getTeacher() : "");
    }

    private static StringBuilder accumulate(User user, UserSubjects userSubjects, Lesson lesson, boolean checkedAdded, boolean checkedKicked, String time, Boolean isOdd) {
        if (!lesson.isEmpty()) {
            if (!checkedAdded || UserSubjectService.getPercent(userSubjects.getAdded()).contains(lesson.getName())) {
                if (!checkedKicked || !UserSubjectService.getPercent(userSubjects.getRemoved()).contains(lesson.getName())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String week = "";
                    if (isOdd != null) {
                        week = isOdd ? " (нечет)" : " (чет)";
                    }
                    stringBuilder.append(time).append(week).append(":").append(user.isCompact() ? ' ' : '\n');

                    accumulate(user, stringBuilder, lesson);
                    return stringBuilder;
                }
            }
        }
        return new StringBuilder();
    }

    private static StringBuilder accumulate(User user, UserSubjects userSubjects, UniversityPair universityPair, boolean checkedAdded, boolean checkedKicked, String time) {
        StringBuilder stringBuilder;
        if (universityPair.getEven().equals(universityPair.getOdd())) {
            stringBuilder = accumulate(user, userSubjects, universityPair.getOdd(), checkedAdded, checkedKicked, time, null);
        } else {
            stringBuilder = accumulate(user, userSubjects, universityPair.getEven(), checkedAdded, checkedKicked, time, false);
            if (stringBuilder.length() > 0) {
                stringBuilder.append(System.lineSeparator());
            }
            stringBuilder.append(accumulate(user, userSubjects, universityPair.getOdd(), checkedAdded, checkedKicked, time, true));
        }
        return stringBuilder;
    }

    private static void accumulate(Table table, User user, UserSubjects userSubjects, ArrayList<String> nearGroups, int dayNumber, StringBuilder accumulator) {
        String group = user.getGroup();
        ArrayList<Day> days = table.get(group).getDays();
        if (days.size() <= dayNumber) {
            return;
        }
        Day day = days.get(dayNumber);
        accumulator.append(daysNames.get(dayNumber)).append(": ").append(user.isCompact() ? "" : System.lineSeparator());
        boolean isAdded = false;
        for (int i = 0; i < 8; i++) {
            String time = (user.isShowEndPair() ? timesStart.get(i) + "-" + timesEnd.get(i) : timesStart.get(i));
            StringBuilder stringBuilder = new StringBuilder();
            if (day.getUniversityPairs().size() <= i) {
                continue;
            }
            StringBuilder temporary = accumulate(user, userSubjects, day.getUniversityPairs().get(i), false, true, time);
            if (temporary.length() > 0) {
                stringBuilder.append(temporary).append(System.lineSeparator());
            }
            for (String nearGroup : nearGroups) {
                temporary = accumulate(user, userSubjects, table.get(nearGroup).getDays().get(dayNumber).getUniversityPairs().get(i), true, false, time);
                if (temporary.length() > 0) {
                    stringBuilder.append(temporary).append(System.lineSeparator());
                }
            }
            if (stringBuilder.length() > 0) {
                accumulator.append(stringBuilder).append(System.lineSeparator());
                isAdded = true;
            }
        }
        if (!isAdded) {
            accumulator.append("Пустой день! Поздравляю!").append(System.lineSeparator().repeat(2));
        } else {
            accumulator.append(System.lineSeparator());
        }
    }

    public static String accumulate(Table table, User user, UserSubjects userSubjects, Integer day) {
        String group = user.getGroup();
        ArrayList<String> nearGroups =
                table.groups().
                        stream().
                        filter(element -> element.startsWith(group.substring(0, 3)) && !element.equals(group)).
                        collect(Collectors.toCollection(ArrayList::new));
        StringBuilder accumulator = new StringBuilder();
        if (day == null) {
            for (int i = 0; i < 6; i++) {
                accumulate(table, user, userSubjects, nearGroups, i, accumulator);
            }
        } else {
            accumulate(table, user, userSubjects, nearGroups, day, accumulator);
        }
        return accumulator.length() == 0 ? "Пусто!" : accumulator.toString();
    }
}
