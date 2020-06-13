package inicio;

import base.ComunicacionEmisor;

import base.Encriptadora;

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
        LogicaEmisor.getInstancia().setEncriptador(new Encriptadora());
        LogicaEmisor.getInstancia().recuperarNoEnviados();
        vista.abrirSesion();
    }
}
