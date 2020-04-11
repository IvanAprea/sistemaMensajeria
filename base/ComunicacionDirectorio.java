package base;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import negocio.Directorio;
import negocio.Emisor;

public class ComunicacionDirectorio {

    private static ComunicacionDirectorio _instancia = null;
    private ServerSocket sepd; //sepe=socketEscucharPuertoDirectorio
    private Socket socket;
    
    private ComunicacionDirectorio() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static ComunicacionDirectorio getInstancia()
    {
        if(_instancia == null)
            _instancia = new ComunicacionDirectorio();
        return _instancia;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized void escucharPuerto(String puerto) {
        new Thread() {
            public void run() {
                try {
                    sepd = new ServerSocket(Integer.parseInt(puerto));
                    sepd.setSoTimeout(1000);
                    while (true) {
                        ComunicacionDirectorio.getInstancia().setSocket(sepd.accept());
                        Socket soc = ComunicacionDirectorio.getInstancia().getSocket();                        
                        PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
                        DataInputStream dIn = new DataInputStream(soc.getInputStream());
                        String str = dIn.readUTF();
                        str = str.substring(1);
                        Directorio.getInstancia().ejecutarComando(str);
                    }
                    //ver donde cerrar el socket
                } catch (BindException e) {
                    Emisor.getInstancia().lanzarCartelError("ERROR: El puerto ya está siendo escuchado");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                 } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    try {
                        sepd.close();
                    } catch (IOException f) {
                        f.printStackTrace();
                    }
                }
            }
        }.start();
    }
    
    public void darLista(){
        
    }
    
    public void cerrarSocket(){
        try {
            this.getSocket().close();
        } catch (IOException e) {
            //Ver como lanzar error en Directorio
        }
    }
}
