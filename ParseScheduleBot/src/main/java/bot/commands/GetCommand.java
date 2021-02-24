package bot.commands;

import bean.User;
import bean.UserSubjects;
import bot.ParseScheduleBot;
import holder.TableLinkHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Writer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class GetCommand extends CommandWithKeyboard {

    private final Logger logger = Logger.getLogger(GetCommand.class.getName());

    @Override
    public String getReplyCommand(String start, String command) {
        switch (start) {
            case "Понедельник" -> {
                return "/get d Понедельник";
            }
            case "Вторник" -> {
                return "/get d Вторник";
            }
            case "Среда" -> {
                return "/get d Среда";
            }
            case "Четверг" -> {
                return "/get d Четверг";
            }
            case "Пятница" -> {
                return "/get d Пятница";
            }
            case "Суббота" -> {
                return "/get d Суббота";
            }
            case "Завтра" -> {
                return "/get 1";
            }
            case "Сегодня" -> {
                return "/get";
            }
            case "Все" -> {
                return "/get all";
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String message) {
        if (TableLinkHolder.isEmpty()) {
            if (!TableLinkHolder.reload()) {
                sendMessage.setText("К сожалению, существуют какие то проблемы с табличкой");
                return;
            }
        }
        Long from = update.getMessage().getChatId();
        int toGo = 0;
        boolean isRealDay = false;
        if (message.split(" ").length > 1) {
            String to = message.split(" ")[1];
            if (Character.isAlphabetic(to.charAt(0))) {
                if ("all".equals(to)) {
                    toGo = -1;
                } else {
                    isRealDay = true;
                    String third = message.split(" ")[2];
                    if ("Вторник".equals(third)) {
                        toGo = 1;
                    }
                    if ("Среда".equals(third)) {
                        toGo = 2;
                    }
                    if ("Четверг".equals(third)) {
                        toGo = 3;
                    }
                    if ("Пятница".equals(third)) {
                        toGo = 4;
                    }
                    if ("Суббота".equals(third)) {
                        toGo = 5;
                    }
                }
            } else {
                toGo = Integer.parseInt(message.split(" ")[1]);
                toGo %= 7;
                toGo += 7;
            }
        }
        User user;
        UserSubjects userSubjects;
        try {
            user = parseScheduleBot.getUserService().findOrCreate(from);
            userSubjects = parseScheduleBot.getUserSubjectService().findOrCreate(from);
        } catch (Exception e) {
            Writer.printStackTrace(logger, e);
            sendMessage.setText("Ошибка поиска вас в базе данных");
            return;
        }
        if (Objects.equals(user.getGroup(), "")) {
            sendMessage.setText("Выберите группу");
            return;
        }
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        String text;
        if (isRealDay) {
            text = TableLinkHolder.get().toString(user.getGroup(), (toGo + 7) % 7, user, userSubjects);
        } else {
            if (toGo == -1) {
                text = TableLinkHolder.get().toString(user.getGroup(), user, userSubjects);
            } else {
                text = TableLinkHolder.get().toString(user.getGroup(), (day + toGo + 13) % 7, user, userSubjects);
            }
        }
        if (user.isCodeShow()) {
            text = "<code>" + text + "</code>";
        }
        sendMessage.setText(text);
        sendMessage.enableHtml(true);
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    private final ArrayList<String> command = new ArrayList<>(List.of("/get"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/get <day> - получить расписание на day вперед (если не указано - 0). /get all выдает все расписание\n";
    }
}
