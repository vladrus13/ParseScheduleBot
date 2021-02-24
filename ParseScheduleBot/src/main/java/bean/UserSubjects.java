package bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// TODO make added and removed Lists
@Entity
@Table(name = "UserSubject")
public class UserSubjects {
    @Id
    @Column(name = "chat_id")
    private Long chatId = null;
    private String added = "";
    private String removed = "";

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getRemoved() {
        return removed;
    }

    public void setRemoved(String removed) {
        this.removed = removed;
    }
}
