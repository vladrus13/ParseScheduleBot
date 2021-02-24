package parser.table;

public class Skip {
    public int startRow;
    public final int startColumn;
    public int sizeRow;
    public final int sizeColumn;
    public final String text;

    public Skip(int startRow, int startColumn, int sizeRow, int sizeColumn, String text) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.sizeRow = sizeRow;
        this.sizeColumn = sizeColumn;
        this.text = text;
    }

    public void down() {
        startRow++;
        sizeRow--;
    }
}
