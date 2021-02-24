package holder;

import google.GoogleTableResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class TableResult {

    public static class ResultPair {
        public final String name;
        public final String result;

        public ResultPair(String name, String result) {
            this.name = name;
            this.result = result;
        }
    }

    public final String name;
    public final String url;
    public final String range;
    public final String namesColumn;
    public final String resultsColumn;
    public ArrayList<ResultPair> result;
    private final ReentrantLock reentrantLock = new ReentrantLock();

    public TableResult(String name, String url, String range, String namesColumn, String resultsColumn) throws IOException, GeneralSecurityException {
        this.name = name;
        this.url = url;
        this.range = range;
        this.namesColumn = namesColumn;
        this.resultsColumn = resultsColumn;
        reload();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableResult that = (TableResult) o;
        return name.equals(that.name) && url.equals(that.url) && range.equals(that.range) && namesColumn.equals(that.namesColumn) && resultsColumn.equals(that.resultsColumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, range, namesColumn, resultsColumn);
    }

    public ArrayList<ResultPair> get() throws Exception {
        if (result == null) {
            if (!reload()) {
                throw new Exception("Can't reload table with url: " + url);
            }
        }
        return result;
    }

    public boolean reload() throws IOException, GeneralSecurityException {
        if (reentrantLock.isLocked()) return false;
        reentrantLock.lock();
        ArrayList<ResultPair> newResults = new ArrayList<>();
        List<List<Object>> responseNames = GoogleTableResponse.reload(url, range, namesColumn + ":" + namesColumn);
        List<List<Object>> responseResults = GoogleTableResponse.reload(url, range, resultsColumn + ":" + resultsColumn);
        for (int i = 0; i < Math.min(responseNames.size(), responseResults.size()); i++) {
            try {
                newResults.add(new ResultPair(responseNames.get(i).get(0).toString(), responseResults.get(i).get(0).toString()));
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        result = newResults;
        reentrantLock.unlock();
        return true;
    }
}
