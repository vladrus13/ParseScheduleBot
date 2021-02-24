package bot.commands.academicPerformance;

import bean.User;
import bot.ParseScheduleBot;
import bot.commands.CommandWithKeyboard;
import holder.MainTableHolder;
import holder.TableResultHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GetStatCommand extends CommandWithKeyboard {
    private final Logger logger = Logger.getLogger(GetStatCommand.class.getName());

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    private final ArrayList<String> command = new ArrayList<>(List.of("/stat"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "Получает статистику";
    }

    @Override
    public String getReplyCommand(String start, String command) {
        if (command.startsWith("Статистика")) {
            return "/stat";
        }
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
        String group = user.getGroup();
        MainTableHolder.GroupTable table = MainTableHolder.get(group);
        if (table == null) {
            sendMessage.setText("У вашей группы нет таблички!");
            return;
        }
        ArrayList<ArrayList<String>> links = table.subjects;
        StringBuilder sb = new StringBuilder("Статистика:\n");
        for (ArrayList<String> link : links) {
            String name = link.get(0);
            sb.append(name).append(": ");
            try {
                String result = TableResultHolder.get(name, link.get(1), link.get(2), link.get(3), link.get(4), user.getName());
                sb.append(result).append("\n");
            } catch (Exception e) {
                Writer.printStackTrace(logger, e);
                sb.append("Ошибка получения!");
            }
        }
        sendMessage.setText(sb.toString());
    }
}
