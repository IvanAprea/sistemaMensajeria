package client;

public class Mensaje {
	private String asunto;
	private String texto;
	private Emisor emisor;
	private int tipo;
	
	public Mensaje(String asunto, String texto, Emisor emisor, int tipo) {
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
	public Emisor getEmisor() {
		return emisor;
	}
	
	public int getTipo() {
		return tipo;
	}
	
}
