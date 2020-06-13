package interfaces;


import java.io.IOException;

import java.net.InetAddress;

import java.util.ArrayList;

public interface IEnviarMensajeCom{
    void enviarMensaje(String mensaje, InetAddress ip, int puerto) throws IOException ;
    void enviarMensajes(ArrayList<String> mensajes, InetAddress ip, int puerto) throws IOException ;
}
