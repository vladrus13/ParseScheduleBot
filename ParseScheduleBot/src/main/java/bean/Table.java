package bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@javax.persistence.Table(name = "Table")
public class Table {
    @Id
    private int id;

    private String url;

    private String nameColumn;

    private String resultColumn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getResultColumn() {
        return resultColumn;
    }

    public void setResultColumn(String resultColumn) {
        this.resultColumn = resultColumn;
    }
}
