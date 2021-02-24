package database;

import bean.User;

import java.util.List;

public class UserService {

    private final DataAccessObject<User> dataAccessObject = new DataAccessObject<>(User.class, "User");

    public User findByChatId(Long chatId) {
        return dataAccessObject.findById(chatId);
    }

    public void update(User user) {
        User user_real;
        if (countByChatId(user.getChatId()) == 0) {
            user_real = new User();
        } else {
            user_real = findByChatId(user.getChatId());
        }
        if (user.getRole() != null) user_real.setRole(user.getRole());
        if (user.getGroup() != null) user_real.setGroup(user.getGroup());
        if (user.getUsername() != null) user_real.setUsername(user.getUsername());
        if (user.getName() != null) user_real.setName(user.getName());
        if (user.getSettings() != null) user_real.setSettings(user.getSettings());
        save(user_real);
    }

    public int countByChatId(Long chatId) {
        User user = findByChatId(chatId);
        if (user == null) return 0;
        return 1;
    }

    public User findOrCreate(Long chatId) {
        if (countByChatId(chatId) == 0) {
            User user = new User();
            user.setChatId(chatId);
            save(user);
            return user;
        } else {
            return findByChatId(chatId);
        }
    }

    public List<User> getAll() {
        return dataAccessObject.findAll();
    }

    public void save(User user) {
        if (countByChatId(user.getChatId()) == 0) {
            dataAccessObject.save(user);
        } else {
            dataAccessObject.update(user);
        }
    }
}
