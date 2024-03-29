package objects;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author kourosh
 * @since 2019-11-12
 */
public class Lecture3 {

    private ArrayList<Student> students;

    public Lecture3() {

    }

    public Lecture3(int size) {
        students = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            students.add(null);
        }

    }

    public void setStudent(int index, Student student) {
        students.set(index, student);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Lecture3 lecture3 = (Lecture3) o;
        return Objects.equals(students, lecture3.students);
    }
}
