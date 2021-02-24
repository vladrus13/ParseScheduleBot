package parser.table;

import java.util.Objects;

public class Lesson {

    private final String name;
    private final String place;
    private final String teacher;

    public Lesson(String name, String place, String teacher) {
        this.name = name;
        this.place = place;
        this.teacher = teacher;
    }

    public boolean isEmpty() {
        return name.equals(" ()") && place.isEmpty() && teacher.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(name, lesson.name) &&
                Objects.equals(place, lesson.place) &&
                Objects.equals(teacher, lesson.teacher);
    }


    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getTeacher() {
        return teacher;
    }
}
