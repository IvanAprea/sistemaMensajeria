package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
                        ObjectInputStream is = new ObjectInputStream(soc.getInputStream());
                        Receptor.getInstancia().recibirMensaje(is.readObject());
                    }

                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }.start();	
    }
    
    public void enviarMensaje(){
        
    }
    
   
    public void informarMensajeRecibido(){
        
    }
}
