package bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserPlugin")
public class UserPlugin {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "chat_id")
    private Long chat_id = null;

    @Column(name = "plugin_name")
    private String pluginName = null;

    public Long getChatId() {
        return chat_id;
    }

    public void setChatId(Long chat_id) {
        this.chat_id = chat_id;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String botName) {
        this.pluginName = botName;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
