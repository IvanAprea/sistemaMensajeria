package interfaz;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.sound.sampled.Clip;

import client.Mensaje;

public interface IVentanaReceptor 
{
	static final String SPEAKER_IMAGE_URL = "./resource/speakericon.png";
	static final String ALERT_SOUND_URL = "./resource/incomingsound.wav";
	
	void actualizaListaMensajes(Mensaje mensaje);
	void abrirMensaje();
	void cerrarMensaje();
	void lanzarAlerta(String emisor);
	void pararAlerta();
	void addActionListener(ActionListener actionListener);
	Clip getClip();
	void setClip(Clip clip);
}
