package inicio;

import base.ComunicacionDirectorio;

import negocio.NegocioDirectorio;
import negocio.NegocioEmisor;

import presentacion.VentanaEmisor;

public class InicioDirectorio {
    public InicioDirectorio() {
        super();
    }

    public static void main(String[] args) {
        ComunicacionDirectorio.getInstancia().escucharPuerto("70");
        NegocioDirectorio.getInstancia().comprobacionUsuariosOnline();
    }
}
