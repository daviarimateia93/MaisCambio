package br.com.maiscambio.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.model.entity.Cidade;

@Controller
@RequestMapping("/cidade")
public class CidadeController extends BaseController
{
	@Transactional(readOnly = true)
	@RequestMapping(method = { RequestMethod.GET })
	@Autenticacao()
	public @ResponseBody List<Cidade> findByEstadoIdAndPaisId(@RequestParam String estadoId, @RequestParam String paisId)
	{
		return getCidadeService().findByEstadoIdAndPaisIdSortedAscByNome(estadoId, paisId);
	}
}
