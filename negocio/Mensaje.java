package negocio;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Mensaje {
	private byte[] texto,asunto;
	private Persona emisor;
	private int tipo;
        private String fecha;
        
        public Mensaje() {
                super();
        }
            
	public Mensaje(byte[] asunto, byte[] texto, Persona emisor, int tipo) {
		super();
		this.asunto = asunto;
		this.texto = texto;
		this.emisor = emisor;
		this.tipo = tipo;
	}

    @Override
    public String toString() {
        
        return "["+this.fecha+"]    " + "De: " + emisor.toString() + " Asunto: " + asunto +((tipo == 1) ? "  [!] " : "     ");
    }

    public void setAsunto(byte[] asunto) {
        this.asunto = asunto;
    }

    public byte[] getAsunto() {
        return asunto;
    }

    public void setTexto(byte[] texto) {
        this.texto = texto;
    }

    public byte[] getTexto() {
        return texto;
    }
    
    public void setearFecha(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.fecha = dtf.format(LocalDateTime.now());
    }

    public String getFecha() {
        return fecha;
    }

    public void setEmisor(Persona emisor) {
        this.emisor = emisor;
    }

    public Persona getEmisor() {
        return emisor;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }
}
