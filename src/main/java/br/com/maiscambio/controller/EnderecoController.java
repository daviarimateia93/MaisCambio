package br.com.maiscambio.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.model.entity.Endereco;

@Controller
@RequestMapping("/endereco")
public class EnderecoController extends BaseController
{
	@Transactional(readOnly = true)
	@RequestMapping(value = "/cep/{cep}", method = RequestMethod.GET)
	@Autenticacao()
	public @ResponseBody Endereco findByCep(@PathVariable String cep) throws MalformedURLException, IOException
	{
		return getEnderecoService().findByCep(cep);
	}
}
