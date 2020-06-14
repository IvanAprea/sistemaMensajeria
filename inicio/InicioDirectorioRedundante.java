package inicio;

import base.ComunicacionDirActivo;

import base.ComunicacionDirRedundante;

import negocio.LogicaDirectorio;

public class InicioDirectorioRedundante
{
    public InicioDirectorioRedundante()
    {
        super();
    }

    public static void main(String[] args)
    {
        LogicaDirectorio.getInstancia().setComDir(new ComunicacionDirRedundante());
        LogicaDirectorio.getInstancia().cargarDatosConfig("configDirRedundante.txt");
        LogicaDirectorio.getInstancia().getComDir().escucharPuerto("71");
        if(!LogicaDirectorio.getInstancia().getComDir().conectarDirectorio())
            LogicaDirectorio.getInstancia().getComDir().escucharDirectorio("73");
    }
}
