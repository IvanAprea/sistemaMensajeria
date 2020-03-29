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
    void actualizarListaAgenda(ArrayList<Persona> personas);
    void addActionListener(ActionListener actionListener);
    void abrirConfig();
    void cerrarConfig();
    void mostrarPanelMsjRecibido();
    String getIPConfig();
    String getPuertoConfig();
}
