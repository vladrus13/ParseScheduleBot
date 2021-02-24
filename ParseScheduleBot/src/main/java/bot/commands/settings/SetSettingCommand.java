package bot.commands.settings;

import bean.User;
import bot.ParseScheduleBot;
import bot.commands.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SetSettingCommand extends Command {

    private final Logger logger = Logger.getLogger(SetSettingCommand.class.getName());

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        String[] message = update.getMessage().getText().split(" ");
        if (message.length != 5) {
            sendMessage.setText("Неверный формат");
            return;
        }
        int settings = Integer.parseInt(message[1]) + Integer.parseInt(message[2]) * 2 + Integer.parseInt(message[3]) * 4 + Integer.parseInt(message[4]) * 8;
        try {
            User user = parseScheduleBot.getUserService().findOrCreate(update.getMessage().getChatId());
            user.setUsername(update.getMessage().getFrom().getUserName());
            user.setSettings(settings);
            parseScheduleBot.getUserService().update(user);
        } catch (Exception e) {
            Writer.printStackTrace(logger, e);
            return;
        }
        sendMessage.setText("Successful set");
    }

    final ArrayList<String> command = new ArrayList<>(List.of("/setSettings"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/setSettings <показывать ли конец пары> <показывать ли преподавателя> <компактно ли> <моноширинное отображение> - (меню настроек) в формате 0/1";
    }
}
