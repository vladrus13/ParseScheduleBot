package bot.commands.HW;

import bean.User;
import bot.ParseScheduleBot;
import bot.commands.CommandWithKeyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GetHWCommand extends CommandWithKeyboard {

    private final Logger logger = Logger.getLogger(GetHWCommand.class.getName());

    @Override
    public String getReplyCommand(String start, String finish) {
        return null;
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot, String text) {
        User user;
        try {
            user = parseScheduleBot.getUserService().findOrCreate(update.getMessage().getChatId());
        } catch (Exception e) {
            Writer.printStackTrace(logger, e);
            return;
        }
        if (Objects.equals(user.getGroup(), "")) {
            sendMessage.setText("Choose the group");
            return;
        }
        if (text.split(" ").length < 2) {
            sendMessage.setText("Не могу найти дз по этому предмету!");
            return;
        }
        if (Files.exists(Path.of("../ParseScheduleTable/files/").resolve(user.getGroup())
                .resolve(text.split(" ")[1] + ".jpg"))) {
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(user.getChatId());
            sendDocument.setDocument(new File(String.valueOf(Path.of("../ParseScheduleTable/files/").resolve(user.getGroup())
                    .resolve(text.split(" ")[1] + ".jpg"))));
            try {
                parseScheduleBot.execute(sendDocument);
            } catch (TelegramApiException e) {
                Writer.printStackTrace(logger, e);
            }
        }
        String answer = "";
        if (Files.exists(Path.of("../ParseScheduleTable/files/").resolve(user.getGroup())
                .resolve(text.split(" ")[1] + ".txt"))) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of("../ParseScheduleTable/files/").resolve(user.getGroup())
                    .resolve(text.split(" ")[1] + ".txt"))) {
                answer = bufferedReader.lines().collect(Collectors.joining("\n"));
            } catch (IOException ignored) {
            }
        }
        if (answer.equals("")) {
            answer = "Empty!)";
        }
        sendMessage.setText(answer);
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        run(sendMessage, update, parseScheduleBot, update.getMessage().getText());
    }

    final ArrayList<String> commands = new ArrayList<>(List.of("Получить"));

    @Override
    public ArrayList<String> getCommand() {
        return commands;
    }

    @Override
    public String getHelp() {
        return "Получение всех дз, которые есть для этого предмета";
    }
}
