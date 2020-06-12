package inicio;

import base.Desencriptadora;

import presentacion.VentanaReceptor2;
import negocio.LogicaReceptor;
import negocio.Mensaje;
import negocio.Persona;

public class inicioReceptor {
    
    public inicioReceptor() {
        super();
    }
    
    public static void main(String[] args) {
        VentanaReceptor2 ventana = new VentanaReceptor2();
        LogicaReceptor.getInstancia().setearPuerto("81");
        LogicaReceptor.getInstancia().setearIp();
        LogicaReceptor.getInstancia().setVentanaReceptor(ventana);
        LogicaReceptor.getInstancia().cargarDatosConfig();
        LogicaReceptor.getInstancia().setDesencriptador(new Desencriptadora());
        ventana.pack();
        ventana.iniciarSesion();
    }

}
