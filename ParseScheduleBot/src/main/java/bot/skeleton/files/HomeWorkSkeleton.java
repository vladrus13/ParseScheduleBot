package bot.skeleton.files;

import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.commands.HW.GetHWCommand;
import bot.commands.HW.SetHWCommand;
import bot.skeleton.Skeleton;
import bot.skeleton.UsersSkeleton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class HomeWorkSkeleton extends Skeleton {

    private final GetHWCommand getHWCommand = new GetHWCommand();
    private final SetHWCommand setHWCommand = new SetHWCommand();
    private final ArrayList<Command> container = new ArrayList<>(List.of(getHWCommand, setHWCommand));

    public HomeWorkSkeleton(Skeleton skeleton) {
        super(skeleton);
    }

    @Override
    public ArrayList<Command> containerGet() {
        return container;
    }

    @Override
    public void afterGet(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        if (update.getMessage().getText().equals("<< Назад")) {
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
        first.add("Получить");
        first.add("Задать");
        first.add("<< Назад");
        keyboardRowList.add(first);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
