package interfaz;

import client.Persona;

import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

public interface IVistaEmisor {
    String getAsunto();
    String getMensaje();
    List<Persona> getPersonas();
    int getTipo();
    String getNombreConfig();
    String getApellidoConfig();
    void enviarMensaje();
    void actualizarListaAgenda(ArrayList<Persona> personas);
    void addActionListener(ActionListener actionListener);
    void abrirConfig();
    void confirmarConfiguracion();
    void cerrarConfig();
    void lanzarCartelError(String err);
    String getIPConfig();
    String getPuertoConfig();
}
