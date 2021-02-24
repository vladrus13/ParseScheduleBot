package bot.commands.settings;

import bean.UserPlugin;
import bot.ParseScheduleBot;
import bot.commands.CommandWithKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class SetPluginCommand extends CommandWithKeyboard {
    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    final ArrayList<String> command = new ArrayList<>(List.of("/addPlugin", "/removePlugin"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        // TODO add
        return "null";
    }

    @Override
    public String getReplyCommand(String start, String command) {
        if (start.startsWith("Включить")) {
            return "/addPlugin " + command.substring(9);
        }
        if (start.startsWith("Выключить")) {
            return "/removePlugin " + command.substring(10);
        }
        return null;
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String text) {
        List<UserPlugin> userPluginList = parseScheduleBot.getUserPluginService().getPluginByChatId(update.getMessage().getChatId());
        int positionOfSpace = text.indexOf(' ');
        String commandText = text.substring(0, positionOfSpace == -1 ? text.length() : positionOfSpace);
        String plugin = text.substring(positionOfSpace + 1);
        switch (commandText) {
            case "/addPlugin" -> {
                boolean exists = userPluginList.stream().anyMatch(element -> element.getPluginName().equals(plugin));
                if (exists) {
                    sendMessage.setText("Уже включен!");
                } else {
                    UserPlugin userPlugin = new UserPlugin();
                    userPlugin.setChatId(update.getMessage().getChatId());
                    userPlugin.setPluginName(plugin);
                    userPluginList.add(userPlugin);
                    parseScheduleBot.getUserPluginService().save(userPlugin);
                    sendMessage.setText("Успешно добавлено!");
                }
            }
            case "/removePlugin" -> {
                boolean exists = userPluginList.stream().anyMatch(element -> element.getPluginName().equals(plugin));
                if (!exists) {
                    sendMessage.setText("Плагин не найдет или уже выключен");
                } else {
                    UserPlugin userPlugin = userPluginList.stream().filter(element -> element.getPluginName().equals(plugin)).findAny().orElseThrow();
                    parseScheduleBot.getUserPluginService().remove(userPlugin);
                    sendMessage.setText("Успешно выключено!");
                }
            }
            default -> sendMessage.setText("Неизвестная команда");
        }
    }
}
