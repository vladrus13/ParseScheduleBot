package bot;

import bot.skeleton.UsersSkeleton;
import database.UserPluginService;
import database.UserService;
import database.UserSubjectService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.Writer;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class ParseScheduleBot extends TelegramLongPollingBot {

    private String token = "";
    private final UserService userService = new UserService();
    private final UserSubjectService userSubjectService = new UserSubjectService();
    private final UserPluginService userPluginService = new UserPluginService();
    private final Logger logger = Logger.getLogger(ParseScheduleBot.class.getName());

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && (update.getMessage().hasText() || update.getMessage().hasDocument())) {
            String text = update.getMessage().getText();
            if (text == null) {
                text = update.getMessage().getCaption();
            }
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
            logger.info("User: " + update.getMessage().getFrom().getUserName() + " send command: " + text);
            try {
                UsersSkeleton.get(update.getMessage().getChatId().toString()).get(sendMessage, update, this);
            } catch (Exception e) {
                Writer.printStackTrace(logger, e);
            }
            try {
                if (sendMessage.getText() == null || sendMessage.getText().isEmpty()) {
                    sendMessage.setText("Я не знаю этой команды или же произошла какая-то ошибка!\n" +
                            "Мы знаем, что это произошло (наверное) и уже скоро это исправим)");
                }
                execute(sendMessage);
            } catch (Exception e) {
                Writer.printStackTrace(logger, e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "ParseScheduleBot";
    }

    @Override
    public String getBotToken() {
        if (token.isEmpty()) {
            Properties properties = new Properties();
            try {
                properties.load(ParseScheduleBot.class.getResourceAsStream("/properties.properties"));
                token = properties.getProperty("BOT_TOKEN");
            } catch (IOException exception) {
                System.err.println("Can't load properties, trying get another method");
                token = System.getenv("BOT_TOKEN");
                if (token == null || Objects.equals(token, "")) {
                    System.err.println("Can't find \"BOT_TOKEN\" variable in Environment Variables, throwing exception");
                    exception.printStackTrace();
                }
            }
        }
        return token;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserSubjectService getUserSubjectService() {
        return userSubjectService;
    }

    public UserPluginService getUserPluginService() {
        return userPluginService;
    }
}
