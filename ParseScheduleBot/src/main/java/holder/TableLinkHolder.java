package holder;

import bot.ParseScheduleBot;
import parser.table.Table;
import utils.Writer;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class TableLinkHolder {
    private final static Logger logger = Logger.getLogger(ParseScheduleBot.class.getName());

    private static final ReentrantLock lock = new ReentrantLock();
    private static Table table;

    public static Table get() {
        if (table == null) {
            if (!reload()) {
                logger.severe("Can't take table");
            }
        }
        return table;
    }

    public static boolean reload() {
        if (lock.isLocked()) return false;
        lock.lock();
        try {
            table = new Table();
        } catch (IOException e) {
            Writer.printStackTrace(logger, e);
        } finally {
            lock.unlock();
        }
        return true;
    }

    public static boolean isEmpty() {
        return table == null;
    }
}
