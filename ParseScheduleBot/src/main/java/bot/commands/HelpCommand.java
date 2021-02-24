package bot.commands;

import bot.ParseScheduleBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends CommandWithKeyboard {
    @Override
    public String getReplyCommand(String start, String command) {
        if (start.equals("Помощь")) {
            return "/help";
        }
        return null;
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String text) {
        sendMessage.setText(
                """
                        Добро пожаловать в справку! Внимание! Не все команды доступны во всех меню!
                        /start - приветственное окно
                        /reloadTable - если вам показалось, что расписание изменилось, смело жми (не нажимать слишком часто!)
                        /reloadLink - если вы староста, и изменили табличку со ссылками
                        /link - выдает ссылки для вашей группы
                        /addSubjects - (меню настроек) добавляет предметы из других групп. Формат - <group1> <day1> <pair1> <group2> ... - где группа в формате M3***, день и пара числа от 0
                        /help - вызов данного меню
                        /get <day> - получить расписание на day вперед (если не указано - 0). /get all выдает все расписание
                        /setGroup <group> - (меню настроек) выбрать группу в формате M3***
                        /ignoreSubjects <subject1> <subject2> ... - (меню настроек) выбрать игнорируемые для Вас предметы (например, предметы по выбору, которые вы не выбрали, или предметы, на которые вы не ходите (осуждаю))
                        /setSettings <показывать ли конец пары> <показывать ли преподавателя> <компактно ли> <моноширинное отображение> - (меню настроек) в формате 0/1"""
        );
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    private final ArrayList<String> command = new ArrayList<>(List.of("/help"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/help - вызов меню справки";
    }
}
