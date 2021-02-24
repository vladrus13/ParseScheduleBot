package bot.commands;

import bot.ParseScheduleBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class AboutCommand extends CommandWithKeyboard {
    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    private final ArrayList<String> command = new ArrayList<>(List.of("/about"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/about - информация о боте и обновлениях";
    }

    @Override
    public String getReplyCommand(String start, String command) {
        if (command.equals("О боте")) {
            return "/about";
        }
        if (command.equals("Че происходит, какие то кнопки, что случилось?")) {
            return "/about";
        }
        return null;
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String text) {
        sendMessage.setText("""
                Ура! Бот получил долгожданное (не очень) обновление и теперь он получил бета версию, из которой никогда и не выходил(
                Что нового пришло? В принципе, ничего, но теперь оно в разы удобнее!
                                
                1. Вы можете в настройках выбрать группу, в которой учитесь. Советуем сделать это заранее, весь бот завязан на этом. Так же в настройках вы можете выбрать, какие предметы вы не хотите видеть из вашего расписания, а какие предметы из чужих групп хотите видеть.
                                
                2. Теперь у вас появилась возможность смотреть баллы прям из бота. Сообщите вашему старосте, если хотите обладать такой возможностью, и ему будут выжаны инструкции, как настроить бота так, что бы он показывал ваши баллы во вкладке "Успеваемость".
                                
                3. Плагины (тестовая версия)! Хотите сделать полезное расширения для бота, которое будет пользоваться расписанием, табличками с успеваемостью? Пишите @vladrus13!
                """);
    }
}
