package bot.commands;

import bot.ParseScheduleBot;
import holder.TableLinkHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class ReloadTableCommand extends Command {

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        if (TableLinkHolder.reload()) {
            sendMessage.setText("Произошла успешная перезагрузка");
        } else {
            sendMessage.setText("Что-то пошло не так, стактрейс уже в пути!");
        }
    }

    final ArrayList<String> command = new ArrayList<>(List.of("/reloadTable"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/reloadTable - если вам показалось, что расписание изменилось, смело жми (не нажимать слишком часто!)";
    }
}
