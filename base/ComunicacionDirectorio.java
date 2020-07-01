package base;

import interfaces.IEscucharPuerto;

import interfaces.IRegistro;

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

import java.util.HashMap;

import negocio.GestorUsuariosReceptores;
import negocio.GestorEnvioMensajes;
import negocio.UsuarioReceptor;

public class ComunicacionDirectorio implements IEscucharPuerto{

    private static ComunicacionDirectorio _instancia = null;
    private ServerSocket sepd; //sepe=socketEscucharPuertoDirectorio
    private Socket socket;
    private DataInputStream dIn;
    private DataOutputStream dOut;
    private PrintWriter out;
    
    private ComunicacionDirectorio() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static ComunicacionDirectorio getInstancia()
    {
        if(_instancia == null)
            _instancia = new ComunicacionDirectorio();
        return _instancia;
    }
    
   
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized void escucharPuerto(String puerto) {
        new Thread() {
            public void run() {
                try {
                    
                    sepd = new ServerSocket(Integer.parseInt(puerto));
                    while (true) {
                        socket = sepd.accept();
                        out = new PrintWriter(socket.getOutputStream(), true);
                        dIn = new DataInputStream(socket.getInputStream());
                        GestorUsuariosReceptores.getInstancia().ejecutarComando(dIn.readUTF());
                        socket.close();
                    }
                    //ver donde cerrar el socket
                } catch (BindException e) {
                    GestorEnvioMensajes.getInstancia().lanzarCartelError("ERROR: El puerto ya está siendo escuchado");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                 } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    try {
                        sepd.close();
                    } catch (IOException f) {
                        f.printStackTrace();
                    }
                }
            }
        }.start();
    }
    
    public void darLista(StringWriter hashmapMarshalizado){
        try 
        {
            dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF(hashmapMarshalizado.toString());
            dOut.flush();
        } 
        catch (IOException e) {
        
        }
    }

    public String leerDIn() {
        try {
            return dIn.readUTF();
        } catch (IOException e) {
            e.printStackTrace();      
            try {
                socket.close();
                return null;
            } catch (IOException f) {
                f.printStackTrace();
                return null;
            }
        }
    }
    
    public void darIDRec(String ip, String puerto){
        try{
            dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF(ip+":"+puerto);
            dOut.flush();    
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void enviarBackUp(String userOnline, String userRec){
        try {
            dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF(userOnline);
            dOut.flush();
            dOut.writeUTF(userRec);
            dOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
