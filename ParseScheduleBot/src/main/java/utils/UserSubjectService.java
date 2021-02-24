package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class UserSubjectService {

    public static ArrayList<String> getPercent(String added) {
        return Arrays.stream(added.split("%")).collect(Collectors.toCollection(ArrayList::new));
    }

    public static String setPercent(ArrayList<String> added) {
        return String.join("%", added);
    }
}