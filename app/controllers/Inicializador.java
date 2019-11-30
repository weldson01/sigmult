package controllers;

import enums.TipoUsuario;
import models.Usuario;
import models.Cota;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Inicializador extends Job {
	public void doJob() {
		if (Usuario.count() == 0) {
			// Fixtures.loadModels("test-data.yml")

			Usuario cosme = new Usuario();
			cosme.nome = "Cosme Gabriel";
			cosme.email = "cosmefelix11@gmail.com";
			cosme.matricula = "cosmefelix11@gmail.com";
			cosme.url_foto_75x100 = "https://vignette.wikia.nocookie.net/ospadrinhosdetimmy/images/2/2f/FairlyOddParents_Cosmo_01.jpg/revision/latest?cb=20110911060118&path-prefix=pt-br";
			cosme.setSenha("123");
			cosme.tipoUsuario = TipoUsuario.ADMIN;
			cosme.tipoVinculo = "Administrador";
			cosme.save();
			Usuario weldson = new Usuario();
			weldson.nome = "Weldson Carlos";
			weldson.email = "w@w.com";
			weldson.matricula = "w@w.com";
			weldson.url_foto_75x100 = "https://image.shutterstock.com/image-vector/vector-banner-eye-providence-allseeing-260nw-1198474069.jpg";
			weldson.setSenha("q1w2e3");
			weldson.tipoUsuario = TipoUsuario.ADMIN;
			cosme.tipoVinculo = "Administrador";
			weldson.save();
			//teste de cota
			//Cota cota = new Cota();
			//cota.nome = ("Cota teste");
			//cota.acessivelUser.add(weldson);
			//cota.acessivelUser.add(cosme);
			//cota.quantidadeDisponivel = 100;
			//cota.quantidadeTotal = 100;
			//cota.adm = weldson;
			//cota.save();
		}
		
		

	}

}
