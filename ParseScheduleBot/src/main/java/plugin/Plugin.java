package plugin;

import bean.Pair;
import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.skeleton.Skeleton;

import java.util.List;

public abstract class Plugin {

    protected final ParseScheduleBot parseScheduleBot;

    public Plugin(ParseScheduleBot parseScheduleBot) {
        this.parseScheduleBot = parseScheduleBot;
    }

    public abstract void onStart();

    public abstract List<Skeleton> getSkeletons();

    public abstract List<Pair<Skeleton, Command>> getCommands();

    public abstract String getName();

    public abstract String getSystemName();

    public abstract String description();
}
