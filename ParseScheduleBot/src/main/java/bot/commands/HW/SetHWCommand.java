package bot.commands.HW;

import bean.User;
import bot.ParseScheduleBot;
import bot.commands.CommandWithKeyboard;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SetHWCommand extends CommandWithKeyboard {

    private final Logger logger = Logger.getLogger(SetHWCommand.class.getName());

    @Override
    public String getReplyCommand(String start, String command) {
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
        try {
            Files.createDirectories(Path.of("../ParseScheduleTable/files/").resolve(user.getGroup()));
            Files.deleteIfExists(Path.of("../ParseScheduleTable/files/").resolve(user.getGroup())
                    .resolve(text.split(" ")[0] + ".jpg"));
            Files.deleteIfExists(Path.of("../ParseScheduleTable/files/").resolve(user.getGroup())
                    .resolve(text.split(" ")[0] + ".txt"));
        } catch (IOException e) {
            Writer.printStackTrace(logger, e);
        }
        if (update.getMessage().hasDocument()) {
            Document document = update.getMessage().getDocument();
            GetFile getFile = new GetFile();
            getFile.setFileId(document.getFileId());
            try {
                String filePath = parseScheduleBot.execute(getFile).getFilePath();
                parseScheduleBot.downloadFile(filePath,
                        new File(Path.of("../ParseScheduleTable/files/").resolve(user.getGroup())
                                .resolve(text.split(" ")[1] + ".jpg").toString()));
            } catch (TelegramApiException e) {
                Writer.printStackTrace(logger, e);
            }
        }
        if (text.split(" ").length > 1) {
            String result = Arrays.stream(text.split(" ")).skip(2).collect(Collectors.joining(" "));
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of("../ParseScheduleTable/files/")
                    .resolve(user.getGroup()).resolve(text.split(" ")[1] + ".txt"))) {
                bufferedWriter.write(result);
            } catch (IOException e) {
                Writer.printStackTrace(logger, e);
            }
        }
        sendMessage.setText("Successfully!");
    }

    @Override
    public void run(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        String text = update.getMessage().getText();
        if (text == null) {
            text = update.getMessage().getCaption();
        }
        run(sendMessage, update, parseScheduleBot, text);
    }

    final ArrayList<String> commands = new ArrayList<>(List.of("Задать"));

    @Override
    public ArrayList<String> getCommand() {
        return commands;
    }

    @Override
    public String getHelp() {
        return "Загружает ДЗ для указанного предмета";
    }
}
