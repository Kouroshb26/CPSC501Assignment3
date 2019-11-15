package objects;

import java.util.Arrays;

/**
 * @author kourosh
 * @since 2019-11-12
 */
public class Lecture {

    private int[] studentIDs;

    public Lecture() {
    }

    public Lecture(int size) {
        studentIDs = new int[size];
    }

    public void setStudent(int index, int studentID) {
        this.studentIDs[index] = studentID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Lecture lecture = (Lecture) o;
        return Arrays.equals(studentIDs, lecture.studentIDs);
    }

}
