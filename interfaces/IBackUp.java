package interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface IBackUp
{
    void backUp(Object mensajesNoEnviados,String fileName);
    Object recuperarDatos(String fileName);
}
