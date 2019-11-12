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
    public static final String serverAddress = "localhost";
    public static final int serverPort = 4444;
    public static final Deserializer deserializer = new Deserializer();


    public static void main(String[] args) {

        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
                try (Socket socket = serverSocket.accept()) {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Document document = (Document) inputStream.readObject();
                    deserializer.deserialize(document);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
