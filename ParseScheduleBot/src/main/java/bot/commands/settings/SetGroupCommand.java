package bot.commands.settings;

import bean.User;
import bot.ParseScheduleBot;
import bot.commands.CommandWithKeyboard;
import holder.TableLinkHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SetGroupCommand extends CommandWithKeyboard {

    private final Logger logger = Logger.getLogger(SetGroupCommand.class.getName());

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    final ArrayList<String> command = new ArrayList<>(List.of("/setGroup"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/setGroup - (меню настроек) выбрать группу в формате M3***";
    }

    @Override
    public String getReplyCommand(String start, String command) {
        if (start.equals("В")) {
            return "/setGroup" + command.substring(1);
        }
        return null;
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String text) {
        if (text.split(" ").length < 2) {
            sendMessage.setText("Неверный формат");
            return;
        }
        User user;
        try {
            user = parseScheduleBot.getUserService().findOrCreate(update.getMessage().getChatId());
        } catch (Exception e) {
            sendMessage.setText("Ошибка нахождения вас в базе данных");
            Writer.printStackTrace(logger, e);
            return;
        }
        String group = text.split(" ")[1];
        user.setGroup(group);
        try {
            parseScheduleBot.getUserService().update(user);
        } catch (Exception e) {
            sendMessage.setText("Не могу обновить данные");
            Writer.printStackTrace(logger, e);
            return;
        }
        boolean have = TableLinkHolder.get().haveGroup(group);
        if (!have) {
            group += "\nWARNING: table not contain this group. Please, reload the table or check input";
        }
        sendMessage.setText("Successful set: " + group);
    }
}
