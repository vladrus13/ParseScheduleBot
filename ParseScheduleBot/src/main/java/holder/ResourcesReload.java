package holder;

import utils.Writer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ResourcesReload {

    private final static Logger logger = Logger.getLogger(ResourcesReload.class.getName());
    private final static ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

    public static void resourcesReload() {
        Runnable task = () -> {
            try {
                TableResultHolder.reload();
            } catch (IOException | GeneralSecurityException e) {
                Writer.printStackTrace(logger, e);
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        schedule.scheduleAtFixedRate(task, 100 * 10 * 10, 5, TimeUnit.MINUTES);
    }
}
