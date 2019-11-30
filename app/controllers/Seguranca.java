package controllers;

import play.mvc.Before;
import play.mvc.Controller;

public class Seguranca extends Controller {
	@Before
	public static void verificar() {
		if (session.contains("usuario.matricula") == false || session.contains("usuario.email") == false) {
			Logins.form();
		}

	}
}
