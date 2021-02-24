package holder;

import bean.Pair;
import google.GoogleTableResponse;
import utils.Writer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class MainTableHolder {
    private static final Logger logger = Logger.getLogger(MainTableHolder.class.getName());

    public static class GroupTable {
        public final ArrayList<Pair<String, String>> links;
        public final ArrayList<ArrayList<String>> subjects;

        GroupTable(String url, String name) throws IOException, GeneralSecurityException {
            links = new ArrayList<>();
            subjects = new ArrayList<>();
            List<List<Object>> linksObjects = GoogleTableResponse.reload(url, name, "A:B");
            List<List<Object>> subjectsObjects = GoogleTableResponse.reload(url, name, "D:H");
            for (List<Object> it : linksObjects) {
                links.add(new Pair<>((String) it.get(0), it.size() > 1 ? it.get(1).toString() : null));
            }
            if (subjectsObjects != null) {
                for (List<Object> it : subjectsObjects) {
                    try {
                        subjects.add(new ArrayList<>(List.of(
                                it.get(0).toString(),
                                it.get(1).toString(),
                                it.get(2).toString(),
                                it.get(3).toString(),
                                it.get(4).toString()
                        )));
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
    }

    private static final Map<String, GroupTable> table = new ConcurrentHashMap<>();

    public static void reload(final String name) {
        String url = "id";
        try {
            table.put(name, new GroupTable(url, name));
        } catch (Exception e) {
            Writer.printStackTrace(logger, e);
        }
    }

    public static GroupTable get(final String name) {
        if (name == null) {
            return null;
        }
        if (!table.containsKey(name)) {
            reload(name);
        }
        return table.get(name);
    }
}
