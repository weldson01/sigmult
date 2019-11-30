package controllers;

import java.util.List;

import org.apache.log4j.lf5.PassingLogRecordFilter;

import enums.TipoUsuario;
import models.Usuario;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

//@With(Seguranca.class)//problema com isso
public class Usuarios extends Controller {
	@Before(only="listar")
	 static void checkAuthentification() {
        if(!session.contains("usuario.tipo")) {
        	Logins.form();
        }
        if(!session.get("usuario.tipo").equals("ADMIN")){
        	Application.index();
        }
    }
	public static void form(Usuario user) {
		
		
		render(user);
	}

	public static void salvar(Usuario user, String senha, String confirm_password) {
		if (senha.equals(confirm_password) == false) {
			flash.error("senhas não coincidem");
			form(user);
		}
		if (senha.equals("") == false) {
			user.setSenha(senha);
		}
		user.save();
		renderTemplate("Logins/form.html");
	}

	public static void listar() {
		String busca = params.get("buscar");
		List<Usuario> lista;
		if (busca == null) {
			lista = Usuario.findAll();
		} else {
			lista = Usuario.find("select u from Usuario u " + "where u.nome like ?1 or " + "u.email like ?1",
					"%" + busca + "%").fetch();
		}
		render(lista);
	}

	public static void deletar(long id) {
		Usuario u = Usuario.findById(id);
		u.delete();
		listar();
	}

	public static void editar(long id) {
		Usuario user = Usuario.findById(id);
		// TipoUsuario[] tipo = TipoUsuario.values();//futuro para colocar usuarios.
		renderTemplate("Usuarios/form.html", user);
	}
	public static void mostrarImagem(Long id) {
		Usuario usuario = Usuario.findById(id);
		renderBinary(usuario.foto.get());
	}
	public static void mostrarImagemAtual() {
		Usuario usuario = Usuario.findById(Long.parseLong(session.get("usuario.id")));	
		renderBinary(usuario.foto.get());
	}
}
