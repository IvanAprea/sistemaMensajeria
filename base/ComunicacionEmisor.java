package base;

import exceptions.excepcionEnviarMensaje;

import interfaces.IDirectorio;

import interfaces.IEnviarMensajeCom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.StringWriter;

import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import negocio.NegocioEmisor;

public class ComunicacionEmisor implements IEnviarMensajeCom,IDirectorio{

    private static ComunicacionEmisor _instancia = null;
    private ServerSocket sepe; //sepe=socketEscucharPuertoEmisor
    private Socket s; //sem=socketEnviarMensaje

    private ComunicacionEmisor() {
        super();
    }

    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static ComunicacionEmisor getInstancia() {
        if (_instancia == null)
            _instancia = new ComunicacionEmisor();
        return _instancia;
    }


    public void enviarMensaje(StringWriter mensaje, InetAddress ip, int puerto,int tipo) throws excepcionEnviarMensaje {
        try {
            s = new Socket(ip, puerto);
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF(mensaje.toString());
            dOut.flush();
            if(tipo == 2){
                DataInputStream dIn = new DataInputStream(s.getInputStream());
                String resultado = dIn.readUTF();
                NegocioEmisor.getInstancia().recibirConfirmacion(resultado);
            }
            s.close();

        } catch (IOException e) {
            try {
                if(s!=null)
                    s.close();
                throw new excepcionEnviarMensaje();
            } catch (IOException f) {
                f.printStackTrace();
            }

        }
    }

    public Socket abrirConexionDirectorio(InetAddress ip, int puerto) {
        try {
            return new Socket(ip, puerto);
        } catch (IOException e) {
            NegocioEmisor.getInstancia().lanzarCartelError("ERROR al conectar con el directorio");
            return null;
        }
    }
    
    public String pedirListaADirectorio(Socket socket){
        String hm;
        try 
        {
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            String s = "GET";
            dOut.writeUTF(s);
            dOut.flush();         
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            return dIn.readUTF();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return null;
        }
    }
    
}
