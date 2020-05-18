package interfaces;

import java.io.IOException;
import java.io.StringWriter;

import java.net.InetAddress;

public interface IComMensajeria
{
    void enviarPendientes(StringWriter mensaje) throws IOException;
    String recibirMsj() throws Exception;
    void notificarEmisorConLlegadaMsj(String nombreRec) throws IOException;
    void enviarConfirmacion(InetAddress ipem, int puertoem,String nReceptor) throws IOException;
}
