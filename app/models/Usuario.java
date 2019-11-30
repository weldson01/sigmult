package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import enums.TipoUsuario;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import play.libs.Crypto;

@Entity
public class Usuario extends Model {
	public String nome;
	public String matricula;
	public String tipoVinculo;
	public String email;
	public String senha;
	public String url_foto_75x100;
	public TipoUsuario tipoUsuario;
	public Blob foto;

	

	@OneToMany(mappedBy = "usuario")
	public List<Requisicao> requisicao;
	
	@ManyToMany(mappedBy = "acessivelUser")
	public List<Cota> cotasDisponiveis;
	
	@ManyToMany(mappedBy = "administradores")
	public List<Cota> cotasAdm;
	@OneToMany(mappedBy= "usuarioImp")
	public List<Impressao> impressoes;
	
	//@OneToMany(mappedBy = "adm")
	//public List<Cota> cotasAdm;
	
	@OneToMany(mappedBy = "from")
	public List<Mensagem> mensagensEnviadas;
	
	@OneToMany(mappedBy = "to")
	public List<Mensagem> mensagensRecebidas;
	public Usuario(String nome, String email, String senha) {
		this.nome = nome;
		this.email = email;
		setSenha(senha);
	}

	
	
	@ManyToMany
	@JoinTable(name="pessoas_cotas")
	public List<Cota> cotas;
	
	public Usuario() {
		tipoUsuario = TipoUsuario.PADRAO;
	}

	public void setSenha(String s) {
		senha = Crypto.passwordHash(s);
	}
	
}
