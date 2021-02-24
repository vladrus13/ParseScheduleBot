package bot.commands;

import bean.User;
import bot.ParseScheduleBot;
import holder.MainTableHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReloadLinkCommand extends Command {

    private final Logger logger = Logger.getLogger(ReloadLinkCommand.class.getName());

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        User user;
        try {
            user = parseScheduleBot.getUserService().findOrCreate(update.getMessage().getChatId());
        } catch (Exception e) {
            sendMessage.setText("Can't create you!");
            Writer.printStackTrace(logger, e);
            return;
        }
        MainTableHolder.reload(user.getGroup());
        sendMessage.setText("Successful reload!");
    }

    final ArrayList<String> command = new ArrayList<>(List.of("/reloadLink"));

    @Override
    public ArrayList<String> getCommand() {
        return command;
    }

    @Override
    public String getHelp() {
        return "/reloadLink - если вы староста, и изменили табличку со ссылками";
    }
}
