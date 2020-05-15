package inicio;

import base.ComunicacionEmisor;

import presentacion.VentanaEmisor;

import negocio.NegocioEmisor;

import presentacion.IVentanaEmisor;

public class InicioEmisor {
    public InicioEmisor() {
        super();
    }

    public static void main(String[] args) {
        VentanaEmisor vista = new VentanaEmisor();
        NegocioEmisor.getInstancia().cargarDatosConfig();
        NegocioEmisor.getInstancia().setearIp();
        NegocioEmisor.getInstancia().setVista(vista);
        NegocioEmisor.getInstancia().pedirAvisosPendientes();
        NegocioEmisor.getInstancia().escucharPuerto();
        vista.abrirSesion();
    }
}
