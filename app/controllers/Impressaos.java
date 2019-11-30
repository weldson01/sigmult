package controllers;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import play.db.jpa.Blob;
import org.h2.engine.User;

import enums.EstadoDaImpressao;
import models.Arquivo;
import models.Cota;
import models.Impressao;
import models.Requisicao;
import models.Usuario;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.Controller;

public class Impressaos extends Controller {
	public static void form() {
		//pegando todas as cotas disponiveis do usuário
		
		Usuario user = Usuario.findById(Long.parseLong(session.get("usuario.id")));	
		List<Cota> lista = user.cotasDisponiveis;
		render(lista);
	}
	public static void salvar(Impressao imp, Long cotid) {
		if(imp.dataDeRecebimento == null) {
			flash.error("Data de recebimento inválida");
			form();
			
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar data = Calendar.getInstance();
		Cota cota = Cota.findById(cotid);
		Usuario user = Usuario.findById(Long.parseLong(session.get("usuario.id")));
		imp.cota = cota;
		imp.dataDeSolicitacao = data;
		imp.dataDeSolicitacaoString = sdf.format(data.getTime());
		imp.dataDeRecebimentoString = sdf.format(imp.dataDeRecebimento.getTime());
	//	if(imp.cota.quantidadeDisponivel <= imp.arquivos.quantidadeCopias) {
		imp.arquivo.save();
		imp.status = EstadoDaImpressao.AGUARDANDO_APROVACAO;
		imp.usuarioImp = user;
		imp.save();
		listarind();
	//	}else {
	//		flash.error("Cotas insuficientes!");
	//		form();
	//	}
	}
	public static void detalhes(Long id) {
		Impressao imp = Impressao.findById(id);
		render(imp);
	}
	public static void status(Long id, Boolean status) {
		Impressao imp = Impressao.findById(id);
		if(imp.cota.quantidadeDisponivel <= imp.arquivo.quantidadeCopias) {
		if(status == true) {
			//impressão aceita
			//descontar da cota
			//soma ao montante de impressões 
			Impressao.impressos += imp.arquivo.quantidadeCopias;
			
		}else if(status == false) {
			//impressão recusada
			//mostrar mensagem de recusa para o usuario solicitador informando o motivo da recusa
			
		}else if(status == null) {
			//status inválido
			flash.error("Status inválido");
			}
		}else {
			flash.error("Cotas insuficientes!");
		}
	}
	public static void listargeral() {
		List<Impressao> lista = Impressao.findAll();
		renderTemplate("Impressaos/listaprov.html", lista);
	}
	public static void listarind() {
		Usuario user = Usuario.findById(Long.parseLong(session.get("usuario.id")));
		List<Impressao> lista = user.impressoes;
		renderTemplate("Impressaos/listar.html",lista);
	}
	public static void baixarArquivo(Long id) {
		Impressao imp = Impressao.findById(id);
		if(imp != null) {
			//erro aqui
		renderBinary(imp.arquivo.arquivo.getFile());
		}
	}
	public static void nomeArquivo(Long id) {
		Impressao imp = Impressao.findById(id);
		renderText(imp.arquivo.arquivo.getFile().getName());
	}
	public static void deletar(Long id) {
		Impressao imp = Impressao.findById(id);
		imp.delete();
		listarind();
	}
}
