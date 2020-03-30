package presentación;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.sound.sampled.Clip;

import negocio.Mensaje;

public interface IVentanaReceptor 
{
	static final String ALERT_SOUND_URL = "./resource/incomingsound.wav";
	
	void actualizaListaMensajes(Mensaje mensaje);
	void abrirMensaje();
	void cerrarMensaje();
	void lanzarAlerta(String emisor);
	void pararAlerta();
	void confirmarConfiguracion();
	void abrirConfiguracion();
	void lanzarCartelError(String err);
	void addActionListener(ActionListener actionListener);
	Clip getClip();
	void setClip(Clip clip);
}
