package bot.skeleton.settings;

import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.commands.settings.SetGroupCommand;
import bot.skeleton.Skeleton;
import bot.skeleton.UsersSkeleton;
import holder.TableLinkHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetGroupSkeleton extends Skeleton {

    private final ArrayList<Command> container = new ArrayList<>(
            List.of(new SetGroupCommand()));

    public SetGroupSkeleton(Skeleton skeleton) {
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
    public ReplyKeyboardMarkup getReplyKeyboard(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        ArrayList<KeyboardRow> keyboardRowList = new ArrayList<>();
        ArrayList<String> groups = TableLinkHolder.get().groups();
        Collections.sort(groups);
        for (int i = 0; i < groups.size(); i += 2) {
            KeyboardRow group = new KeyboardRow();
            group.add("В " + groups.get(i));
            if (i + 1 < groups.size()) {
                group.add("В " + groups.get(i + 1));
            }
            keyboardRowList.add(group);
        }
        KeyboardRow back = new KeyboardRow();
        back.add("<< Назад");
        keyboardRowList.add(back);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
