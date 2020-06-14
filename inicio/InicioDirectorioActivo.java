package inicio;

import base.ComunicacionDirActivo;
import base.ComunicacionDirectorio;

import negocio.LogicaDirectorio;
import negocio.LogicaEmisor;

public class InicioDirectorioActivo {
    public InicioDirectorioActivo() {
        super();
    }

    public static void main(String[] args) {
        LogicaDirectorio.getInstancia().setComDir(new ComunicacionDirActivo());
        LogicaDirectorio.getInstancia().cargarDatosConfig("configDirActivo.txt");
        //levantar los datos del directorio y donde escuchar
        LogicaDirectorio.getInstancia().getComDir().escucharPuerto("70");
        //conectar al otro directorio
        LogicaDirectorio.getInstancia().getComDir().conectarDirectorio();
        LogicaDirectorio.getInstancia().getComDir().escucharDirectorio("72");
        LogicaDirectorio.getInstancia().comprobacionUsuariosOnline();
    }
}
