package inicio;

import base.ComunicacionDirectorio;

import negocio.LogicaDirectorio;
import negocio.LogicaEmisor;

import presentacion.VentanaEmisorOld;

public class InicioDirectorio {
    public InicioDirectorio() {
        super();
    }

    public static void main(String[] args) {
        ComunicacionDirectorio.getInstancia().escucharPuerto("70");
        LogicaDirectorio.getInstancia().comprobacionUsuariosOnline();
    }
}
