package br.com.maiscambio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.maiscambio.WebMvcConfig;
import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Usuario.Status;
import br.com.maiscambio.util.View;

@Controller
@RequestMapping("/estabelecimento")
public class EstabelecimentoController extends BaseController
{
	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	public View index(@RequestParam(required = false) boolean success)
	{
		View view = view("full", "estabelecimento", "Novo cadastro");
		view.addObject("success", success);
		view.addObject("estados", getEstadoService().findByPaisIdSortedAscByNome(WebMvcConfig.getEnvironment().getProperty("paisId")));
		
		return view;
	}
	
	@Transactional
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody Estabelecimento saveEstabelecimentoAsMatriz(Estabelecimento estabelecimento)
	{
		estabelecimento.setPai(null);
		estabelecimento.setData(null);
		
		if(estabelecimento.getUsuarios() != null)
		{
			if(!estabelecimento.getUsuarios().isEmpty())
			{
				estabelecimento.getUsuarios().get(0).setStatus(Status.INATIVO);
				estabelecimento.getUsuarios().get(0).setPessoa(estabelecimento);
			}
		}
		
		return getEstabelecimentoService().saveAsInsert(estabelecimento);
	}
}
