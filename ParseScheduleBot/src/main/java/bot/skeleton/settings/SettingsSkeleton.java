package bot.skeleton.settings;

import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.commands.HelpCommand;
import bot.commands.settings.SetGroupCommand;
import bot.commands.settings.SetSettingCommand;
import bot.skeleton.Skeleton;
import bot.skeleton.UsersSkeleton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SettingsSkeleton extends Skeleton {

    private final SetGroupSkeleton setGroupSkeleton = new SetGroupSkeleton(this);
    private final SubjectsSkeleton subjectsSkeleton = new SubjectsSkeleton(this);
    private final PluginSkeleton pluginSkeleton = new PluginSkeleton(this);

    private final ArrayList<Command> container = new ArrayList<>(
            List.of(new SetGroupCommand(),
                    new SetSettingCommand(),
                    new HelpCommand()));

    public SettingsSkeleton(Skeleton skeleton) {
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
        if (text.equals("Переход")) {
            sendMessage.setText("Переход в меню смены группы");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), setGroupSkeleton);
        }
        if (text.equals("Убрать или добавить предметы")) {
            sendMessage.setText("Переход в меню выбора предметов. Внимание! Данная возможность дорогая с точки зрения машины. Не играйтесь с ней, вероятно, бот будет отвечать примерно за 2-3 секунды. Так же если вы уберете предмет, то в он уберется, но в кнопках обновление сразу не произойдет");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), subjectsSkeleton);
        }
        if (text.equals("Плагины")) {
            sendMessage.setText("Переход в меню выбора плагинов");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), pluginSkeleton);
        }
    }

    @Override
    public ReplyKeyboardMarkup getReplyKeyboard(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBo) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        ArrayList<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow first = new KeyboardRow();
        first.add("<< Назад");
        first.add("Помощь");
        KeyboardRow second = new KeyboardRow();
        second.add("Переход");
        second.add("Убрать или добавить предметы");
        KeyboardRow third = new KeyboardRow();
        second.add("Плагины");
        keyboardRowList.add(first);
        keyboardRowList.add(second);
        keyboardRowList.add(third);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
