package br.com.maiscambio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.Perfil;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.util.View;

@Controller
@RequestMapping("/dashboard")
public class DashboardController extends BaseController
{
	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_DASHBOARD_LEITURA))
	public View index()
	{
		return view("full", "dashboard", "Dashboard");
	}
}
