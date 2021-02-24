package bot.skeleton.settings;

import bean.User;
import bean.UserSubjects;
import bot.ParseScheduleBot;
import bot.commands.Command;
import bot.commands.settings.SetAddSubjectsCommand;
import bot.skeleton.Skeleton;
import bot.skeleton.UsersSkeleton;
import holder.TableLinkHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import parser.table.Day;
import parser.table.Group;
import parser.table.UniversityPair;
import utils.UserSubjectService;

import java.util.*;

public class SubjectsSkeleton extends Skeleton {

    private final ArrayList<Command> container = new ArrayList<>(
            List.of(new SetAddSubjectsCommand()));

    public SubjectsSkeleton(Skeleton skeleton) {
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
            sendMessage.setText("Возвращение в меню настроек");
            UsersSkeleton.set(update.getMessage().getChatId().toString(), parent);
        }
    }

    @Override
    public ReplyKeyboardMarkup getReplyKeyboard(SendMessage sendMessage, Update update, ParseScheduleBot parseScheduleBot) {
        User user = parseScheduleBot.getUserService().findOrCreate(update.getMessage().getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        if (user.getGroup() == null || user.getGroup().isEmpty()) {
            sendMessage.setText("Пожалуйста, выберите группу");
            return replyKeyboardMarkup;
        }
        String group = user.getGroup();
        UserSubjects userSubjects = parseScheduleBot.getUserSubjectService().findOrCreate(user.getChatId());
        ArrayList<String> added = UserSubjectService.getPercent(userSubjects.getAdded());
        ArrayList<String> removed = UserSubjectService.getPercent(userSubjects.getRemoved());
        Set<String> realSubjects = new HashSet<>();
        Set<String> otherSubjects = new HashSet<>();
        ArrayList<String> groups = TableLinkHolder.get().groups();
        Group us = TableLinkHolder.get().get(group);
        for (Day day : us.getDays()) {
            for (UniversityPair universityPair : day.getUniversityPairs()) {
                realSubjects.add(universityPair.getOdd().getName());
                realSubjects.add(universityPair.getEven().getName());
            }
        }
        for (String groupName : groups) {
            if (groupName.startsWith(group.substring(0, 3)) && !groupName.equals(group)) {
                Group other = TableLinkHolder.get().get(groupName);
                for (Day day : other.getDays()) {
                    for (UniversityPair universityPair : day.getUniversityPairs()) {
                        String name = universityPair.getOdd().getName();
                        if (!realSubjects.contains(name)) {
                            otherSubjects.add(name);
                        }
                        name = universityPair.getEven().getName();
                        if (!realSubjects.contains(name)) {
                            otherSubjects.add(name);
                        }
                    }
                }
            }
        }
        ArrayList<String> exist = new ArrayList<>();
        ArrayList<String> notExist = new ArrayList<>();
        for (String s : realSubjects) {
            if (removed.contains(s)) {
                notExist.add(s);
            } else {
                exist.add(s);
            }
        }
        for (String s : otherSubjects) {
            if (added.contains(s)) {
                exist.add(s);
            } else {
                notExist.add(s);
            }
        }
        Collections.sort(exist);
        Collections.sort(notExist);
        ArrayList<KeyboardRow> keyboardRowList = new ArrayList<>();
        for (String s : exist) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add("Убрать " + s);
            keyboardRowList.add(keyboardRow);
        }
        for (String s : notExist) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add("Добавить " + s);
            keyboardRowList.add(keyboardRow);
        }
        KeyboardRow back = new KeyboardRow();
        back.add("<< Назад");
        keyboardRowList.add(back);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
