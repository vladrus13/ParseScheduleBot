package database;

import bean.UserSubjects;

public class UserSubjectService {

    private final DataAccessObject<UserSubjects> dataAccessObject = new DataAccessObject<>(UserSubjects.class, "UserSubjects");

    public UserSubjects findByChatId(Long chatId) {
        return dataAccessObject.findById(chatId);
    }

    public int countByChatId(Long chatId) {
        UserSubjects userSubjects = findByChatId(chatId);
        if (userSubjects == null) return 0;
        return 1;
    }

    public UserSubjects findOrCreate(Long chatId) {
        if (countByChatId(chatId) == 0) {
            UserSubjects userSubjects = new UserSubjects();
            userSubjects.setChatId(chatId);
            save(userSubjects);
            return userSubjects;
        } else {
            return findByChatId(chatId);
        }
    }

    public void save(UserSubjects userSubjects) {
        if (countByChatId(userSubjects.getChatId()) == 0) {
            dataAccessObject.save(userSubjects);
        } else {
            dataAccessObject.update(userSubjects);
        }
    }
}
