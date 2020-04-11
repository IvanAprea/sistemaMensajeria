package inicio;

import presentaci�n.VentanaEmisor;

import base.Agenda;
import negocio.Emisor;

import presentaci�n.IVentanaEmisor;

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
