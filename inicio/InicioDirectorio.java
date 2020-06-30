package inicio;

import base.ComunicacionDirectorio;

import negocio.GestorUsuariosReceptores;
import negocio.GestorEnvioMensajes;

import presentacion.VentanaEmisorOld;

public class InicioDirectorio {
    public InicioDirectorio() {
        super();
    }

    public static void main(String[] args) {
        ComunicacionDirectorio.getInstancia().escucharPuerto("70");
        GestorUsuariosReceptores.getInstancia().comprobacionUsuariosOnline();
    }
}
