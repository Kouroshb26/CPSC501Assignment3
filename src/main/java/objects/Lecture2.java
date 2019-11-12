package objects;

import java.util.Arrays;

/**
 * @author kourosh
 * @since 2019-11-12
 */
public class Lecture2 {

    private Student[] students = new Student[10];

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
