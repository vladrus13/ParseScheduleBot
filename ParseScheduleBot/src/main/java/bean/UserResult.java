package bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserResult")
public class UserResult {
    @Id
    private int id;

    @Column(name = "table_id")
    private int tableId;

    @Column(name = "user_id")
    private long userId;

    private String result;

    public int getId() {
        return id;
    }

    public int getTableId() {
        return tableId;
    }

    public long getUserId() {
        return userId;
    }

    public String getResult() {
        return result;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
