package client;

public class Mensaje {
	private String asunto;
	private String texto;
	private String emisor;
	
	public Mensaje(String asunto, String texto, String emisor) {
		super();
		this.asunto = asunto;
		this.texto = texto;
		this.emisor = emisor;
	}
	public String getAsunto() {
		return asunto;
	}
	public String getTexto() {
		return texto;
	}
	public String getEmisor() {
		return emisor;
	}
	
}
