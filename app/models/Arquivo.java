package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Blob;
import play.db.jpa.Model;
@Entity
public class Arquivo extends Model{
	public Blob arquivo;
	public String tamanhoFolha;
	public String intervaloImpressao;
	public int quantidadeCopias;
	public String frenteVerso;
	
	//erro nesta classe
	@OneToOne
	@JoinColumn(name="impressao_id")
	public Impressao impressao;

}
