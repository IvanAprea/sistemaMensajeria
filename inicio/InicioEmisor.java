package inicio;

import base.ComunicacionEmisor;

import base.Encriptadora;

import negocio.GestorEnvioMensajes;

import presentacion.IVentanaEmisor;
import presentacion.VentanaEmisor2;

public class InicioEmisor {
    public InicioEmisor() {
        super();
    }

    public static void main(String[] args) {
        VentanaEmisor2 vista = new VentanaEmisor2();
        GestorEnvioMensajes.getInstancia().cargarDatosConfig("config.txt");
        GestorEnvioMensajes.getInstancia().setearIp();
        GestorEnvioMensajes.getInstancia().recuperarNoEnviados();
        GestorEnvioMensajes.getInstancia().enviarMensajesPendientes();
        GestorEnvioMensajes.getInstancia().persistirNoEnviados();
        GestorEnvioMensajes.getInstancia().setVista(vista);
        GestorEnvioMensajes.getInstancia().escucharPuerto();
        GestorEnvioMensajes.getInstancia().setEncriptador(new Encriptadora());
        vista.abrirSesion();
    }
}
