package controllers;

import java.util.List;

import org.hibernate.SessionException;

import models.Cota;
import models.Usuario;
import play.mvc.Controller;

public class Cotas extends Controller{
	public static void form() {
		
		render();
	}
	public static void salvar(Cota cot, String matricula) {
		Usuario user = Usuario.find("matricula =?1", matricula).first();
		if(user != null) {
			cot.adm = user;
			cot.save();
			flash.success("Nova cota "+cot.nome+" criada com sucesso");
			listar();
		}else{
			flash.error("Matricula não encontrada para nenhum do usuários cadastrados!");
			renderTemplate("Cotas/form.html", cot);
		}
		
		
	}
	public static void editar() {
		
	}
	public static void deletar() {
		
	}
	public static void listar() {
		List<Cota> lista = Cota.findAll();
		render(lista);
	}
	public static void listarInd(){
		Usuario user = Usuario.find("matricula =?1", session.get("usuario.matricula")).first();
		List<Cota> lista = user.cotasDisponiveis;
		render(lista);
	}
	public static void encontrarUser(Long cotID,String search) {
		Cota cot = Cota.findById(cotID);
		Usuario user = Usuario.find("matricula =?1", search).first();
		renderTemplate("Cotas/adicionarPessoa.html", user, cot);
	}
	public static void adicionarUser(Long cotID, Long userID) {
		System.out.println(cotID);
		Cota cota = Cota.findById(cotID);
		Usuario usu = Usuario.findById(userID);
		if(usu != null) {
		cota.acessivelUser.add(usu);
		flash.success("Usuário adicionado a cota com sucesso!");
		cota.save();
		}else {
			flash.error("Erro ao tentar adicionar usuário a cota");
		}
		renderTemplate("Cotas/listarInd.html");
	}
	public static void detalhes(Long id) {
		Cota cot = Cota.findById(id);
		render(cot);
	}
	public static void adicionarPessoa(Long id) {
		Cota cot = Cota.findById(id);
		render(cot);
	}
}
