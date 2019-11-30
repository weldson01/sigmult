package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import enums.EstadoDaRequisicao;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Requisicao extends Model {
	@Required(message="Campo obrigatório")
	@MinSize(1)
	@MaxSize(200)
	public String causa;
	public EstadoDaRequisicao status;
	@ManyToOne
	@JoinColumn(name = "Material_id")
	public Material material;
 
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	public Usuario usuario; //Pessoa que fez o pedido
	
	@ManyToOne
	@JoinColumn(name = "usuario_responsavel_id")
	public Usuario usuario_responsavel; //administrador ou bolsista que aceitou ou negou
	static public Long emprestadosMensal = (long) 0;
}
