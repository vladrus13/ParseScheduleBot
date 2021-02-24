package plugin.scheduleTimer;

import bean.Pair;
import bean.User;
import bean.UserPlugin;
import bean.UserSubjects;
import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.skeleton.Skeleton;
import database.UserPluginService;
import database.UserService;
import database.UserSubjectService;
import holder.TableLinkHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import plugin.Plugin;
import utils.Writer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ScheduleTimerPlugin extends Plugin {

    private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
    private final UserPluginService userPluginService = new UserPluginService();
    private final UserService userService = new UserService();
    private final UserSubjectService userSubjectService = new UserSubjectService();
    private final Logger logger = Logger.getLogger(ScheduleTimerPlugin.class.getName());

    public ScheduleTimerPlugin(ParseScheduleBot parseScheduleBot) {
        super(parseScheduleBot);
    }

    @Override
    public void onStart() {
        Runnable task = () -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            List<UserPlugin> users = userPluginService.getPluginByPluginName(getSystemName());
            for (UserPlugin userPlugin : users) {
                User user = userService.findOrCreate(userPlugin.getChatId());
                UserSubjects userSubjects = userSubjectService.findOrCreate(user.getChatId());
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(user.getChatId());
                if (user.getGroup() == null || user.getGroup().equals("")) {
                    sendMessage.setText("Выберите группу!");
                }
                sendMessage.setText(TableLinkHolder.get().toString(user.getGroup(), day, user, userSubjects));
                try {
                    parseScheduleBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    Writer.printStackTrace(logger, e);
                }
            }
        };
        Calendar calendar = Calendar.getInstance();
        Calendar next9 = Calendar.getInstance();
        next9.set(Calendar.HOUR_OF_DAY, 9);
        next9.set(Calendar.MINUTE, 0);
        next9.set(Calendar.SECOND, 0);
        if (calendar.after(next9)) {
            next9.add(Calendar.DAY_OF_YEAR, 1);
        }
        schedule.scheduleAtFixedRate(task, next9.getTimeInMillis() - System.currentTimeMillis(), 1, TimeUnit.DAYS);
    }

    @Override
    public List<Skeleton> getSkeletons() {
        return new ArrayList<>();
    }

    @Override
    public List<Pair<Skeleton, Command>> getCommands() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "Расписание-таймер";
    }

    @Override
    public String getSystemName() {
        return "ScheduleTimerPlugin";
    }

    @Override
    public String description() {
        return "Если включите это расширение, бот каждый день в 9:00 будет присылать вам расписание на сегодня";
    }
}
