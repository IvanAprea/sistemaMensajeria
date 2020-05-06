package interfaces;

import java.io.StringWriter;

import java.net.InetAddress;
import java.net.Socket;

public interface IUsuarioCom {
    void iniciarSesion(StringWriter usuario,InetAddress ip,int puerto) throws Exception;
    void notificarDesconexionDirectorio(String ID,InetAddress ip,int puerto) throws Exception;
}
