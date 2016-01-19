package br.com.maiscambio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.maiscambio.util.View;

@Controller
@RequestMapping("/")
public class InitialController extends BaseController
{
	@RequestMapping(method = RequestMethod.GET)
	public View index()
	{
		return view("full", "home", "Home");
	}
}