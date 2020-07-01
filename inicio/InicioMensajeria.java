package inicio;

import base.ComunicacionMensajeria;

import negocio.GestorEnvioMensajes;
import negocio.GestorServidorMensajeria;

public class InicioMensajeria {
    public InicioMensajeria() {
        super();
    }
    
    public static void main(String[] args) {
        GestorServidorMensajeria.getInstancia().recuperarDatos();
        GestorServidorMensajeria.getInstancia().cargarDatosConfig("configMensajeria.txt");
        ComunicacionMensajeria.getInstancia().escucharPuerto("71");
        GestorServidorMensajeria.getInstancia().backUp();
    }
}
