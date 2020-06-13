package interfaces;

import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;

public interface IDirectorio {
    String pedirListaADirectorio(Socket socket);
    Socket abrirConexionDirectorio(InetAddress ip, int puerto) throws IOException;
}
