package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;

public class Comunicacion {
    
	private static Comunicacion _instancia = null;
	
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
        new Thread() {
            public void run() {
                try {
                    ServerSocket s = new ServerSocket(Integer.parseInt(puerto));
                    while (true) 
                    {
                        Socket soc = s.accept();
                        PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                        Receptor.getInstancia().recibirMensaje(in.readLine());
                    }

                } 
                catch (BindException e) 
                {
                	
                    e.printStackTrace();
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }.start();	
    }
    
    public void enviarMensaje(StringWriter mensaje,InetAddress ip,int puerto){
    	
    	try {
    	    
            Socket socket = new Socket(ip,puerto);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(mensaje.toString());
            out.close();
            socket.close();
            
    	} catch (IOException e) {
            e.printStackTrace();
        }

    }
    
   
    public void informarMensajeRecibido(InetAddress ipPropia, InetAddress ipAInformar, String puerto){
        
    }
}
