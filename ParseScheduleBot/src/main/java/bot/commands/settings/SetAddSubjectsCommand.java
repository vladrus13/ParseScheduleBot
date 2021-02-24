package bot.commands.settings;

import bean.UserSubjects;
import bot.ParseScheduleBot;
import bot.commands.CommandWithKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.UserSubjectService;

import java.util.ArrayList;
import java.util.List;

public class SetAddSubjectsCommand extends CommandWithKeyboard {

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    final ArrayList<String> command = new ArrayList<>(List.of("/remove", "/add"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        // TODO Deprecated
        return "/addSubjects - (меню настроек) добавляет предметы из других групп. Формат - <group1> <day1> <pair1> <group2> ... - где группа в формате M3***, день и пара числа от 0";
    }

    @Override
    public String getReplyCommand(String start, String command) {
        if (start.equals("Добавить")) {
            return "/add " + command.substring(9);
        }
        if (start.equals("Убрать")) {
            return "/remove " + command.substring(7);
        }
        return null;
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String text) {
        UserSubjects userSubjects = parseScheduleBot.getUserSubjectService().findOrCreate(update.getMessage().getChatId());
        int positionOfSpace = text.indexOf(' ');
        String commandText = text.substring(0, positionOfSpace == -1 ? text.length() : positionOfSpace);
        String subject = text.substring(positionOfSpace + 1);
        switch (commandText) {
            case "/add" -> {
                ArrayList<String> removed = UserSubjectService.getPercent(userSubjects.getRemoved());
                if (removed.contains(subject)) {
                    removed.remove(subject);
                    userSubjects.setRemoved(UserSubjectService.setPercent(removed));
                } else {
                    ArrayList<String> added = UserSubjectService.getPercent(userSubjects.getAdded());
                    added.add(subject);
                    userSubjects.setAdded(UserSubjectService.setPercent(added));
                }
                parseScheduleBot.getUserSubjectService().save(userSubjects);
                sendMessage.setText("Успешно добавлено!");
            }
            case "/remove" -> {
                ArrayList<String> added = UserSubjectService.getPercent(userSubjects.getAdded());
                if (added.contains(subject)) {
                    added.remove(subject);
                    userSubjects.setAdded(UserSubjectService.setPercent(added));
                } else {
                    ArrayList<String> removed = UserSubjectService.getPercent(userSubjects.getRemoved());
                    removed.add(subject);
                    userSubjects.setRemoved(UserSubjectService.setPercent(removed));
                }
                parseScheduleBot.getUserSubjectService().save(userSubjects);
                sendMessage.setText("Успешно удалено!");
            }
            default -> sendMessage.setText("Неизвестная команда");
        }
    }
}
