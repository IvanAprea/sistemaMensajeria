package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Comunicacion {
    
    private static Comunicacion _instancia = null;
    private ServerSocket sepe; //sepe=socketEscucharPuertoEmisor 
    private Socket sem; //sem=socketEnviarMensaje
    
    private Comunicacion() {
    	super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static Comunicacion getInstancia()
    {
        if(_instancia == null)
            _instancia = new Comunicacion();
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
    
    public synchronized void escucharPuertoEmisor(String puerto) {
        new Thread() {
            public void run() {
                try {
                    sepe = new ServerSocket(Integer.parseInt(puerto));
                    sepe.setSoTimeout(1000);// SACAR COMENTARIO
                    while(true){
                        Socket soc = sepe.accept();
                        PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
                        DataInputStream dIn = new DataInputStream(soc.getInputStream());
                        String str = dIn.readUTF();
                        str = str.substring(1);
                        String aux = str;
                        InetAddress adr = InetAddress.getByName(str.split(":")[0]);
                        Emisor.getInstancia().recibirConfirmacion(aux);
                        soc.close();
                    }
                }
                catch (BindException e) 
                {
                    Emisor.getInstancia().lanzarCartelError("ERROR: El puerto ya está siendo escuchado");
                }
                catch (UnknownHostException e) 
                {
                    e.printStackTrace();
                    //Este error se da cuando envio un mensaje a un puerto que es igual al que abro para recibir la confirmacion
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    try {
                        sepe.close();
                    } catch (IOException f) {
                        f.printStackTrace();
                    }
                }
            }
        }.start();
    }
    
    public void enviarMensaje(StringWriter mensaje,InetAddress ip,int puerto){
    	try {
            sem = new Socket(ip,puerto);
    	    DataOutputStream dOut = new DataOutputStream(sem.getOutputStream());
    	    dOut.writeUTF(mensaje.toString());
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
}
