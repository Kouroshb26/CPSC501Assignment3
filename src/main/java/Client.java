import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

import org.jdom2.Document;

/**
 * @author kourosh
 * @since 2019-11-10
 */
public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(Server.serverAddress, Server.serverPort);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            Serializer serializer = new Serializer();
            Document document = serializer.serialize("asdfasfdfasfasd");
            outputStream.writeObject(Serializer.toString(document));
            outputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}


