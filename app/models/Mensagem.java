package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import enums.EstadoDaMensagem;
import enums.TipoMensagem;
import play.db.jpa.Model;
@Entity
public class Mensagem extends Model{
	//Estado da mensagem lido, não lido...
		public EstadoDaMensagem status;
	//tipo de requisição
		public TipoMensagem tipo;
	//Mensagem a ser mostrada
		public String frase;
	@ManyToOne
	@JoinColumn(name = "usuarioTo_id")
	public Usuario to;
	@ManyToOne
	@JoinColumn(name = "usuarioFrom_id")
	public Usuario from;
	
}
