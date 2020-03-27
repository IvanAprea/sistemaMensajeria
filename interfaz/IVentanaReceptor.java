package interfaz;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import client.Mensaje;

public interface IVentanaReceptor 
{
	void actualizaListaMensajes(Mensaje mensaje);
	void abrirMensaje();
	void cerrarMensaje();
	void pararAlerta();
	void addActionListener(ActionListener actionListener);
}
