package inicio;

import presentacion.VentanaEmisor;

import negocio.NegocioEmisor;

import presentacion.IVentanaEmisor;

public class InicioEmisor {
    public InicioEmisor() {
        super();
    }

    public static void main(String[] args) {
        VentanaEmisor vista = new VentanaEmisor();
        NegocioEmisor.getInstancia().cargarDatosDirectorio();
        NegocioEmisor.getInstancia().setearIp();
        NegocioEmisor.getInstancia().setVista(vista);
        vista.abrirSesion();
    }
}
