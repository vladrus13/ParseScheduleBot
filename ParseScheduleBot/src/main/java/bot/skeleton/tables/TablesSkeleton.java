package bot.skeleton.tables;

import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.commands.academicPerformance.GetStatCommand;
import bot.skeleton.Skeleton;
import bot.skeleton.UsersSkeleton;
import bot.skeleton.tables.nameChange.NameChangeSkeleton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class TablesSkeleton extends Skeleton {

    private final ArrayList<Command> container = new ArrayList<>(
            List.of(new GetStatCommand()));

    private final NameChangeSkeleton nameChangeSkeleton = new NameChangeSkeleton(this);

    public TablesSkeleton(Skeleton skeleton) {
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
        if (text.equals("Сменить имя")) {
            sendMessage.setText("Введите имя");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), nameChangeSkeleton);
        }
    }

    @Override
    public ReplyKeyboardMarkup getReplyKeyboard(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        ArrayList<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow first = new KeyboardRow();
        first.add("Сменить имя");
        first.add("Статистика");
        KeyboardRow back = new KeyboardRow();
        back.add("<< Назад");
        keyboardRowList.add(first);
        keyboardRowList.add(back);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
