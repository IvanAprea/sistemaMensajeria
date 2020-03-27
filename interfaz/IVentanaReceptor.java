package interfaz;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import client.Mensaje;

public interface IVentanaReceptor 
{
	void actualizaListaMensajes(Mensaje mensaje);
	void addActionListener(ActionListener actionListener);
}
