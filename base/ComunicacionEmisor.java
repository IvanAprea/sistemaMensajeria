package base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.StringWriter;

import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import negocio.Emisor;

public class ComunicacionEmisor {

    private static ComunicacionEmisor _instancia = null;
    private ServerSocket sepe; //sepe=socketEscucharPuertoEmisor
    private Socket sem; //sem=socketEnviarMensaje

    private ComunicacionEmisor() {
        super();
    }

    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static ComunicacionEmisor getInstancia() {
        if (_instancia == null)
            _instancia = new ComunicacionEmisor();
        return _instancia;
    }

    public synchronized void escucharPuerto(String puerto) {
        new Thread() {
            public void run() {
                try {
                    sepe = new ServerSocket(Integer.parseInt(puerto));
                    sepe.setSoTimeout(1000); // SACAR COMENTARIO
                    while (true) {
                        Socket soc = sepe.accept();
                        PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
                        DataInputStream dIn = new DataInputStream(soc.getInputStream());
                        String str = dIn.readUTF();
                        str = str.substring(1);
                        String aux = str;
                        InetAddress adr = InetAddress.getByName(str.split(":")[0]);
                        Emisor.getInstancia().recibirConfirmacion(aux);
                        soc.close();
                    }
                } catch (BindException e) {
                    Emisor.getInstancia().lanzarCartelError("ERROR: El puerto ya está siendo escuchado");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    //Este error se da cuando envio un mensaje a un puerto que es igual al que abro para recibir la confirmacion
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    try {
                        sepe.close();
                    } catch (IOException f) {
                        f.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void enviarMensaje(StringWriter mensaje, InetAddress ip, int puerto) {
        try {
            sem = new Socket(ip, puerto);
            DataOutputStream dOut = new DataOutputStream(sem.getOutputStream());
            dOut.writeUTF(mensaje.toString());
            dOut.flush();
            sem.close();

        } catch (IOException e) {
            try {
                sem.close();
            } catch (IOException f) {
                f.printStackTrace();
            }

        }
    }

    public Socket abrirConexionDirectorio(InetAddress ip, int puerto) {
        try {
            return new Socket(ip, puerto);
        } catch (IOException e) {
            Emisor.getInstancia().lanzarCartelError("ERROR al conectar con el directorio");
            return null;
        }
    }

    //Estas listas se tendrian que poder mostrar de alguna forma en la ventana:
    //Consultar a los que estan online -> devuelve lista de online

    //Consultar a los que estan offline _> devuelve lista de offline

    //Consultar a todos -> los devuelve a todos (destinatarios registrados)
}
