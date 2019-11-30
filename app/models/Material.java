package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import enums.EstadoDoMaterial;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Material extends Model {
	@MinSize(1)
	@MaxSize(40)
	@Required
	public String nome;
	@MinSize(1)
	@MaxSize(40)
	@Required
	public String cod;
	public EstadoDoMaterial status;
	public Blob foto;

	@OneToMany(mappedBy = "material")
	public List<Requisicao> requisicaos;
}
