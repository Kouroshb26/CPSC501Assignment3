package objects;

import java.util.Arrays;

/**
 * @author kourosh
 * @since 2019-11-12
 */
public class Lecture2 {

    private Student[] students;

    public Lecture2() {
    }

    public Lecture2(int size) {
        students = new Student[size];
    }

    public void setStudent(int index, Student student) {
        students[index] = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Lecture2 lecture2 = (Lecture2) o;
        return Arrays.equals(students, lecture2.students);
    }

}
