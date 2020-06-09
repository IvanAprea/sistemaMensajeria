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

import negocio.LogicaDirectorio;
import negocio.LogicaEmisor;
import negocio.UsuarioReceptor;

public class ComunicacionDirectorio implements IEscucharPuerto,IRegistro{

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
    
    public synchronized void nuevoUsuario(){
        try {
            LogicaDirectorio.getInstancia().nuevoUsuario(dIn.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
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
                        LogicaDirectorio.getInstancia().ejecutarComando(dIn.readUTF());
                        socket.close();
                    }
                    //ver donde cerrar el socket
                } catch (BindException e) {
                    LogicaEmisor.getInstancia().lanzarCartelError("ERROR: El puerto ya está siendo escuchado");
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

    public void setearUsuarioDesconectado() {
        try {
            LogicaDirectorio.getInstancia().setearUsuarioDesconectado(dIn.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
    }
    
    public void recibirAlive(){
        try {
            LogicaDirectorio.getInstancia().recibirAlive(dIn.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
    }

}
