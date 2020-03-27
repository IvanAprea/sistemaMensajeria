package interfaz;

import client.Persona;

import java.util.ArrayList;

public interface IVistaEmisor {
    String getAsunto();
    String getMensaje();
    Persona[] getPersonas();
    int getTipo();
    String getNombreConfig();
    String getApellidoConfig();
    void actualizarListaAgenda(ArrayList<Persona> personas);
}
