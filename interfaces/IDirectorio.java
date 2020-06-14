package interfaces;

import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;

public interface IDirectorio {
    String pedirListaADirectorio(InetAddress ip, int puerto) throws IOException;
}
