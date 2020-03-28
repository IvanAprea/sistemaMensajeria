package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;

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
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }.start();	
    }
    
    public void enviarMensaje(Mensaje mensaje){
    	//TODO ESTO DEBERIA SER PROCESADO EN EMISOR Y ACA MANDAR SOLO EL STRING A ENVIAR
    	//HABRIA QUE VER COMO PASARLE LA IP Y PUERTO DEL RECEPTOR
    	/*
    	try {
    	    JAXBContext context = JAXBContext.newInstance(Mensaje.class);
    	    Marshaller marshaller = context.createMarshaller();
    	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    	    StringWriter sw = new StringWriter();
    	    marshaller.marshal(mensaje, sw);
    	    
            Socket socket = new Socket("IP","PUERTO");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(sw.toString());
            out.close();
            socket.close();
    	} catch (JAXBException ex) {
    	    ex.printStackTrace();
    	}
    	*/
    }
    
   
    public void informarMensajeRecibido(String ip, String puerto){
        
    }
}
