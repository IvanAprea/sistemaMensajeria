package client;

public class Mensaje {
	private String asunto;
	private String texto;
	private Persona emisor;
	private int tipo;
	
	public Mensaje(String asunto, String texto, Persona emisor, int tipo) {
		super();
		this.asunto = asunto;
		this.texto = texto;
		this.emisor = emisor;
		this.tipo = tipo;
	}
	public String getAsunto() {
		return asunto;
	}
	public String getTexto() {
		return texto;
	}
	public Persona getEmisor() {
		return emisor;
	}
	
	public int getTipo() {
		return tipo;
	}
	@Override
	public String toString() {
		return "De: " + emisor.toString() + " Asunto: " + asunto;
	}
	
	
}
