package inicio;

import base.ComunicacionDirectorio;

import negocio.Directorio;
import negocio.Emisor;

import presentacion.VentanaEmisor;

public class InicioDirectorio {
    public InicioDirectorio() {
        super();
    }

    public static void main(String[] args) {
        ComunicacionDirectorio.getInstancia().escucharPuerto("70");
        Directorio.getInstancia().comprobacionUsuariosOnline();
    }
}
