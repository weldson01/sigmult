package controllers;

import java.util.HashMap;
import java.util.Map;

import enums.TipoUsuario;
import models.Usuario;
import play.libs.Crypto;
import play.libs.WS;
import play.mvc.Controller;


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import models.DadosSuap;
import models.Usuario;
import play.libs.WS;
import play.mvc.Before;
import play.mvc.Controller;
import com.google.gson.Gson;

public class Logins extends Controller {
	public static void form() {
		if(session.contains("usuario.id")) {
			Application.index();
		}
		render();
	}
	//Autenticação pelo suap
	public static void autenticarSuap(String matricula, String senha) {
		//checar se o usuário que está logando é um administrador
		Usuario user = Usuario.find("matricula = ?1 and senha = ?2", matricula, Crypto.passwordHash(senha)).first();
		if (user != null && user.tipoUsuario == TipoUsuario.ADMIN) {
			System.out.println("entrou no if");
			session.put("usuario.matricula", user.matricula);
			session.put("usuario.email", user.email);
			session.put("usuario.nome", user.nome);
			session.put("usuario.id", user.id);
			session.put("usuario.tipo", user.tipoUsuario);
			Application.index();

		} else {
			//acessando ao SUAP para obtenção de informações do usuario
			WS.HttpResponse resposta;

			String urlToken = "https://suap.ifrn.edu.br/api/v2/autenticacao/token/";
			String urlDados = "https://suap.ifrn.edu.br/api/v2/minhas-informacoes/meus-dados/";

			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("username", matricula);
			parametros.put("password", senha);

			resposta = WS.url(urlToken).params(parametros).post();

			if (resposta.success()) {

				String token = resposta.getJson().getAsJsonObject().get("token").getAsString();
				Map<String, String> header = new HashMap<String, String>();
				header.put("X-CSRFToken", token);
				header.put("Authorization", "JWT " + token);

				resposta = WS.url(urlDados).headers(header).get();

				DadosSuap dadosSUAP = new Gson().fromJson(resposta.getString(), DadosSuap.class);

				Usuario usuario = Usuario.find("matricula = ?1", dadosSUAP.matricula).first();

				if (usuario == null) {
					usuario = new Usuario();
					usuario.nome = dadosSUAP.nome_usual;
					usuario.matricula = dadosSUAP.matricula;
					usuario.tipoVinculo = dadosSUAP.tipo_vinculo;
					usuario.url_foto_75x100 = "http://suap.ifrn.edu.br" + dadosSUAP.url_foto_75x100;
					usuario.email = dadosSUAP.email;
					usuario.save();
				}

				session.put("usuario.email", usuario.email);
				session.put("usuario.nome", usuario.nome);
				session.put("usuario.foto", usuario.url_foto_75x100);
				session.put("usuario.id", usuario.id);
				session.put("usuario.tipo", usuario.tipoUsuario);
				session.put("usuario.matricula", usuario.matricula);
				Application.index();// Página inicial
				return ;
				
			} else {
				flash.error("Matricula/Email ou senha inválidos");
				form();// Redireciona para o form de login
			}
		}
	}
	//modelagem antiga
	public static void logar(String email, String senha) {
		Usuario user = Usuario.find("email = ?1 and senha = ?2", email, Crypto.passwordHash(senha)).first();
		if (user == null) {
			// System.out.println(email + " " + Crypto.passwordHash(senha));
			flash.error("E-mail ou senha inválidos");
			form(); // Redireciona para form de login
		} else {

			session.put("usuario.email", user.email);
			session.put("usuario.nome", user.nome);
			session.put("usuario.id", user.id);
			session.put("usuario.tipo", user.tipoUsuario);

			Application.index(); // Página inicial
		}

	}
	//metodo para loggout
	public static void sair() {
		session.clear();
		renderTemplate("Logins/form.html");
	}
}
