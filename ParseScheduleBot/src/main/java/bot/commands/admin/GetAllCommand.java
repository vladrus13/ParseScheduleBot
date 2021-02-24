package bot.commands.admin;

import bean.User;
import bot.ParseScheduleBot;
import bot.commands.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GetAllCommand extends Command {

    private final Logger logger = Logger.getLogger(GetAllCommand.class.getName());

    @SuppressWarnings("OptionalGetWithoutIsPresent")
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
        StringBuilder message = new StringBuilder(String.format("<code>All users (%s):\n", allUsers.size()));
        int maxChatIdSize = allUsers.stream().mapToInt(element -> element.getChatId().toString().length()).max().getAsInt();
        int maxGroupSize = allUsers.stream().mapToInt(element -> element.getGroup().length()).max().getAsInt();
        message.append(" ".repeat(maxChatIdSize - "ChatId".length()))
                .append("ChatId")
                .append(" | ")
                .append("Group")
                .append(" ".repeat(maxGroupSize - "Group".length()))
                .append(" | ")
                .append("Username\n");
        for (User user : allUsers) {
            message.append(" ".repeat(maxChatIdSize - user.getChatId().toString().length()))
                    .append(user.getChatId())
                    .append(" | ").append(" ".repeat(maxGroupSize - user.getGroup().length())).append(user.getGroup())
                    .append(" | ").append(user.getUsername()).append("\n");
        }
        message.append("</code>");
        sendMessage.enableHtml(true);
        sendMessage.setText(message.toString());
    }

    private final ArrayList<String> command = new ArrayList<>(List.of("/getAll"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "Возвращает всех людей, которые пользуются ботом";
    }
}
