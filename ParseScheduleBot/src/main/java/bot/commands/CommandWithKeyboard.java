package bot.commands;

import bot.ParseScheduleBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class CommandWithKeyboard extends Command {

    public abstract String getReplyCommand(String start, String command);

    public boolean isAcceptedCommand(String start, String command) {
        return getReplyCommand(start, command) != null;
    }

    public String getAcceptedCommand(String start, String command) {
        return getReplyCommand(start, command);
    }

    public abstract void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String text);
}
