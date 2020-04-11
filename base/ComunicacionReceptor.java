package base;

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

import negocio.Receptor;
import negocio.UsuarioReceptor;

public class ComunicacionReceptor {

    private static ComunicacionReceptor _instancia = null;
    private Socket sem; //sem=socketEnviarMensaje
    
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
                    ServerSocket s = new ServerSocket(Integer.parseInt(puerto));
                    while (true) 
                    {
                        Socket soc = s.accept();
                        PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
                        DataInputStream dIn = new DataInputStream(soc.getInputStream());
                        Receptor.getInstancia().recibirMensaje(dIn.readUTF());
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
    
    public synchronized void informarMensajeRecibido(InetAddress ipPropia,String puertoPropio, InetAddress ipAInformar, String puertoAInformar){
        try {
            Socket socket = new Socket(ipAInformar,Integer.parseInt(puertoAInformar));
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            String s = ipPropia.toString()+":"+puertoPropio;
            dOut.writeUTF(s);
            dOut.flush();
            socket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void iniciarSesion(StringWriter usuario,InetAddress ip,int puerto){
        try {
            sem = new Socket(ip,puerto);
            DataOutputStream dOut = new DataOutputStream(sem.getOutputStream());
            dOut.writeUTF(usuario.toString());
            dOut.flush();
            sem.close();
            
        } catch (IOException e) {
            try {
                sem.close();
            } catch (IOException f) {
                f.printStackTrace();
            }
            
        }
    }
    
    //INICIAR THREAD HEATBEAT -> Cada cierto tiempo manda mensaje a directorio con el metodo de ivan Scheduled...
}
