package br.com.maiscambio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.maiscambio.WebMvcConfig;
import me.gerenciar.util.View;

@Controller
@RequestMapping("/")
public class InitialController extends BaseController
{
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		View view = view("full", "home", "Home");
		view.addObject("moedas", getTaxaService().getMoedasAsStringList());
		view.addObject("estados", getEstadoService().findByPaisIdSortedAscByNome(WebMvcConfig.getInstance().getEnvironment().getProperty("paisId")));
		
		return view;
	}
}