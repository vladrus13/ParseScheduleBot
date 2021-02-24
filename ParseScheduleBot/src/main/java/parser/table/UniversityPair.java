package parser.table;

public class UniversityPair {

    private final Lesson odd;
    private final Lesson even;

    public UniversityPair(Lesson odd, Lesson even) {
        this.odd = odd;
        this.even = even;
    }

    public Lesson getOdd() {
        return odd;
    }

    public Lesson getEven() {
        return even;
    }
}
