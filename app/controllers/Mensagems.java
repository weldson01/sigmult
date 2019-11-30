package controllers;

import models.Mensagem;
import models.Usuario;
import play.db.jpa.Model;
import play.mvc.Controller;
import java.util.List;

import enums.EstadoDaMensagem;

public class Mensagems extends Controller{
	public static void form() {
		params.get("Long id");
		render();
	}
	public static void editar(Long id) {
		Mensagem m = Mensagem.findById(id);
		renderTemplate("Mensagems/form.html", m);
	}
	public static void salver(Mensagem m) {
		m.save();
	}
	public static void deletar(Long id) {
		Mensagem m = Mensagem.findById(id);
		m.delete();
	}
	public static void listarGeral() {
		List<Mensagem> lista = Mensagem.findAll();
		renderTemplate("Mensagems/listar.html", lista);
	}
	public static void listarAtual() {
		Usuario user = Usuario.findById(session.get("usuario.id"));
		List<Mensagem> lista = user.mensagensRecebidas;
		renderTemplate("Mensagems/listar.html", lista);
	}
	public static void enviarMensagem(Mensagem mensagem) {
		//para enviar mensagem por email
	}
	public static void msgNL() {
		//mostra o número de mensagens não lidas
		Usuario user = Usuario.findById(session.get("usuario.id"));
		int msgNL = 0;
		for(int i = 0; i<user.mensagensRecebidas.size(); i++) {
			if(user.mensagensRecebidas.get(i).status == EstadoDaMensagem.NAOLIDO) {
				msgNL++;
			}
		}
		renderText(msgNL);
	}
}
