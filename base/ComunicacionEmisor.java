package base;


import interfaces.IDirectorio;

import interfaces.IEnviarMensajeCom;

import interfaces.IEscucharPuerto;

import interfaces.IPendienteEmisor;

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
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import java.util.ArrayList;

import java.util.Iterator;

import negocio.GestorEnvioMensajes;
import negocio.GestorRecepcionMensajes;

public class ComunicacionEmisor implements IEnviarMensajeCom,IDirectorio,IEscucharPuerto,IPendienteEmisor{

    private static ComunicacionEmisor _instancia = null;
    private Socket socketDirectorio;
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


    public synchronized void enviarMensaje(String mensaje, InetAddress ip, int puerto) throws IOException{
        try {
            s = new Socket(ip, puerto);
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF("MSJ_NUEVOMSJ");
            dOut.writeUTF(mensaje);
            dOut.flush();
        }finally
        {
            try
            {
                if(s!=null)
                    s.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public synchronized void enviarMensajes(ArrayList<String> mensajes, InetAddress ip, int puerto) throws IOException{
        try {
            s = new Socket(ip, puerto);
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            Iterator<String> it = mensajes.iterator();
            while(it.hasNext()){
                dOut.writeUTF("MSJ_NUEVOMSJ");
                dOut.writeUTF(it.next());
                dOut.flush();
                it.remove();
            }
        }finally
        {
            try
            {
                if(s!=null)
                    s.close();
            } catch (IOException e)
            {
                System.out.println("La mensajeria esta off");
            }
        }
    }
    
    public synchronized void escucharPuerto(String puerto) {
        Thread tr = new Thread() {
            public void run() {
                try {
                    ServerSocket sepe = new ServerSocket(Integer.parseInt(puerto));
                    String[] tokens;
                    while (true) 
                    {
                        s = sepe.accept();
                        DataInputStream dIn = new DataInputStream(s.getInputStream());
                        tokens = dIn.readUTF().split("-");
                        GestorEnvioMensajes.getInstancia().recibirConfirmacion(tokens[0],tokens[1]);
                        s.close();
                    }
                }
                catch (BindException e)
                {
                    //Msj de error
                    e.printStackTrace();
                }
                catch (SocketTimeoutException  e)
                {
                    //Msj de error
                    e.printStackTrace();
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
            
        };
        tr.start();
    }

    public Socket abrirConexionDirectorio(InetAddress ip, int puerto) throws IOException {
        
            return new Socket(ip, puerto);
        
    }
    
    public String pedirListaADirectorio(InetAddress ip, int puerto) throws IOException
    {
        String hm;

            socketDirectorio = new Socket(ip,puerto);
            DataOutputStream dOut = new DataOutputStream(socketDirectorio.getOutputStream());
            String s = "DIR_GETLISTA";
            dOut.writeUTF(s);
            dOut.flush();         
            DataInputStream dIn = new DataInputStream(socketDirectorio.getInputStream());
            return dIn.readUTF();
        
    }

    public void pedirAvisosPendientes(String IPMensajeria, String puertoMensajeria, String idEmisor) {
        try
        {
            s = new Socket(InetAddress.getByName(IPMensajeria), Integer.parseInt(puertoMensajeria));
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF("MSJ_PEDIDOAVISOSEM");
            dOut.writeUTF(idEmisor);
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            String res = dIn.readUTF();
            String[] tokens;
            while(res.equalsIgnoreCase("TRUE"))
            {
                tokens = dIn.readUTF().split("-");
                GestorEnvioMensajes.getInstancia().recibirConfirmacion(tokens[0],tokens[1]);
                res = dIn.readUTF();
            }
            s.close();
        } catch (IOException e)
        {
            GestorEnvioMensajes.getInstancia().lanzarCartelError("El servicio de mensajes esta offline momentaneamente.");
        }
    }
    
    public void setSocketDirectorio(Socket socketDirectorio) {
        this.socketDirectorio = socketDirectorio;
    }
    
    public Socket getSocketDirectorio() {
        return socketDirectorio;
    }
}
