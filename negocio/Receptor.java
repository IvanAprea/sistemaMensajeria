package negocio;

import base.ComunicacionDirectorio;
import base.ComunicacionEmisor;
import base.ComunicacionReceptor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import presentación.VentanaReceptor;
import presentación.IVentanaReceptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

public class Receptor extends Persona implements ActionListener{
	
        private String IPDirectorio, puertoDirectorio;
	private static Receptor _instancia = null;
	private IVentanaReceptor ventanaReceptor;
        private boolean RMocupado=false;
        private final String regex=", *";
        private final String nombreConfigDirectorio="config.txt";
        private final String decoder="UTF8";
        private final int cantDatos=2;
	
    private Receptor() {
    	super();
    }


    public String getIPDirectorio() {
        return IPDirectorio;
    }

    public String getPuertoDirectorio() {
        return puertoDirectorio;
    }

    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static Receptor getInstancia()
    {
        if(_instancia == null)
            _instancia = new Receptor();
        return _instancia;
    }
    
	private void setSound(String sound){
        try {
            //URL url = this.getClass().getClassLoader().getResource("incomingsound.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("resource/incomingsound.wav").getAbsoluteFile());
            this.ventanaReceptor.setClip(AudioSystem.getClip());
            this.ventanaReceptor.getClip().open(audioIn);
         } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } catch (LineUnavailableException e) {
            e.printStackTrace();
         }
    }
	
    public synchronized void recibirMensaje(String str){
        while(RMocupado==true){
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        RMocupado=true;
    	try {
    	    javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(Mensaje.class);
    	    javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
    	    StringReader reader = new StringReader(str);
    	    Mensaje mensaje = (Mensaje) unmarshaller.unmarshal(reader);
            this.ventanaReceptor.actualizaListaMensajes(mensaje);
            
            if(mensaje.getTipo() == 1) {
            	this.setSound(IVentanaReceptor.ALERT_SOUND_URL);
            	this.ventanaReceptor.lanzarAlerta(mensaje.getEmisor().getNombre());
            }
            else if(mensaje.getTipo() == 2) {
            	try {
                    ComunicacionReceptor.getInstancia().informarMensajeRecibido(
                                    InetAddress.getByName(this.getIP()),
                                    this.getPuerto(),
                                    InetAddress.getByName(mensaje.getEmisor().getIP()),
                                    mensaje.getEmisor().getPuerto());
                } catch (UnknownHostException e) {
                        e.printStackTrace();
                }
            }
            }catch (Exception e){
                e.printStackTrace();
            }
        RMocupado=false;
        notifyAll();
    }
    
    public void lanzarCartelError(String err) {
    	this.ventanaReceptor.lanzarCartelError(err);
    }
    
    public void setVentanaReceptor(IVentanaReceptor ventanaReceptor) {
        this.ventanaReceptor = ventanaReceptor;
        this.ventanaReceptor.addActionListener(this);
    }
	
    public void configAtributos(String nombre) {
        this.setNombre(nombre);
        ComunicacionReceptor.getInstancia().escucharPuerto(this.getPuerto());
    }
	
    public void iniciarSesion(){
        UsuarioReceptor usuario = new UsuarioReceptor(this.getIP()+":"+this.getPuerto(), this.getNombre(), this.getIP(), this.getPuerto());
        try{
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(UsuarioReceptor.class);
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(usuario, sw);
            try{
                ComunicacionReceptor.getInstancia().iniciarSesion(sw, InetAddress.getByName(this.getIPDirectorio()), Integer.parseInt(this.getPuertoDirectorio()));
                //ComunicacionReceptor.getInstancia().escucharPuerto(this.getPuerto()); COMENTARIO PARA QUE ANDE, SACAR!!!!!!!
            } catch (Exception e){
                this.lanzarCartelError("No se pudo iniciar la sesión");
                this.ventanaReceptor.getJDiagSesionRecep().setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void cargarDatosDirectorio(){
        BufferedReader br;
        String[] datos;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(nombreConfigDirectorio), decoder));
            String linea = br.readLine();
            while(linea!=null){
                datos=linea.split(regex);
                this.IPDirectorio=datos[0];
                this.puertoDirectorio=datos[1];
                linea = br.readLine();
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    @Override
    public void actionPerformed(ActionEvent arg) {
        String comando = arg.getActionCommand();
        if (comando.equalsIgnoreCase("ABRIR MENSAJE"))
            this.ventanaReceptor.abrirMensaje();
        else if (comando.equalsIgnoreCase("CERRAR MENSAJE"))
            this.ventanaReceptor.cerrarMensaje();
        else if (comando.equalsIgnoreCase("PARAR ALERTA"))
            this.ventanaReceptor.pararAlerta();
        else  if (comando.equalsIgnoreCase("ACEPTAR SESION"))
            this.ventanaReceptor.confirmarSesion();
    }
}
