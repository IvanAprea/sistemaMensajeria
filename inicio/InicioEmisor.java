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
        
        final String nombreConfigDirectorio="config.txt";
                
        VentanaEmisor2 vista = new VentanaEmisor2();
        LogicaEmisor.getInstancia().cargarDatosConfig(nombreConfigDirectorio);
        LogicaEmisor.getInstancia().setearIp();
        LogicaEmisor.getInstancia().setVista(vista);
        ComunicacionEmisor.getInstancia().escucharPuerto(LogicaEmisor.getInstancia().getPuerto());
        LogicaEmisor.getInstancia().setEncriptador(new Encriptadora());
        LogicaEmisor.getInstancia().recuperarNoEnviados();
        vista.abrirSesion();
    }
}
