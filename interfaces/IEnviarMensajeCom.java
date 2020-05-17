package interfaces;

import exceptions.excepcionEnviarMensaje;

import java.io.StringWriter;

import java.net.InetAddress;

public interface IEnviarMensajeCom{
    void enviarMensaje(StringWriter mensaje, InetAddress ip, int puerto,int tipo) throws excepcionEnviarMensaje ;
}
