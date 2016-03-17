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

@Controller
@RequestMapping("/taxa")
public class TaxaController extends BaseController
{
	@Transactional
	@RequestMapping(value = "/{finalidade}", method = RequestMethod.POST)
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_TAXA_ESCRITA))
	public @ResponseBody Taxa save(@PathVariable Finalidade finalidade)
	{
		return null;
	}
}
