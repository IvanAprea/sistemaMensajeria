package inicio;

import presentacion.VentanaReceptor;
import presentacion.VentanaReceptor2;
import negocio.NegocioReceptor;
import negocio.Mensaje;
import negocio.Persona;

public class inicioReceptor {
    
    public inicioReceptor() {
        super();
    }
    
    public static void main(String[] args) {
        VentanaReceptor2 ventana = new VentanaReceptor2();
        NegocioReceptor.getInstancia().setearPuerto("81");
        NegocioReceptor.getInstancia().setearIp();
        NegocioReceptor.getInstancia().setVentanaReceptor(ventana);
        NegocioReceptor.getInstancia().cargarDatosConfig();
        ventana.pack();
        ventana.iniciarSesion();
    }

}
