package br.com.maiscambio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.maiscambio.WebMvcConfig;
import br.com.maiscambio.util.View;

@Controller
@RequestMapping("/estabelecimento")
public class EstabelecimentoController extends BaseController
{
	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		View view = view("full", "estabelecimento", "Novo cadastro");
		view.addObject("estados", getEstadoService().findByPaisIdSortedAscByNome(WebMvcConfig.getEnvironment().getProperty("paisId")));
		
		return view;
	}
}
