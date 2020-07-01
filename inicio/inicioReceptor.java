package inicio;

import base.Desencriptadora;

import presentacion.VentanaReceptor2;
import negocio.GestorRecepcionMensajes;
import negocio.Mensaje;
import negocio.Persona;

public class inicioReceptor {
    
    public inicioReceptor() {
        super();
    }
    
    public static void main(String[] args) {
        VentanaReceptor2 ventana = new VentanaReceptor2();
        GestorRecepcionMensajes.getInstancia().setearPuerto("81");
        GestorRecepcionMensajes.getInstancia().setearIp();
        GestorRecepcionMensajes.getInstancia().setVentanaReceptor(ventana);
        GestorRecepcionMensajes.getInstancia().cargarDatosConfig("config.txt");
        GestorRecepcionMensajes.getInstancia().setDesencriptador(new Desencriptadora());
        ventana.pack();
        ventana.iniciarSesion();
    }

}
