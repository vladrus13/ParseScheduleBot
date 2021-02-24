package bot.skeleton;

import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.commands.CommandWithKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.ArrayList;

public abstract class Skeleton {
    protected final Skeleton parent;

    public Skeleton(Skeleton parent) {
        this.parent = parent;
    }

    public Skeleton() {
        this.parent = null;
    }

    public abstract ArrayList<Command> containerGet();

    public void get(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        String text = update.getMessage().getText();
        if (text == null) {
            text = update.getMessage().getCaption();
        }
        int positionOfSpace = text.indexOf(' ');
        String commandText = text.substring(0, positionOfSpace == -1 ? text.length() : positionOfSpace);
        for (Command command : containerGet()) {
            if (command.getCommand().contains(commandText)) {
                command.run(sendMessage, update, parseScheduleBot);
                return;
            }
        }
        for (Command command : containerGet()) {
            if (command instanceof CommandWithKeyboard) {
                CommandWithKeyboard commandWithKeyboard = (CommandWithKeyboard) command;
                if (commandWithKeyboard.isAcceptedCommand(commandText, text)) {
                    commandWithKeyboard.run(sendMessage, update, parseScheduleBot, commandWithKeyboard.getAcceptedCommand(commandText, text));
                    return;
                }
            }
        }
        afterGet(sendMessage, update, parseScheduleBot);
        sendMessage.setReplyMarkup(UsersSkeleton.get(sendMessage.getChatId()).getReplyKeyboard(sendMessage, update, parseScheduleBot));
    }

    public abstract void afterGet(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot);

    public ReplyKeyboard getReplyKeyboard(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBo) {
        return null;
    }

    public Skeleton getParent() {
        return parent;
    }
}
