package interfaces;

import java.io.IOException;
import java.io.StringWriter;

import java.net.InetAddress;

public interface IComMensajeria
{
    void enviarPendientes(StringWriter mensaje) throws IOException;
    String recibirMsj() throws Exception;
    void notificarEmisorLlegadaMsj(String nombreRec) throws IOException;
    void enviarConfirmacion(InetAddress ipem, int puertoem,String nReceptor) throws IOException;
    void enviarMensaje(StringWriter mensaje, InetAddress iprec, int puertorec, int tipo, InetAddress ipem, int puertoem) throws IOException;
    String pedirIDADirectorio(String nombreRec,InetAddress ip, int puerto) throws IOException;
}
