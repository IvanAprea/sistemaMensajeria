package base;

import interfaces.IEscucharPuerto;

import interfaces.IRedundanciaDir;
import interfaces.IRegistro;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.StringReader;
import java.io.StringWriter;

import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import negocio.LogicaDirectorio;
import negocio.LogicaEmisor;
import negocio.UsuarioReceptor;
import negocio.UsuariosRecMap;

public abstract class ComunicacionDirectorio implements IEscucharPuerto,IRegistro,IRedundanciaDir{

    private static ComunicacionDirectorio _instancia = null;
    private ServerSocket sepd; //sepe=socketEscucharPuertoDirectorio
    private Socket socketPD;
    private DataInputStream dIn;
    private DataOutputStream dOut;
    private PrintWriter out;
    
    public ComunicacionDirectorio() {
        super();
    }
    
    public synchronized void nuevoUsuario(){
        try {
            LogicaDirectorio.getInstancia().nuevoUsuario(dIn.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socketPD.close();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
    }
    
    public void setSocket(Socket socket) {
        this.socketPD = socket;
    }

    public Socket getSocket() {
        return socketPD;
    }

    public synchronized void escucharPuerto(String puerto) {
        new Thread() {
            public void run() {
                try {
                    
                    sepd = new ServerSocket(Integer.parseInt(puerto));
                    while (true) {
                        socketPD = sepd.accept();
                        out = new PrintWriter(socketPD.getOutputStream(), true);
                        dIn = new DataInputStream(socketPD.getInputStream());
                        LogicaDirectorio.getInstancia().ejecutarComando(dIn.readUTF());
                        socketPD.close();
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
    
    public abstract void conectarDirectorio();
    public abstract void escucharDirectorio(String puerto);
    public abstract void cargarDatosDir();
    public abstract String recibirDatos();
    public abstract void setIpDir(String s);
    public abstract void setPuertoDir(String s);
    
    public void darLista(StringWriter hashmapMarshalizado){
        try 
        {
            dOut = new DataOutputStream(socketPD.getOutputStream());
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
                socketPD.close();
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
                socketPD.close();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
    }
    
}
