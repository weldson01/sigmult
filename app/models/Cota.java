package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import net.bytebuddy.asm.Advice.This;
import play.db.jpa.Model;

@Entity
public class Cota extends Model{
	public String nome;
	public  int quantidadeTotal;
	public  int quantidadeDisponivel;
	@ManyToMany
	@JoinTable(name="usuario_cota")
	public List<Usuario> acessivelUser;
	
	
	@ManyToMany
	@JoinTable(name="pessoas_Adm")
	public List<Usuario> administradores;	
	@ManyToOne
	@JoinColumn(name="adm_id")
	public Usuario adm;
	
	@OneToMany(mappedBy="cota")
	public List<Impressao> impressoes;
	
	
}
