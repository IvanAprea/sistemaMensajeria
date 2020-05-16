package negocio;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Mensaje {
	private String asunto;
	private String texto;
	private Persona emisor;
	private int tipo;
        private LocalDateTime fecha;
        
        public Mensaje() {
                super();
        }
            
	public Mensaje(String asunto, String texto, Persona emisor, int tip) {
		super();
		this.asunto = asunto;
		this.texto = texto;
		this.emisor = emisor;
		this.tipo = tipo;
	}

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return "["+dtf.format(this.getFecha())+"]    " + "De: " + emisor.toString() + " Asunto: " + asunto +((tipo == 1) ? "  [!] " : "     ");
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
    
    public void setearFecha(){
        this.fecha = LocalDateTime.now();    
    }

    public LocalDateTime getFecha() {
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
}
