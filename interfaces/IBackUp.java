package interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface IBackUp
{
    void backUp(HashMap<String, ArrayList<String>> mensajesNoEnviados,String fileName);
    HashMap<String, ArrayList<String>> recuperarDatos(String fileName);
}
