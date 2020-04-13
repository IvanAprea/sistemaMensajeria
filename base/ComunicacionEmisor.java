package base;

import exceptions.excepcionEnviarMensaje;

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
    private Socket s; //sem=socketEnviarMensaje

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

    /*public synchronized void escucharPuerto(String puerto) {
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
    }*/

    public void enviarMensaje(StringWriter mensaje, InetAddress ip, int puerto,int tipo) throws excepcionEnviarMensaje {
        try {
            s = new Socket(ip, puerto);
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeUTF(mensaje.toString());
            dOut.flush();
            if(tipo == 2){
                DataInputStream dIn = new DataInputStream(s.getInputStream());
                String resultado = dIn.readUTF();
                Emisor.getInstancia().recibirConfirmacion(resultado);
            }
            s.close();

        } catch (IOException e) {
            try {
                if(s!=null)
                    s.close();
                throw new excepcionEnviarMensaje();
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
    
    public String pedirListaADirectorio(Socket socket){
        String hm;
        try 
        {
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            String s = "GET";
            dOut.writeUTF(s);
            dOut.flush();         
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            return dIn.readUTF();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return null;
        }
    }
    
}
