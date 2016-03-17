package br.com.maiscambio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.Perfil;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.service.UsuarioService;
import br.com.maiscambio.util.View;

@Controller
@RequestMapping("/dashboard")
public class DashboardController extends BaseController
{
	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_DASHBOARD_LEITURA))
	public View index(@RequestParam String selectedFormattedDate)
	{
		Usuario usuario = getUsuarioService().getFromRequest(getRequest());
		
		View view = view("full", "dashboard", "Dashboard");
		
		if(UsuarioService.hasPerfil(usuario, Usuario.Perfil.ADMIN))
		{
			view.addObject("estabelecimentos", getEstabelecimentoService().findByUsuarioStatusWherePaiIsNullAndUsuariosSizeIsOne(Usuario.Status.INATIVO));
		}
		else
		{
			view.addObject("moedas", getTaxaService().getMoedasAsStringList());
		}
		
		return view;
	}
}
