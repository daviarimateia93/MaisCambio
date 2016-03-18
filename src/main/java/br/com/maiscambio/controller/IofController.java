package br.com.maiscambio.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.Perfil;
import br.com.maiscambio.model.entity.Iof;
import br.com.maiscambio.model.entity.Usuario;

@Controller
@RequestMapping("/iof")
public class IofController extends BaseController
{
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	@Autenticacao(@Perfil(Usuario.Perfil.ADMIN))
	public @ResponseBody Iof save(Iof iof)
	{
		iof.setData(new Date());
		
		return getIofService().saveAsInsert(iof);
	}
}
