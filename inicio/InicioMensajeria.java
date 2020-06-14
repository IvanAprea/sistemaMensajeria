package inicio;

import base.ComunicacionMensajeria;

import negocio.LogicaMensajeria;

public class InicioMensajeria {
    public InicioMensajeria() {
        super();
    }
    
    public static void main(String[] args) {
        LogicaMensajeria.getInstancia().recuperarDatos();
        ComunicacionMensajeria.getInstancia().escucharPuerto("75");
        LogicaMensajeria.getInstancia().backUp();
    }
}
