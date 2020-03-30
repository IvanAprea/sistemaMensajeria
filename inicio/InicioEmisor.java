package inicio;

import Ventana.VentanaEmisor;

import client.Agenda;
import client.Emisor;

import interfaz.IVentanaEmisor;

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
