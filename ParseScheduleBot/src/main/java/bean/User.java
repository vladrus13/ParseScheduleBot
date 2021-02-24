package bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class User {
    @Id
    @Column(name = "chat_id")
    private Long chatId = null;
    private Integer role = 0;
    @Column(name = "group_m")
    private String group = null;
    private String username = "";
    private String name = "";
    // first byte - is show end time
    // second byte - is show teacher
    // third byte - is compact
    private Integer settings = 0;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getSettings() {
        return settings;
    }

    public void setSettings(int settings) {
        this.settings = settings;
    }

    private boolean getBit(long n, int m) {
        return ((n >> (m)) & 1) == 1;
    }

    public boolean isShowEndPair() {
        return getBit(settings, 0);
    }

    public boolean isShowTeacher() {
        return getBit(settings, 1);
    }

    public boolean isCompact() {
        return getBit(settings, 2);
    }

    public boolean isCodeShow() {
        return getBit(settings, 3);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
