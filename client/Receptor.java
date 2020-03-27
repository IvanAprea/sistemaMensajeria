package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.Mensaje;
import interfaz.IVentanaReceptor;

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
    
    public void recibirMensaje(Object obj){
        Mensaje mensaje = (Mensaje) obj;
        this.ventanaReceptor.actualizaListaMensajes(mensaje);
        
    }
    
    public void informarMensajeRecibido(){
        
    }
    
	public void setVentanaReceptor(IVentanaReceptor ventanaReceptor) {
        this.ventanaReceptor = ventanaReceptor;
        this.ventanaReceptor.addActionListener(this);
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
	}
}
