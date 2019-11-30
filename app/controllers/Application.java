package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import com.mchange.v2.cfg.PropertiesConfigSource.Parse;

import models.*;
public class Application extends Controller {

	public static void index() {
		Long emprestimos = Requisicao.emprestadosMensal;
		Long impressos = Impressao.impressos;
		Long users = Usuario.count();
		Usuario user = Usuario.find("matricula =?1 ", session.get("usuario.matricula")).first();
		if(user != null) {
		int msg = user.mensagensRecebidas.size();
		render(emprestimos, impressos,users, msg);
		}else {
			render(emprestimos, impressos,users);
		}
	}

}