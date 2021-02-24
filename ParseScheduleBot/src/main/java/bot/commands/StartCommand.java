package bot.commands;

import bot.ParseScheduleBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class StartCommand extends Command {

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        sendMessage.setText("Привет! Это вспомогательный бот для КТ! Набери /help для получения информации по командам или (еще лучше) пользуйся удобными кнопочками, которые появились у тебя в меню ниже!");
    }

    final ArrayList<String> command = new ArrayList<>(List.of("/start"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/start - приветственное окно";
    }
}
