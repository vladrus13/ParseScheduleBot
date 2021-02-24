package bot.commands;

import bot.ParseScheduleBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

public abstract class Command {

    public abstract void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot);

    public abstract ArrayList<String> getCommand();

    public abstract String getHelp();
}
