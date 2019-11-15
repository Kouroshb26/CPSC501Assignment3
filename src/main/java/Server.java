import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdom2.Document;


/**
 * @author kourosh
 * @since 2019-11-10
 */
public class Server {
    public static final int serverPort = 6365;
    private static final Deserializer deserializer = new Deserializer();
    private static final String serverAddress = "localhost";
    private static final Inspector inspector = new Inspector();


    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Document document = (Document) inputStream.readObject();
                    System.out.println("The xml that was sent over:");
                    System.out.println(Serializer.toString(document));
                    Object object = deserializer.deserialize(document);
                    inspector.inspect(object);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
