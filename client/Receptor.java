package client;

import client.Mensaje;
import interfaz.IVentanaReceptor;

public class Receptor {
	
	private IVentanaReceptor ventanaReceptor;
	
    public Receptor(IVentanaReceptor ventana) {
        this.ventanaReceptor = ventana;
    }
    
    public void recibirMensaje(Object obj){
        Mensaje mensaje = (Mensaje) obj;
        this.ventanaReceptor.actualizaListaMensajes(mensaje);
        
    }
    
    public void informarMensajeRecibido(){
        
    }
}
