package inicio;

import base.ComunicacionEmisor;

import negocio.NegocioEmisor;

import presentacion.IVentanaEmisor;
import presentacion.VentanaEmisor2;

public class InicioEmisor {
    public InicioEmisor() {
        super();
    }

    public static void main(String[] args) {
        VentanaEmisor2 vista = new VentanaEmisor2();
        NegocioEmisor.getInstancia().cargarDatosConfig();
        NegocioEmisor.getInstancia().setearIp();
        NegocioEmisor.getInstancia().setVista(vista);
        NegocioEmisor.getInstancia().escucharPuerto();
        vista.abrirSesion();
    }
}
