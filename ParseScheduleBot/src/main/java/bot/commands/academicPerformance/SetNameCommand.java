package bot.commands.academicPerformance;

import bean.User;
import bot.ParseScheduleBot;
import bot.commands.CommandWithKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Writer;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SetNameCommand extends CommandWithKeyboard {
    private final Logger logger = Logger.getLogger(SetNameCommand.class.getName());

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    @Override
    public ArrayList<String> getCommand() {
        return new ArrayList<>();
    }

    @Override
    public String getHelp() {
        return "Устанавливает имя";
    }

    @Override
    public String getReplyCommand(String start, String command) {
        if (command.matches("[А-Яа-я ]+")) return command;
        return null;
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String text) {
        User user;
        try {
            user = parseScheduleBot.getUserService().findOrCreate(update.getMessage().getChatId());
        } catch (Exception e) {
            Writer.printStackTrace(logger, e);
            sendMessage.setText("Ошибка поиска вас в базе данных");
            return;
        }
        user.setName(text);
        try {
            parseScheduleBot.getUserService().update(user);
        } catch (Exception e) {
            Writer.printStackTrace(logger, e);
            sendMessage.setText("Ошибка при сохранении данных");
            return;
        }
        sendMessage.setText("Успешно выполнено!");
    }
}
