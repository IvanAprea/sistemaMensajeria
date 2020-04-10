package inicio;

import presentación.VentanaReceptor;
import negocio.Receptor;
import negocio.Mensaje;
import negocio.Persona;

public class inicioReceptor {
    
    public inicioReceptor() {
        super();
    }
    
    public static void main(String[] args) {
        VentanaReceptor ventana = new VentanaReceptor();
        Receptor.getInstancia().setearIp();
        Receptor.getInstancia().setVentanaReceptor(ventana);
        ventana.pack();
        ventana.setVisible(true);
        ventana.abrirConfiguracion();
    }

}
