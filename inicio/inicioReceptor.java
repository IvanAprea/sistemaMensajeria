package inicio;

import Ventana.VentanaReceptor;
import client.Receptor;
import client.Comunicacion;
import client.Mensaje;
import client.Persona;

public class inicioReceptor {

	public static void main(String[] args) {
		VentanaReceptor ventana = new VentanaReceptor();
		Receptor.getInstancia().setVentanaReceptor(ventana);
		Comunicacion.getInstancia().escucharPuerto(Receptor.getInstancia().getPuerto());
		ventana.pack();
		ventana.setVisible(true);
	}

}
