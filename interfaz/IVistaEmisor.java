package interfaz;

import client.Persona;

import java.awt.event.ActionListener;

import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IVistaEmisor {
    String getAsunto();
    String getMensaje();
    List<Persona> getPersonas();
    int getTipo();
    String getNombreConfig();
    String getApellidoConfig();
    void enviarMensaje();
    void actualizarListaAgenda(HashMap<String,Persona> personas);
    void addActionListener(ActionListener actionListener);
    void abrirConfig();
    void confirmarConfiguracion();
    void cerrarConfig();
    void lanzarCartelError(String err);
    String getIPConfig();
    String getPuertoConfig();
    void addKeyListener(KeyListener kl1,KeyListener kl2);
}
