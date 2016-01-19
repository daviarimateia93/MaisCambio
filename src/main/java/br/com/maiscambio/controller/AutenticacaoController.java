package br.com.maiscambio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.maiscambio.WebMvcConfig;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.service.AutenticacaoService;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.View;

@Controller
@RequestMapping("/autenticacao")
public class AutenticacaoController extends BaseController
{
	public static final String PARAMETER_URL = "url";
	
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		String url = getAutenticacaoUrl();
		
		try
		{
			authenticate();
		}
		catch(HttpException httpException)
		{
			return view("full", "autenticacao", "Autenticação");
		}
		
		return url != null ? redirect(url) : redirect(WebMvcConfig.getEnvironment().getProperty("posAutenticacaoPage"));
	}
	
	@RequestMapping(value = "/sessionRenew", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void sessionRenew()
	{
	
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public View login(Usuario usuario, @RequestParam(required = false) boolean doNotRedirect)
	{
		String url = getAutenticacaoUrl();
		
		getAutenticacaoService().login(getRequest(), usuario.getApelido(), usuario.getSenha(), true, true);
		
		if(doNotRedirect)
		{
			throw new HttpException(HttpStatus.OK);
		}
		
		return url != null ? redirect(url) : redirect(WebMvcConfig.getEnvironment().getProperty("posAutenticacaoPage"));
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public View logout()
	{
		if(AutenticacaoService.isLoggedIn(getRequest()))
		{
			getAutenticacaoService().logout(getRequest());
		}
		
		return redirect("/");
	}
}
