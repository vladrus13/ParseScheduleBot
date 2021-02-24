package bot.commands.admin;

import bean.User;
import bot.ParseScheduleBot;
import bot.commands.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AlertCommand extends Command {

    private final Logger logger = Logger.getLogger(AlertCommand.class.getName());

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        User messager;
        try {
            messager = parseScheduleBot.getUserService().findOrCreate(update.getMessage().getChatId());
        } catch (Exception e) {
            Writer.printStackTrace(logger, e);
            sendMessage.setText("Возникли проблемы с поиском вашего аккаунта в базе данных");
            return;
        }
        if (messager.getRole() < 5) {
            sendMessage.setText("Недостаточно прав!");
            return;
        }
        List<User> allUsers;
        try {
            allUsers = parseScheduleBot.getUserService().getAll();
        } catch (Exception e) {
            Writer.printStackTrace(logger, e);
            return;
        }
        String text = update.getMessage().getText();
        String message = text.substring(text.indexOf(' '));
        for (User user : allUsers) {
            SendMessage send = new SendMessage().setChatId(user.getChatId()).setText(message);
            try {
                parseScheduleBot.execute(send);
            } catch (TelegramApiException e) {
                Writer.printStackTrace(logger, e);
                sendMessage.setText("Error!");
            }
        }
        sendMessage.setText("Done!");
    }

    private final ArrayList<String> command = new ArrayList<>(List.of("/alert"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/alert <text> - Совершает уведомление всех пользователей, которые пользуются ботом";
    }
}
