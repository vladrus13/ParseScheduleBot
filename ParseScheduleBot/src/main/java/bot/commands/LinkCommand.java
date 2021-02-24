package bot.commands;

import bean.Pair;
import bean.User;
import bot.ParseScheduleBot;
import holder.MainTableHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LinkCommand extends CommandWithKeyboard {

    private final Logger logger = Logger.getLogger(LinkCommand.class.getName());

    @Override
    public String getReplyCommand(String start, String command) {
        if (start.equals("Ссылки")) {
            return "/link";
        }
        return null;
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String text) {
        User user;
        try {
            user = parseScheduleBot.getUserService().findOrCreate(update.getMessage().getChatId());
        } catch (Exception e) {
            sendMessage.setText("Can't create you!");
            Writer.printStackTrace(logger, e);
            return;
        }
        ArrayList<Pair<String, String>> values;
        MainTableHolder.GroupTable groupTable = MainTableHolder.get(user.getGroup());
        if (groupTable == null) {
            sendMessage.setText("Не могу найти вашу группу");
            return;
        }
        values = groupTable.links;
        if (values == null || values.isEmpty()) {
            sendMessage.setText("Не могу найти табличку");
            return;
        }
        StringBuilder message = new StringBuilder();
        for (Pair<String, String> row : values) {
            if (row.second == null) {
                if (message.length() > 2 && message.charAt(message.length() - 2) == '|') {
                    message.delete(message.length() - 2, message.length() - 1);
                }
                message
                        .append("\n<i>")
                        .append(row.first)
                        .append("</i>: \n");
            } else {
                message
                        .append("<a href=\"")
                        .append(row.second)
                        .append("\">")
                        .append(row.first)
                        .append("</a> | ");
            }
        }
        if (message.length() > 2 && message.charAt(message.length() - 2) == '|') {
            message.delete(message.length() - 2, message.length() - 1);
        }
        sendMessage.setText(message.toString());
        sendMessage.enableHtml(true);
        sendMessage.disableWebPagePreview();
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    final ArrayList<String> command = new ArrayList<>(List.of("/link"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/link - выдает ссылки для вашей группы\n";
    }
}
