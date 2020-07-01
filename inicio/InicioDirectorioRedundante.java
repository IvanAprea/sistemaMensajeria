package inicio;

import base.ComunicacionDirectorio;

import negocio.GestorUsuariosReceptores;

public class InicioDirectorioRedundante {
    public InicioDirectorioRedundante() {
        super();
    }

    public static void main(String[] args) {
        GestorUsuariosReceptores.getInstancia().cargarDatosConfig("configDirRedundante.txt");
        GestorUsuariosReceptores.getInstancia().solicitarBackUp();
        ComunicacionDirectorio.getInstancia().escucharPuerto("72");
        GestorUsuariosReceptores.getInstancia().comprobacionUsuariosOnline();
    }
}
