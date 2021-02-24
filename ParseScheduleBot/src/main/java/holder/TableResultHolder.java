package holder;

import utils.Writer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TableResultHolder {
    private final static Logger logger = Logger.getLogger(TableResultHolder.class.getName());

    // nameTable +/ url +/ range +/ names +/ result
    private static Map<String, TableResult> table = new HashMap<>();
    private static Map<String, TableResult> old = new HashMap<>();

    private static String getId(String name, String url, String range, String names, String result) {
        return name + "/" + url + "/" + range + "/" + names + "/" + result;
    }

    public static String get(String nameTable, String url, String range, String names, String result, String name) throws IOException, GeneralSecurityException {
        String id = getId(nameTable, url, range, names, result);
        if (!table.containsKey(id)) {
            reload(nameTable, url, range, names, result);
        }
        try {
            ArrayList<TableResult.ResultPair> results = table.get(id).get();
            for (TableResult.ResultPair pair : results) {
                if (pair.name.contains(name)) {
                    return pair.result;
                }
            }
        } catch (Exception e) {
            Writer.printStackTrace(logger, e);
        }
        return null;
    }

    public static void reload() throws IOException, GeneralSecurityException {
        old = table;
        Map<String, TableResult> real = new HashMap<>();
        for (TableResult tableResult : old.values()) {
            real.put(getId(tableResult.name, tableResult.url, tableResult.range, tableResult.namesColumn, tableResult.resultsColumn),
                    new TableResult(tableResult.name, tableResult.url, tableResult.range, tableResult.namesColumn, tableResult.resultsColumn));
        }
        table = real;
    }

    public static void reload(String name, String url, String range, String names, String result) throws IOException, GeneralSecurityException {
        String id = getId(name, url, range, names, result);
        TableResult tableResult = new TableResult(name, url, range, names, result);
        table.put(id, tableResult);
    }
}
