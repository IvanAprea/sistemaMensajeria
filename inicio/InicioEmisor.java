package inicio;

import presentación.VentanaEmisor;

import base.Agenda;
import negocio.Emisor;

import presentación.IVentanaEmisor;

public class InicioEmisor {
    public InicioEmisor() {
        super();
    }

    public static void main(String[] args) {
        VentanaEmisor vista = new VentanaEmisor();
        vista.setVisible(true);
        Agenda.getInstance().cargarAgenda();
        Emisor.getInstancia().setearIp();
        Emisor.getInstancia().setVista(vista);
    }
}
