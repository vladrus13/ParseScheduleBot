package bot.skeleton.settings;

import bean.UserPlugin;
import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.commands.LinkCommand;
import bot.commands.settings.SetPluginCommand;
import bot.skeleton.Skeleton;
import bot.skeleton.UsersSkeleton;
import holder.PluginHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import plugin.Plugin;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PluginSkeleton extends Skeleton {

    private final Logger logger = Logger.getLogger(LinkCommand.class.getName());

    private final ArrayList<Command> container = new ArrayList<>(
            List.of(new SetPluginCommand()));

    public PluginSkeleton(Skeleton skeleton) {
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
        Set<String> userPluginList =
                parseScheduleBot
                        .getUserPluginService()
                        .getPluginByChatId(update.getMessage().getChatId())
                        .stream()
                        .map(UserPlugin::getPluginName)
                        .collect(Collectors.toSet());
        ArrayList<Plugin> exists = PluginHolder.getPlugins();
        for (Plugin plugin : exists) {
            SendMessage sendMessagePlugin = new SendMessage();
            sendMessagePlugin.setChatId(update.getMessage().getChatId());
            sendMessagePlugin.setText(String.format("%s\n\n%s", plugin.getName(), plugin.description()));
            try {
                parseScheduleBot.execute(sendMessagePlugin);
            } catch (TelegramApiException e) {
                Writer.printStackTrace(logger, e);
            }
            String text;
            if (userPluginList.contains(plugin.getSystemName())) {
                text = "Выключить " + plugin.getSystemName();
            } else {
                text = "Включить " + plugin.getSystemName();
            }
            KeyboardRow next = new KeyboardRow();
            next.add(text);
            keyboardRowList.add(next);
        }
        KeyboardRow back = new KeyboardRow();
        back.add("<< Назад");
        keyboardRowList.add(back);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
