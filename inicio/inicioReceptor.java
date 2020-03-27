package inicio;

import Ventana.VentanaReceptor;
import client.Receptor;
import client.Mensaje;
import client.Persona;

public class inicioReceptor {

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
    
	public static void main(String[] args) {
		VentanaReceptor ventana = new VentanaReceptor();
		Receptor rec = new Receptor(ventana);
		ventana.pack();
		ventana.setVisible(true);
		Persona p1 = new Persona();
		p1.setIP("101.144.230.0.2");
		p1.setPuerto("4444");
		Mensaje msj1 = new Mensaje("Nueva guia subida", "Estimados alumnos, he subido...", p1, 0);
		setTimeout(() -> rec.recibirMensaje(msj1), 4000);
	}

}