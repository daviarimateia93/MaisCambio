package br.com.maiscambio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.Perfil;
import br.com.maiscambio.model.entity.Taxa;
import br.com.maiscambio.model.entity.Taxa.Finalidade;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.util.Constants;
import br.com.maiscambio.util.View;

@Controller
@RequestMapping("/taxa")
public class TaxaController extends BaseController
{
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{finalidade}", method = RequestMethod.GET)
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_TAXA_LEITURA))
	public View index(@PathVariable Finalidade finalidade)
	{
		View view = view("full", "taxa", "Taxas de " + (finalidade == Finalidade.VENDA ? "venda" : finalidade == Finalidade.COMPRA ? "compra" : Constants.TEXT_EMPTY));
		view.addObject("finalidade", finalidade);
		view.addObject("moedas", getTaxaService().getMoedasAsStringList());
		
		return view;
	}
	
	@Transactional
	@RequestMapping(value = "/{finalidade}", method = RequestMethod.POST)
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_TAXA_ESCRITA))
	public @ResponseBody Taxa save(@PathVariable Finalidade finalidade)
	{
		return null;
	}
}
