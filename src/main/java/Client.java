import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import org.jdom2.Document;

import objects.Lecture;
import objects.Lecture2;
import objects.Lecture3;
import objects.Student;
import objects.Student2;

/**
 * @author kourosh
 * @since 2019-11-10
 */
public class Client {
    private static int objectCounter = 1;
    private static Scanner scanner = new Scanner(System.in);
    private static Serializer serializer = new Serializer();

    public static void main(String[] args) {
        while (true) {
            try (Socket socket = new Socket(Server.serverAddress, Server.serverPort)) {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                Object object = createObject();

                if (object == null) {
                    break;
                } else {
                    Document document = serializer.serialize(object);
                    Serializer.toFile(document, "Object" + objectCounter + ".xml");
                    outputStream.writeObject(document);
                    outputStream.flush();
                    System.out.printf("Sent Object%1$d and saved it to Object%1$d.xml\n", objectCounter);
                }
                objectCounter++;
                Thread.sleep(1000);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }


    }


    private static Object createObject() {
        System.out.println("Please choose from the following options:\n"
            + "1. An Objects with only primitives (Student)\n"
            + "2. An Object with reference to another object (Student 2)\n"
            + "3. An Object that is array of primitives (Lecture)\n"
            + "4. An Object that is array of references (Lecture2)\n"
            + "5. An Object that is a Java Collection classes (Lecture3)\n"
            + "0. To quit");


        int selection = scanner.nextInt();

        int id, size, index, value;
        double grade;

        switch (selection) {
            case 1:
                return createStudent();
            case 2:
                System.out.println("Creating first Student");
                System.out.println("Enter a student ID (int)");
                id = scanner.nextInt();
                System.out.println("Enter a student grade (double)");
                grade = scanner.nextDouble();
                Student2 student1 = new Student2(id, grade);

                System.out.println("Creating second Student");
                System.out.println("Enter a student ID (int)");
                id = scanner.nextInt();
                System.out.println("Enter a student grade (double)");
                grade = scanner.nextDouble();
                Student2 student2 = new Student2(id, grade);

                System.out.println("Referencing them to each other");
                student1.setFriend(student2);
                student2.setFriend(student1);
                return student1;
            case 3:
                System.out.println("Enter size of array");
                size = scanner.nextInt();
                Lecture lecture = new Lecture(size);
                System.out.println("Enter index to set");
                index = scanner.nextInt();
                System.out.println("Enter value to set at that index");
                value = scanner.nextInt();
                lecture.setStudent(index, value);
                return lecture;
            case 4:
                System.out.println("Enter size of array");
                size = scanner.nextInt();
                Lecture2 lecture2 = new Lecture2(size);
                System.out.println("Enter index to set");
                index = scanner.nextInt();
                System.out.println("Creating student object");
                lecture2.setStudent(index, createStudent());
                return lecture2;
            case 5:
                System.out.println("Enter size of array");
                size = scanner.nextInt();
                Lecture3 lecture3 = new Lecture3(size);
                System.out.println("Enter index to set");
                index = scanner.nextInt();
                System.out.println("Creating student object");
                lecture3.setStudent(index, createStudent());
                return lecture3;
            default:
                return null;
        }
    }

    private static Student createStudent() {
        int id;
        double grade;
        System.out.println("Enter a student ID (int)");
        id = scanner.nextInt();
        System.out.println("Enter a student grade (double)");
        grade = scanner.nextDouble();
        return new Student(id, grade);
    }
}


