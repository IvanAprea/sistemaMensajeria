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
import java.net.SocketTimeoutException;

import java.net.UnknownHostException;

import negocio.Mensajeria;
import negocio.NegocioDirectorio;
import negocio.NegocioEmisor;
import negocio.NegocioReceptor;

public class ComunicacionMensajeria {
    
    private static ComunicacionMensajeria _instancia = null;
    private ServerSocket sepd;
    private Socket socket;
    private DataInputStream dIn;
    private DataOutputStream dOut;
    private PrintWriter out;
    
    private ComunicacionMensajeria() {
        super();
    }
    
    public synchronized static ComunicacionMensajeria getInstancia(){
        if(_instancia == null)
            _instancia = new ComunicacionMensajeria();
        return _instancia;
    }
    
    public synchronized void escucharPuerto(String puerto) {
        new Thread() {
            public void run() {
                try {
                    sepd = new ServerSocket(Integer.parseInt(puerto));
                    while (true) {
                        socket = sepd.accept();
                        out = new PrintWriter(socket.getOutputStream(), true);
                        dIn = new DataInputStream(socket.getInputStream());
                        Mensajeria.getInstancia().ejecutarComando(dIn.readUTF());
                        socket.close();
                    }
                } catch (BindException e) {
                    e.printStackTrace();
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
    
    public synchronized void enviarMensaje(StringWriter mensaje, InetAddress iprec, int puertorec, int tipo, InetAddress ipem, int puertoem) throws excepcionEnviarMensaje, IOException {
            socket = new Socket(iprec, puertorec);
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            dIn = new DataInputStream(socket.getInputStream());
            dOut.writeUTF(mensaje.toString());
            dOut.flush();
            if(tipo == 2){
            try
            {
                String msjAux;
                msjAux = ComunicacionMensajeria.getInstancia().recibirMsj();
                socket.close();
                socket = new Socket(ipem,puertoem);
                ComunicacionMensajeria.getInstancia().notificarEmisorConLlegadaMsj(msjAux);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            }
            socket.close();
    }
    
    public synchronized void enviarPendientes(StringWriter mensaje) throws IOException {
        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
        dOut.writeUTF(mensaje.toString());
        dOut.flush();
    }
    
    
    public synchronized String recibirMsj() throws Exception{
            String msj = dIn.readUTF();
            return msj;
    }

    public synchronized void notificarEmisorConLlegadaMsj(String nombreRec) throws IOException {
        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
        dOut.writeUTF(nombreRec);
        dOut.flush();
    }

}
