package inicio;

import Ventana.VentanaEmisor;

import client.Agenda;
import client.Emisor;

import interfaz.IVistaEmisor;

public class InicioEmisor {
    public InicioEmisor() {
        super();
    }

    public static void main(String[] args) {
        VentanaEmisor vista = new VentanaEmisor();
        vista.setVisible(true);
        Agenda agenda = new Agenda();
        agenda.cargarAgenda();
        Emisor emisor = new Emisor(vista,agenda);
    }
}
