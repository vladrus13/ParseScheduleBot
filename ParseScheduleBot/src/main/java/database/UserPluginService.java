package database;

import bean.Pair;
import bean.UserPlugin;

import java.util.List;

public class UserPluginService {

    private final DataAccessObject<UserPlugin> dataAccessObject = new DataAccessObject<>(UserPlugin.class, "UserPlugin");

    public List<UserPlugin> getPluginByPluginName(String name) {
        return dataAccessObject.findByConstraints(List.of(new Pair<>("plugin_name", name)));
    }

    public void save(UserPlugin userPlugin) {
        int size = (int) dataAccessObject.size();
        userPlugin.setId(size + 1);
        dataAccessObject.save(userPlugin);
    }

    public void remove(UserPlugin userPlugin) {
        dataAccessObject.remove(userPlugin);
    }

    public List<UserPlugin> getPluginByChatId(Long chatId) {
        return dataAccessObject.findByConstraints(List.of(new Pair<>("chat_id", chatId)));
    }
}
