package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Ventana.VentanaReceptor;
import client.Mensaje;
import interfaz.IVentanaReceptor;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class Receptor extends Persona implements ActionListener{
	
	private static Receptor _instancia = null;
	private IVentanaReceptor ventanaReceptor;
	
    private Receptor() {
    	super();
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
	
    public void recibirMensaje(String str){
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
                    Comunicacion.getInstancia().informarMensajeRecibido(
                                    InetAddress.getByName(this.getIP()),
                                    InetAddress.getByName(mensaje.getEmisor().getIP()),
                                    mensaje.getEmisor().getPuerto());
                } catch (UnknownHostException e) {
                        e.printStackTrace();
                }
            }
            }catch (Exception e){
                e.printStackTrace();
            }
    }
    
    public void lanzarCartelError(String err) {
    	this.ventanaReceptor.lanzarCartelError(err);
    }
    
    public void setVentanaReceptor(IVentanaReceptor ventanaReceptor) {
        this.ventanaReceptor = ventanaReceptor;
        this.ventanaReceptor.addActionListener(this);
    }
	
    public void configAtributos(String ip, String puerto, String nombre, String apellido) {
        this.setIP(ip);
        this.setPuerto(puerto);
        this.setNombre(nombre);
        this.setApellido(apellido);
        Comunicacion.getInstancia().escucharPuerto(this.getPuerto());
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
        else if (comando.equalsIgnoreCase("ABRIR CONFIGURACION"))
                this.ventanaReceptor.abrirConfiguracion();
        else if (comando.equalsIgnoreCase("ACEPTAR CONFIGURACION"))
                this.ventanaReceptor.confirmarConfiguracion();
    }
}
