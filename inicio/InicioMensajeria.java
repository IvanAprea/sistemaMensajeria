package inicio;

import base.ComunicacionMensajeria;

import negocio.GestorServidorMensajeria;

public class InicioMensajeria {
    public InicioMensajeria() {
        super();
    }
    
    public static void main(String[] args) {
        GestorServidorMensajeria.getInstancia().recuperarDatos();
        ComunicacionMensajeria.getInstancia().escucharPuerto("71");
        GestorServidorMensajeria.getInstancia().backUp();
    }
}
