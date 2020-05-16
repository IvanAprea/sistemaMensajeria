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
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import negocio.NegocioEmisor;
import negocio.NegocioReceptor;

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
            dOut.writeUTF("MSJ_NUEVOMSJ");
            dOut.writeUTF(mensaje.toString());
            dOut.flush();
            /*if(tipo == 2){
                DataInputStream dIn = new DataInputStream(s.getInputStream());
                String resultado = dIn.readUTF();
                if(resultado.equalsIgnoreCase("DISCCONECTED"))
                {
                    s.close();
                    throw new excepcionEnviarMensaje();
                }
                else
                    NegocioEmisor.getInstancia().recibirConfirmacion(resultado);
            }*/
            s.close();

        } catch (IOException e) {
            try {
                if(s!=null)
                    s.close();
            } catch (IOException f) {
                f.printStackTrace();
            }

        }
    }
    
    
    public synchronized void escucharPuerto(String puerto) {
        Thread tr = new Thread() {
            public void run() {
                try {
                    ServerSocket sepe = new ServerSocket(Integer.parseInt(puerto));
                    while (true) 
                    {
                        s = sepe.accept();
                        DataInputStream dIn = new DataInputStream(s.getInputStream());
                        NegocioEmisor.getInstancia().recibirConfirmacion(dIn.readUTF());
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
            String s = "DIR_GETLISTA";
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

    public void pedirAvisosPendientes(String IPMensajeria, String puertoMensajeria, String idEmisor) {
        try
        {
            s = new Socket(InetAddress.getByName(IPMensajeria), Integer.parseInt(puertoMensajeria));
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF("MSJ_PEDIDOAVISOSEM");
            dOut.writeUTF(idEmisor);
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            String res = dIn.readUTF();
            while(res.equalsIgnoreCase("TRUE"))
            {
                NegocioEmisor.getInstancia().recibirConfirmacion(dIn.readUTF());
                res = dIn.readUTF();
            }
            s.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
