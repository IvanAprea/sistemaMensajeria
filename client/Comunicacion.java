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
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Comunicacion {
    
    private static Comunicacion _instancia = null;
    int i=1;
    ServerSocket ss;
    
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
    
    
    public void escucharPuerto(String puerto) {
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
    
    public void escucharPuertoEmisor(String puerto) {
        new Thread() {
            public void run() {
                try {
                    ss = new ServerSocket(Integer.parseInt(puerto));
                    ss.setSoTimeout(1000);// SACAR COMENTARIO
                    Socket soc = ss.accept();
                    PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
                    DataInputStream dIn = new DataInputStream(soc.getInputStream());
                    String str = dIn.readUTF();
                    str = str.substring(1);
                    String aux = str;
                    InetAddress adr = InetAddress.getByName(str.split(":")[0]);
                    Emisor.getInstance().recibirConfirmacion(aux);
                    ss.close();
                }
                catch (BindException e) 
                {
                    
                    Emisor.getInstance().lanzarCartelError("ERROR: El puerto ya está siendo escuchado");
                }
                catch (UnknownHostException e) 
                {
                    e.printStackTrace();
                    //Este error se da cuando envio un mensaje a un puerto que es igual al que abro para recibir la confirmacion
                }
                catch (Exception e) 
                {
                    try {
                        ss.close();
                    } catch (IOException f) {
                        e.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    public void enviarMensaje(StringWriter mensaje,InetAddress ip,int puerto){
    	try {
            Socket socket = new Socket(ip,puerto);
    	    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
    	    dOut.writeUTF(mensaje.toString());
    	    dOut.flush();
            socket.close();
            
    	} catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    public void informarMensajeRecibido(InetAddress ipPropia,String puertoPropio, InetAddress ipAInformar, String puertoAInformar){
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
