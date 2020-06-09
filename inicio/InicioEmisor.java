package inicio;

import base.ComunicacionEmisor;

import negocio.LogicaEmisor;

import presentacion.IVentanaEmisor;
import presentacion.VentanaEmisor2;

public class InicioEmisor {
    public InicioEmisor() {
        super();
    }

    public static void main(String[] args) {
        VentanaEmisor2 vista = new VentanaEmisor2();
        LogicaEmisor.getInstancia().cargarDatosConfig();
        LogicaEmisor.getInstancia().setearIp();
        LogicaEmisor.getInstancia().setVista(vista);
        LogicaEmisor.getInstancia().escucharPuerto();
        vista.abrirSesion();
    }
}
