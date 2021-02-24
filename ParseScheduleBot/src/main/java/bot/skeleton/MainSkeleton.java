package bot.skeleton;

import bot.ParseScheduleBot;
import bot.commands.*;
import bot.commands.admin.AlertCommand;
import bot.commands.admin.GetAllCommand;
import bot.skeleton.files.HomeWorkSkeleton;
import bot.skeleton.schedule.ScheduleSkeleton;
import bot.skeleton.settings.SettingsSkeleton;
import bot.skeleton.tables.TablesSkeleton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MainSkeleton extends Skeleton {

    private final ScheduleSkeleton scheduleSkeleton = new ScheduleSkeleton(this);
    private final SettingsSkeleton settingsSkeleton = new SettingsSkeleton(this);
    private final HomeWorkSkeleton homeWorkSkeleton = new HomeWorkSkeleton(this);
    private final TablesSkeleton tablesSkeleton = new TablesSkeleton(this);

    private final ArrayList<Command> container = new ArrayList<>(
            List.of(new GetCommand(),
                    new HelpCommand(),
                    new LinkCommand(),
                    new StartCommand(),
                    new ReloadLinkCommand(),
                    new ReloadTableCommand(),
                    new GetAllCommand(),
                    new AlertCommand(),
                    new AboutCommand()));

    @Override
    public ArrayList<Command> containerGet() {
        return container;
    }

    @Override
    public void afterGet(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        String text = update.getMessage().getText();
        if (text.equals("Меню расписания")) {
            sendMessage.setText("Переход в меню расписания");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), scheduleSkeleton);
        }
        if (text.equals("Настройки")) {
            sendMessage.setText("Переход в настройки\n" +
                    "Команды доступны в /help");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), settingsSkeleton);
        }
        if (text.equals("HW")) {
            sendMessage.setText("Переход в раздел домашних заданий\n" +
                    "Команды доступны в /help");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), homeWorkSkeleton);
        }
        if (text.equals("Успеваемость")) {
            sendMessage.setText("Переход в меню успеваемости");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), tablesSkeleton);
        }
    }

    @Override
    public ReplyKeyboardMarkup getReplyKeyboard(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        ArrayList<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow h1 = new KeyboardRow();
        h1.add("Меню расписания");
        h1.add("Настройки");
        h1.add("Успеваемость");
        KeyboardRow h2 = new KeyboardRow();
        h2.add("Помощь");
        h2.add("Ссылки");
        h2.add("HW");
        KeyboardRow h3 = new KeyboardRow();
        h3.add("О боте");
        h3.add("Че происходит, какие то кнопки, что случилось?");
        keyboardRowList.add(h1);
        keyboardRowList.add(h2);
        keyboardRowList.add(h3);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
