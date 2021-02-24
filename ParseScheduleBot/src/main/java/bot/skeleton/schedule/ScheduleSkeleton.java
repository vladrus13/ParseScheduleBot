package bot.skeleton.schedule;

import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.commands.GetCommand;
import bot.skeleton.Skeleton;
import bot.skeleton.UsersSkeleton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleSkeleton extends Skeleton {

    private final GetCommand getCommand = new GetCommand();

    private final ArrayList<Command> container = new ArrayList<>(Collections.singleton(getCommand));

    public ScheduleSkeleton(Skeleton skeleton) {
        super(skeleton);
    }

    @Override
    public ArrayList<Command> containerGet() {
        return container;
    }

    @Override
    public void afterGet(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        String text = update.getMessage().getText();
        if (text.equals("<< Назад")) {
            sendMessage.setText("Возвращение в главное меню");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), parent);
        }
    }

    @Override
    public ReplyKeyboardMarkup getReplyKeyboard(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBo) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        ArrayList<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow first = new KeyboardRow();
        first.add("Сегодня");
        first.add("Завтра");
        first.add("Все");
        keyboardRowList.add(first);
        ArrayList<String> dayNames = new ArrayList<>(List.of("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "<< Назад"));
        for (String name : dayNames) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(name);
            keyboardRowList.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
