package objects;

public class Student2 {

    private int studentID;
    private double grade;
    private Student2 friend;

    public Student2() {
    }

    public Student2(int studentID, double grade) {
        this.studentID = studentID;
        this.grade = grade;
    }

    public Student2 getFriend() {
        return friend;
    }

    public void setFriend(Student2 friend) {
        this.friend = friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Student2 student2 = (Student2) o;
        return studentID == student2.studentID
            && Double.compare(student2.grade, grade) == 0;
    }

}
