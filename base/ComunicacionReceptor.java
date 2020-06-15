package base;

import interfaces.IEscucharPuerto;
import interfaces.IPendientesReceptor;
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

import negocio.LogicaReceptor;
import negocio.UsuarioReceptor;

public class ComunicacionReceptor implements IUsuarioCom,IRecepción,IEscucharPuerto,IPendientesReceptor{

    private static ComunicacionReceptor _instancia = null;
    private Socket s; //sem=socketEnviarMensaje
    private DataOutputStream dOutRecepcion;
    
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
                        dOutRecepcion = new DataOutputStream(s.getOutputStream());
                        LogicaReceptor.getInstancia().recibirMensaje(dIn.readUTF());
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
    
    public synchronized void heartbeat(InetAddress ipDirectorio,int puertoDirectorio,InetAddress ipDirectorioAux,int puertoDirectorioAux,String id){
        Thread tr = new Thread(){
            DataOutputStream dOut;
            public void run(){
                try {
                    while(true){
                        Thread.sleep(5000);
                        try{
                            s = new Socket(ipDirectorio,puertoDirectorio);
                        }catch(IOException e)
                        {
                            s = new Socket(ipDirectorioAux,puertoDirectorioAux);
                        }
                        dOut = new DataOutputStream(s.getOutputStream());
                        dOut.writeUTF("DIR_DAR_ALIVE");
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
                    if(s!=null)
                        try {
                            s.close();
                        } catch (IOException f) {
                            f.printStackTrace();
                        }
                    System.out.println("Error al conectar con el directorio HB");
                }
            }
        };
        tr.start();
    }
    
    public synchronized void informarMensajeRecibido(String msj){
        try {
            this.dOutRecepcion.writeUTF(msj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void pedirMensajesPendientes(String IPMensajeria, String PuertoMensajeria, String id)
    {
        try
        {
            s = new Socket(InetAddress.getByName(IPMensajeria), Integer.parseInt(PuertoMensajeria));
            dOutRecepcion = new DataOutputStream(s.getOutputStream());
            dOutRecepcion.writeUTF("MSJ_PEDIDOMSJREC");
            dOutRecepcion.writeUTF(id);
            DataInputStream dIn = new DataInputStream(s.getInputStream());
            String st = dIn.readUTF();
            while(st.equalsIgnoreCase("TRUE"))
            {
                LogicaReceptor.getInstancia().recibirMensaje(dIn.readUTF());
                st = dIn.readUTF();
            }
            s.close();
        } catch (IOException e)
        {
            LogicaReceptor.getInstancia().lanzarCartelError("El servicio de mensajes esta offline momentaneamente.");
        }
    }
    
    public void iniciarSesion(StringWriter usuario,InetAddress ip,int puerto) throws IOException
    {

            s = new Socket(ip,puerto);
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF("DIR_AGREGAR_REC");
            dOut.writeUTF(usuario.toString());
            dOut.flush();
            s.close();
    }
    
    public void notificarDesconexionDirectorio(String ID,InetAddress ip,int puerto) throws IOException
    {

            s = new Socket(ip,puerto);
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF("DIR_DESCONECTAR_REC");
            dOut.writeUTF(ID);
            dOut.flush();
            s.close();

    }
}
