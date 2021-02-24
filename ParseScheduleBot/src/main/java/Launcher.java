import bot.ParseScheduleBot;
import database.HibernateSessionFactory;
import holder.PluginHolder;
import holder.ResourcesReload;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import plugin.Plugin;
import plugin.scheduleTimer.ScheduleTimerPlugin;
import utils.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Launcher {

    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(Launcher.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        HibernateSessionFactory.getSessionFactory();
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        ParseScheduleBot parseScheduleBot = new ParseScheduleBot();
        PluginHolder.setPlugins(new ArrayList<>(List.of(new ScheduleTimerPlugin(parseScheduleBot))));
        ResourcesReload.resourcesReload();
        for (Plugin plugin : PluginHolder.getPlugins()) {
            plugin.onStart();
        }
        try {
            botsApi.registerBot(parseScheduleBot);
        } catch (TelegramApiRequestException e) {
            Writer.printStackTrace(logger, e);
        }
    }
}
