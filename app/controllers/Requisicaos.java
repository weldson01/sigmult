package controllers;

import java.util.List;

import enums.EstadoDaMensagem;
import enums.EstadoDaRequisicao;
import enums.EstadoDoMaterial;
import enums.TipoMensagem;
import enums.TipoUsuario;
import models.Material;
import models.Mensagem;
import models.Requisicao;
import models.Usuario;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

//@With(Seguranca.class)
public class Requisicaos extends Controller {
	@Before(only = {"status","listargeral","devolver"})
	public static void validation() {
		if (!session.get("usuario.tipo").equals("ADMIN") && !session.get("usuario.tipo").equals("1")) {
			//System.out.println(session.get("usuario.tipo"));
			renderTemplate("Requisicaos/error.html");
		}
	}

	public static void form() {
		String email = session.get("usuario.email");
		List<Material> lista = Material.findAll();
		render(email, lista);
	}

	public static void salvar(@Valid Requisicao req, long idMaterial) {
		Usuario user = Usuario.find("email=?1", session.get("usuario.email")).first();
		req.material = Material.findById(idMaterial);
		req.status = EstadoDaRequisicao.AGUARDANDO_APROVACAO;
		if(validation.hasErrors()) {
			validation.keep();
			form();
		}
		if (req.material.status == EstadoDoMaterial.INDISPONIVEL) {
			flash.error("Material não disponível");
			form();
		}
		req.usuario = user;
		req.save();
		flash.success("Requisição solicitada com sucesso");
		form();
	}

	public static void editar(Long id) {
		Requisicao req = Requisicao.findById(id);
		String email = session.get("usuario.email");
		List<Material> lista = Material.findAll();
		renderTemplate("Requisicaos/form.html", req, email, lista);
	}

	public static void listargeral() {
		// listar todas requisições
		List<Requisicao> lista = Requisicao.findAll();
		renderTemplate("Requisicaos/listaprov.html", lista);
	}

	public static void listarind() {
		// para listar apenas requisições de um unico usuario
		String email = session.get("usuario.email");
		Usuario user = Usuario.find("email =?1", email).first();
		List<Requisicao> lista = user.requisicao;
		renderTemplate("Requisicaos/listar.html", lista);
	}

	public static void deletar(Long id) {
		Requisicao req = Requisicao.findById(id);
		req.delete();
		flash.success("Deletado");
		listarind();
	}

	public static void status(boolean aceito, Long id) {
		//Mensagem
		Mensagem msm = new Mensagem();
		Usuario userAtual = Usuario.findById(Long.parseLong(session.get("usuario.id")));
		// vai aceitar ou negar a solicitação
		
		Requisicao req = Requisicao.findById(id);
		if (req.material.status.equals(EstadoDoMaterial.INDISPONIVEL)
				|| req.material.status.equals(EstadoDoMaterial.PENDENTE)) {
			flash.error("Material já emprestado");
			listargeral();
		}
		if (aceito == true) {
			// req.material.status = EstadoDoMaterial.INDISPONIVEL;
			// Materials.salvar(req.material, true);
			req.material.status = EstadoDoMaterial.INDISPONIVEL;
			req.material.save();
			req.status = EstadoDaRequisicao.ACEITO;
			req.usuario_responsavel = Usuario.findById(Long.parseLong(session.get("usuario.id")));
			req.save();
			System.out.println(req.material.status);
			//enviar mensagem de sucesso para o usuário mandando ele ir buscar o material
			
			msm.from = userAtual;
			msm.to = req.usuario;
			msm.frase = ("Sua solicitação do '" + req.material.nome +"' foi aceita, você já pode ir até a sala da MULTIMEIOS para pegar o material.");
			msm.tipo = TipoMensagem.ACEITO;
			Requisicao.emprestadosMensal++;
			msm.save();
			//Mensagems.enviarMensagem(msm);
			//fim do envio da mensagem interna
			flash.success("Aceito com sucesso");
		} else {
			req.status = EstadoDaRequisicao.NEGADO;
			req.save();
			flash.success("Recusado com sucesso");
			//enviar mensagem de sucesso para o usuário mandando ele ir buscar o material
			msm.from = userAtual;
			msm.to = req.usuario;
			msm.frase = ("Sua solicitação do '" + req.material.nome +"' foi aceita, você já pode ir até a sala da MULTIMEIOS para pegar o material.");
			msm.tipo = TipoMensagem.RECUSADO;
			msm.save();
			Mensagems.enviarMensagem(msm);
			//fim do envio da mensagem interna
		}
		listargeral();
	}

	public static void devolucao() {
		render();
	}

	public static void devolver(long id) {
		Requisicao req = Requisicao.findById(id);
		req.material.status = EstadoDoMaterial.DISPONIVEL;
		req.material.save();
		req.delete();
	}
	public static void detalhes(Long id) {
		Requisicao req = Requisicao.findById(id);
		render(req);
	}
}
