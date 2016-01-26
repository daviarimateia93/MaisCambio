package br.com.maiscambio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.maiscambio.WebMvcConfig;
import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.entity.Usuario.Status;
import br.com.maiscambio.util.StringHelper;
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
	public @ResponseBody Estabelecimento saveEstabelecimentoAsMatriz(Estabelecimento estabelecimento) throws IOException, InterruptedException
	{
		estabelecimento.setPai(null);
		estabelecimento.setData(null);
		
		if(estabelecimento.getUsuarios() != null)
		{
			for(Usuario usuario : estabelecimento.getUsuarios())
			{
				List<Usuario.Perfil> perfis = new ArrayList<>();
				perfis.add(Usuario.Perfil.ESTABELECIMENTO_DASHBOARD_LEITURA);
				perfis.add(Usuario.Perfil.ESTABELECIMENTO_ESCRITA);
				perfis.add(Usuario.Perfil.ESTABELECIMENTO_LEITURA);
				perfis.add(Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA);
				perfis.add(Usuario.Perfil.ESTABELECIMENTO_USUARIO_LEITURA);
				
				usuario.setStatus(Status.INATIVO);
				usuario.setPessoa(estabelecimento);
				usuario.setPerfis(perfis);
			}
			
			for(int i = 1; i < estabelecimento.getUsuarios().size(); i++)
			{
				estabelecimento.getUsuarios().remove(i);
			}
		}
		
		estabelecimento = getEstabelecimentoService().saveAsInsert(estabelecimento);
		
		sendNewEmail(estabelecimento, "contato@maiscambio.com.br");
		sendWelcomeEmail(estabelecimento, estabelecimento.getEmail());
		
		return estabelecimento;
	}
	
	private void sendNewEmail(Estabelecimento estabelecimento, String email) throws IOException, InterruptedException
	{
		Map<String, String> variables = new HashMap<>();
		variables.put("#___NOME_FANTASIA___#", estabelecimento.getNomeFantasia());
		variables.put("#___CNPJ_CPF_ID_ESTRANGEIRO___#", estabelecimento.getCnpj() != null ? StringHelper.format(estabelecimento.getCnpj(), "##.###.###/####-##") : estabelecimento.getCpf() != null ? StringHelper.format(estabelecimento.getCpf(), "###.###.###-##") : estabelecimento.getIdEstrangeiro());
		variables.put("#___TELEFONE___#", estabelecimento.getTelefone1().length() == 12 ? StringHelper.format(estabelecimento.getTelefone1(), "+## (##) ####-####") : StringHelper.format(estabelecimento.getTelefone1(), "+## (##) #####-####"));
		variables.put("#___EMAIL___#", estabelecimento.getEmail());
		variables.put("#___USUARIO_APELIDO___#", estabelecimento.getUsuarios().get(0).getApelido());
		
		getEmailService().sendAsynchronously(email, "MaisCâmbio - Bem vindo", loadEmailTemplateWithVariables("email_estabelecimento_new.html", variables));
	}
	
	private void sendWelcomeEmail(Estabelecimento estabelecimento, String email) throws IOException, InterruptedException
	{
		Map<String, String> variables = new HashMap<>();
		variables.put("#___NOME_FANTASIA___#", estabelecimento.getNomeFantasia());
		variables.put("#___CNPJ_CPF_ID_ESTRANGEIRO___#", estabelecimento.getCnpj() != null ? StringHelper.format(estabelecimento.getCnpj(), "##.###.###/####-##") : estabelecimento.getCpf() != null ? StringHelper.format(estabelecimento.getCpf(), "###.###.###-##") : estabelecimento.getIdEstrangeiro());
		variables.put("#___USUARIO_APELIDO___#", estabelecimento.getUsuarios().get(0).getApelido());
		
		getEmailService().sendAsynchronously(email, "MaisCâmbio - Bem vindo", loadEmailTemplateWithVariables("email_estabelecimento_welcome.html", variables));
	}
}
