import org.junit.Test;

import objects.Lecture;
import objects.Lecture2;
import objects.Student;
import objects.Student2;

import static org.junit.Assert.assertEquals;

/**
 * @author kourosh
 * @since 2019-11-12
 */
public class SerializeDeserializeTest {
    Serializer serializer = new Serializer();
    Deserializer deserializer = new Deserializer();

    @Test
    public void testPrimitive() {
        Student student = new Student(20239329, 100.0);
        Object actualStudent = deserializer.deserialize(serializer.serialize(student));

        assertEquals(student, actualStudent);
    }

    @Test
    public void testReference() {
        Student2 student1 = new Student2(204930, 82.02);
        Student2 student2 = new Student2(9278572, 59.02);

        student1.setFriend(student2);
        student2.setFriend(student1);

        Object actualStudent1 = deserializer.deserialize(serializer.serialize(student1));
        assertEquals(student1, actualStudent1);
        assertEquals(student1.getFriend(), ((Student2) actualStudent1).getFriend());

        Object actualStudent2 = deserializer.deserialize(serializer.serialize(student2));
        assertEquals(student2, actualStudent2);
        assertEquals(student2.getFriend(), ((Student2) actualStudent2).getFriend());

    }

    @Test
    public void testPrimitiveArray() {
        Lecture lecture = new Lecture();
        lecture.setStudent(5, 12329238);
        Object actualLecture = deserializer.deserialize(serializer.serialize(lecture));
        assertEquals(lecture, actualLecture);
    }


    @Test
    public void testReferenceArray() {
        Lecture2 lecture = new Lecture2();
        lecture.setStudent(5, new Student(1843983, 69.29));
        Object actualLecture = deserializer.deserialize(serializer.serialize(lecture));
        assertEquals(lecture, actualLecture);
    }

    @Test
    public void testReferenceCollections() {
        Lecture2 lecture = new Lecture2();
        lecture.setStudent(5, new Student(1843983, 69.29));
        Object actualLecture = deserializer.deserialize(serializer.serialize(lecture));
        assertEquals(lecture, actualLecture);
    }

}
