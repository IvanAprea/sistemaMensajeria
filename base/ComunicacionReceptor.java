package base;

import interfaces.IEscucharPuerto;
import interfaces.IRecepción;
import interfaces.IUsuarioCom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.StringWriter;

import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import java.net.UnknownHostException;

import negocio.NegocioReceptor;
import negocio.UsuarioReceptor;

public class ComunicacionReceptor implements IUsuarioCom,IRecepción,IEscucharPuerto{

    private static ComunicacionReceptor _instancia = null;
    private Socket s; //sem=socketEnviarMensaje
    
    private ComunicacionReceptor() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static ComunicacionReceptor getInstancia()
    {
        if(_instancia == null)
            _instancia = new ComunicacionReceptor();
        return _instancia;
    }
    
    
    public synchronized void escucharPuerto(String puerto) {
        Thread tr = new Thread() {
            public void run() {
                try {
                    ServerSocket sv = new ServerSocket(Integer.parseInt(puerto));
                    while (true) 
                    {
                        s = sv.accept();
                        DataInputStream dIn = new DataInputStream(s.getInputStream());
                        NegocioReceptor.getInstancia().recibirMensaje(dIn.readUTF());
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
    
    public synchronized void heartbeat(InetAddress ipDirectorio,int puertoDirectorio,String id){
        Thread tr = new Thread(){
            DataOutputStream dOut;
            public void run(){
                try {
                    while(true){
                        Thread.sleep(5000);
                        s = new Socket(ipDirectorio,puertoDirectorio);
                        dOut = new DataOutputStream(s.getOutputStream());
                        dOut.writeUTF("ALIVE");
                        dOut.writeUTF(id);
                        dOut.flush();
                        s.close();
                    }
                } catch (InterruptedException e) {
                    if(s!=null)
                        try {
                            s.close();
                        } catch (IOException f) {
                            f.printStackTrace();
                        }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        tr.start();
    }
    
    public synchronized void informarMensajeRecibido(String msj){
        try {
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeUTF(msj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void pedirMensajesPendientes(String IPMensajeria, String PuertoMensajeria, String id)
    {
        try
        {
            s = new Socket(InetAddress.getByName(IPMensajeria), Integer.parseInt(PuertoMensajeria));
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF("MSJ_PEDIDOMSJREC");
            dOut.writeUTF(id);
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            while(dIn.readUTF().equalsIgnoreCase("TRUE"))
            {
                NegocioReceptor.getInstancia().recibirMensaje(dIn.readUTF());
            }
            s.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void iniciarSesion(StringWriter usuario,InetAddress ip,int puerto) throws Exception {
        try {
            s = new Socket(ip,puerto);
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF("AGREGAR");
            dOut.writeUTF(usuario.toString());
            dOut.flush();
            s.close();
            
        } catch (IOException e) {
            try {
                if(s!=null){
                    s.close();
                }
                throw new Exception("ERROR");
            } catch (IOException f) {
                f.printStackTrace();
            }
            
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }
    
    public void notificarDesconexionDirectorio(String ID,InetAddress ip,int puerto) throws Exception {
        try {
            s = new Socket(ip,puerto);
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF("DESCONECTAR");
            dOut.writeUTF(ID);
            dOut.flush();
            s.close();
            
        } catch (IOException e) {
            try {
                if(s!=null){
                    s.close();
                }
                throw new Exception("ERROR");
            } catch (IOException f) {
                f.printStackTrace();
            }     
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println(e.getMessage());
        }
    }
}
