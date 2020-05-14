package inicio;

import presentacion.VentanaReceptor;
import negocio.NegocioReceptor;
import negocio.Mensaje;
import negocio.Persona;

public class inicioReceptor {
    
    public inicioReceptor() {
        super();
    }
    
    public static void main(String[] args) {
        VentanaReceptor ventana = new VentanaReceptor();
        NegocioReceptor.getInstancia().setearIp();
        NegocioReceptor.getInstancia().setVentanaReceptor(ventana);
        NegocioReceptor.getInstancia().cargarDatosConfig();
        ventana.pack();
        ventana.iniciarSesion();
    }

}
