package controllers;

import java.io.File;
import java.util.List;

import org.junit.Before;

import enums.EstadoDaRequisicao;
import enums.EstadoDoMaterial;
import enums.TipoUsuario;
import models.Material;
import models.Requisicao;
import play.cache.Cache;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(Seguranca.class)
public class Materials extends Controller {
	public static void form() {
		Material mat = (Material)Cache.get("mat");
		Cache.clear();
		// List<Material> lista = Material.findAll();
		render(mat);
	}

	public static void salvar(@Valid Material mat, Boolean status) {
		if(status != null) {
		if (status == false) {
			
			mat.status = EstadoDoMaterial.DISPONIVEL;
			} else {
			mat.status = EstadoDoMaterial.INDISPONIVEL;
			}
		}
		
		if (validation.hasErrors()) {
			validation.keep();
			
			flash.error("Falha ao tentar salvar material, cheque os campos e tente novamente.");
			Cache.set("mat", mat);
			
			form();
		}else {
		mat.save();
		flash.success("Material cadastrado.");
		}
		listar();
	}

	public static void listar() {
		List<Material> lista = Material.findAll();
		render(lista);
	}

	public static void deletar(Long id) {
		Material mat = Material.findById(id);
		mat.delete();
		listar();
	}

	public static void editar(Long id) {
		Material mat = Material.findById(id);
		renderTemplate("Materials/form.html", mat);
	}

	public static void devolucao() {
		//futuramente colocar uma lista com os materiais
		//List<Material> lista = Material.find("status =?1 ", EstadoDoMaterial.INDISPONIVEL).fetch();
		render();
	}
	public static void detalhes(Long idMaterial) {
		Material mat = Material.find("id =?1", idMaterial).first();
		Requisicao req = Requisicao.find("status=?1 and material=?2 ", EstadoDaRequisicao.ACEITO, mat).first();
		// pensando no que fazer aqui hem
		renderTemplate("Materials/detalhes_devolucao.html", req);
	}

	public static void devolver(Long idMaterial) {
		Material mat = Material.findById(idMaterial);
		mat.status = EstadoDoMaterial.DISPONIVEL;
		Requisicao req = Requisicao.find("material.id =?1 and status=?2", mat.id, EstadoDaRequisicao.ACEITO).first();
		req.status = EstadoDaRequisicao.CONCLUIDO;
		flash.success("Devolução efetuada com sucesso");
		mat.save();
		req.save();
		devolucao();
	}
	public static void mostrarFoto(Long id) {
		Material mat = Material.findById(id);
		renderBinary(mat.foto.get());
	}
	public static void encontrarMaterial(String pesquisa) {
		Material mat = Material.find("cod=?1", pesquisa).first();
		if(mat != null) {
			Requisicao req = Requisicao.find("material =?1 and status =?2", mat, EstadoDaRequisicao.ACEITO).first();
			if(req == null) {
				flash.error("material não emprestado ou sem pedido requisitado");
				renderTemplate("Materials/devolucao.html");
			}
				renderTemplate("Materials/devolucao.html", mat, req);
		}else {
			flash.error("Material não encontrado");
			renderTemplate("Materials/devolucao.html");
		}
	}
}
