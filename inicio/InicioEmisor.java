package inicio;

import presentacion.VentanaEmisor;

import negocio.Emisor;

import presentacion.IVentanaEmisor;

public class InicioEmisor {
    public InicioEmisor() {
        super();
    }

    public static void main(String[] args) {
        VentanaEmisor vista = new VentanaEmisor();
        vista.setVisible(true);
        Emisor.getInstancia().cargarDatosDirectorio();
        Emisor.getInstancia().setearIp();
        Emisor.getInstancia().setVista(vista);
    }
}
