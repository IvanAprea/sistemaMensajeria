package inicio;

import base.ComunicacionMensajeria;

import negocio.Mensajeria;

public class InicioMensajeria {
    public InicioMensajeria() {
        super();
    }
    
    public static void main(String[] args) {
        ComunicacionMensajeria.getInstancia().escucharPuerto("71");
    }
}
