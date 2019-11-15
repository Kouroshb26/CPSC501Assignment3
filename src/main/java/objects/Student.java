package objects;

public class Student {

    private int studentID;
    private double grade;

    public Student() {

    }

    public Student(int studentID, double grade) {
        this.studentID = studentID;
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Student student = (Student) o;
        return studentID == student.studentID
            && Double.compare(student.grade, grade) == 0;
    }
}


