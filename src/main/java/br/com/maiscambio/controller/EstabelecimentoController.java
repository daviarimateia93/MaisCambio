package br.com.maiscambio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.Perfil;
import br.com.maiscambio.WebMvcConfig;
import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.entity.Usuario.Status;
import br.com.maiscambio.model.service.EstabelecimentoService;
import br.com.maiscambio.model.service.UsuarioService;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.RepositoryQuery;
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
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{pessoaId}", method = RequestMethod.GET)
	public View edit(@PathVariable Long pessoaId, @RequestParam(required = false) boolean success, @RequestParam(required = false) boolean activateSuccess)
	{
		Usuario usuario = getUsuarioService().getFromRequest(getRequest());
		Estabelecimento estabelecimento = getEstabelecimentoFromRequest();
		Estabelecimento foundEstabelecimento = getEstabelecimentoService().findOne(pessoaId);
		
		if(foundEstabelecimento == null)
		{
			throw new HttpException(EstabelecimentoService.EXCEPTION_ESTABELECIMENTO_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		
		boolean canEdit = usuario != null ? UsuarioService.hasPerfil(usuario, Usuario.Perfil.ESTABELECIMENTO_ESCRITA) : false;
		canEdit = canEdit ? estabelecimento != null ? foundEstabelecimento.getPessoaId().equals(estabelecimento.getPessoaId()) || foundEstabelecimento.getPai().getPessoaId().equals(estabelecimento.getPessoaId()) : UsuarioService.hasPerfil(usuario, Usuario.Perfil.ADMIN) : false;
		
		View view = view("full", "estabelecimento", "Detalhes");
		view.addObject("estabelecimentos", getEstabelecimentoService().findAllSortedAscByNomeFantasia());
		view.addObject("estados", getEstadoService().findByPaisIdSortedAscByNome(WebMvcConfig.getEnvironment().getProperty("paisId")));
		view.addObject("cidades", getCidadeService().findByEstadoIdAndPaisIdSortedAscByNome(foundEstabelecimento.getEndereco().getCidade().getEstado().getEstadoId(), foundEstabelecimento.getEndereco().getCidade().getEstado().getPais().getPaisId()));
		view.addObject("estabelecimento", foundEstabelecimento);
		view.addObject("success", success);
		view.addObject("activateSuccess", activateSuccess);
		view.addObject("readonly", !canEdit);
		view.addObject("estabelecimentoActivated", getEstabelecimentoService().findByPessoaIdAndUsuarioStatusWherePaiIsNullAndUsuariosSizeIsOne(pessoaId, Usuario.Status.INATIVO) == null);
		
		return view;
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_LEITURA))
	public View list()
	{
		return view("full", "estabelecimento_grid", "Buscar");
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/search", method = { RequestMethod.GET })
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_LEITURA))
	public @ResponseBody Page<Map<String, Object>> findAll()
	{
		Estabelecimento estabelecimento = getEstabelecimentoFromRequest();
		Long pessoaId = estabelecimento != null ? estabelecimento.getPessoaId() : null;
		
		RepositoryQuery<Estabelecimento> repositoryQuery = getRepositoryQuery(Estabelecimento.class);
		
		if(UsuarioService.hasPerfil(getUsuarioService().getFromRequest(getRequest()), Usuario.Perfil.ADMIN))
		{
			return getEstabelecimentoService().findAll(repositoryQuery.toCustomRepositorySelector(), repositoryQuery.toSpecification(), repositoryQuery.toPageable());
		}
		else
		{
			return getEstabelecimentoService().findAll(repositoryQuery.toCustomRepositorySelector(), repositoryQuery.toSpecification(), repositoryQuery.toPageable(), pessoaId, pessoaId);
		}
	}
	
	@Transactional
	@RequestMapping(value = "/{pessoaId}/activate", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
	@Autenticacao({ @Perfil(Usuario.Perfil.ADMIN), @Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA), @Perfil(Usuario.Perfil.ESTABELECIMENTO_ESCRITA) })
	public View activate(@PathVariable Long pessoaId) throws IOException, InterruptedException
	{
		Estabelecimento estabelecimento = getEstabelecimentoService().activate(pessoaId);
		
		sendActivatedEmail(estabelecimento, estabelecimento.getEmail());
		
		return redirect("/estabelecimento/" + pessoaId + "?activateSuccess=true");
	}
	
	@Transactional
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
	public @ResponseBody Estabelecimento save(Estabelecimento estabelecimento) throws IOException, InterruptedException
	{
		if(getUsuarioService().getFromRequest(getRequest()) == null)
		{
			estabelecimento.setPai(null);
		}
		else if(getEstabelecimentoFromRequest() != null)
		{
			if(getEstabelecimentoFromRequest().getPai() == null)
			{
				estabelecimento.setPai(getEstabelecimentoFromRequest());
				estabelecimento.setNomeFantasia(getEstabelecimentoFromRequest().getNomeFantasia());
			}
			else
			{
				estabelecimento.setPai(getEstabelecimentoFromRequest().getPai());
				estabelecimento.setNomeFantasia(getEstabelecimentoFromRequest().getPai().getNomeFantasia());
			}
		}
		
		estabelecimento.setData(null);
		
		if(estabelecimento.getUsuarios() == null)
		{
			throw new HttpException(EstabelecimentoService.EXCEPTION_ESTABELECIMENTO_MUST_HAVE_AT_LEAST_1_USUARIO, HttpStatus.NOT_ACCEPTABLE);
		}
		else
		{
			if(estabelecimento.getUsuarios().isEmpty())
			{
				throw new HttpException(EstabelecimentoService.EXCEPTION_ESTABELECIMENTO_MUST_HAVE_AT_LEAST_1_USUARIO, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		for(int i = 1; i < estabelecimento.getUsuarios().size(); i++)
		{
			estabelecimento.getUsuarios().remove(i);
		}
		
		List<Usuario.Perfil> perfis = new ArrayList<>();
		perfis.add(Usuario.Perfil.ESTABELECIMENTO_DASHBOARD_LEITURA);
		perfis.add(Usuario.Perfil.ESTABELECIMENTO_ESCRITA);
		perfis.add(Usuario.Perfil.ESTABELECIMENTO_LEITURA);
		perfis.add(Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA);
		perfis.add(Usuario.Perfil.ESTABELECIMENTO_USUARIO_LEITURA);
		
		Usuario usuario = estabelecimento.getUsuarios().get(0);
		usuario.setStatus(Status.INATIVO);
		usuario.setPessoa(estabelecimento);
		usuario.setPerfis(perfis);
		
		estabelecimento = getEstabelecimentoService().saveAsInsert(estabelecimento);
		getUsuarioService().saveAsInsert(usuario, true);
		
		sendNewEmail(estabelecimento, "contato@maiscambio.com.br");
		sendWelcomeEmail(estabelecimento, estabelecimento.getEmail());
		
		return estabelecimento;
	}
	
	@Transactional
	@RequestMapping(value = "/{pessoaId}", method = { RequestMethod.POST, RequestMethod.PUT })
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_ESCRITA))
	public @ResponseBody Estabelecimento save(@PathVariable Long pessoaId, Estabelecimento estabelecimento)
	{
		Estabelecimento foundEstabelecimento = getEstabelecimentoService().findOne(pessoaId);
		
		fixEstabelecimentoForSaving(estabelecimento);
		
		if(estabelecimento.getEndereco() != null)
		{
			if(foundEstabelecimento != null)
			{
				estabelecimento.getEndereco().setEnderecoId(foundEstabelecimento.getEndereco().getEnderecoId());
			}
			
			if(estabelecimento.getEndereco().getCidade() != null)
			{
				if(estabelecimento.getEndereco().getCidade().getCidadeId() != null)
				{
					estabelecimento.getEndereco().setCidade(getCidadeService().findOne(estabelecimento.getEndereco().getCidade().getCidadeId()));
				}
			}
		}
		
		return getEstabelecimentoService().saveAsUpdate(pessoaId, estabelecimento);
	}
	
	@Transactional
	@RequestMapping(value = "/{pessoaId}/delete", method = { RequestMethod.POST, RequestMethod.DELETE })
	@ResponseStatus(value = HttpStatus.OK)
	@Autenticacao({ @Perfil(Usuario.Perfil.ADMIN), @Perfil(Usuario.Perfil.ESTABELECIMENTO_ESCRITA) })
	public void delete(@PathVariable Long pessoaId)
	{
		Estabelecimento foundEstabelecimento = getEstabelecimentoService().findByUsuarioId(pessoaId);
		Estabelecimento estabelecimento = getEstabelecimentoFromRequest();
		
		if(foundEstabelecimento != null && estabelecimento != null)
		{
			if(foundEstabelecimento.getPessoaId().equals(estabelecimento.getPessoaId()))
			{
				throw new HttpException(EstabelecimentoService.EXCEPTION_ESTABELECIMENTO_MUST_NOT_BE_THE_SAME_FROM_THE_LOGGED_ONE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		getEstabelecimentoService().delete(pessoaId);
	}
	
	private void sendNewEmail(Estabelecimento estabelecimento, String email) throws IOException, InterruptedException
	{
		Map<String, String> variables = new HashMap<>();
		variables.put("#___PESSOA_ID___#", String.valueOf(estabelecimento.getPessoaId()));
		variables.put("#___NOME_FANTASIA___#", estabelecimento.getNomeFantasia());
		variables.put("#___CNPJ_CPF_ID_ESTRANGEIRO___#", estabelecimento.getCnpj() != null ? StringHelper.format(estabelecimento.getCnpj(), "##.###.###/####-##") : estabelecimento.getCpf() != null ? StringHelper.format(estabelecimento.getCpf(), "###.###.###-##") : estabelecimento.getIdEstrangeiro());
		variables.put("#___TELEFONE___#", estabelecimento.getTelefone1().length() == 12 ? StringHelper.format(estabelecimento.getTelefone1(), "+## (##) ####-####") : StringHelper.format(estabelecimento.getTelefone1(), "+## (##) #####-####"));
		variables.put("#___EMAIL___#", estabelecimento.getEmail());
		variables.put("#___USUARIO_APELIDO___#", estabelecimento.getUsuarios().get(0).getApelido());
		
		getEmailService().sendAsynchronously(email, "MaisCâmbio - Novo parceiro", loadEmailTemplateWithVariables("email_estabelecimento_new.html", variables));
	}
	
	private void sendWelcomeEmail(Estabelecimento estabelecimento, String email) throws IOException, InterruptedException
	{
		Map<String, String> variables = new HashMap<>();
		variables.put("#___NOME_FANTASIA___#", estabelecimento.getNomeFantasia());
		variables.put("#___CNPJ_CPF_ID_ESTRANGEIRO___#", estabelecimento.getCnpj() != null ? StringHelper.format(estabelecimento.getCnpj(), "##.###.###/####-##") : estabelecimento.getCpf() != null ? StringHelper.format(estabelecimento.getCpf(), "###.###.###-##") : estabelecimento.getIdEstrangeiro());
		variables.put("#___USUARIO_APELIDO___#", estabelecimento.getUsuarios().get(0).getApelido());
		
		getEmailService().sendAsynchronously(email, "MaisCâmbio - Bem vindo", loadEmailTemplateWithVariables("email_estabelecimento_welcome.html", variables));
	}
	
	private void sendActivatedEmail(Estabelecimento estabelecimento, String email) throws IOException, InterruptedException
	{
		Map<String, String> variables = new HashMap<>();
		variables.put("#___PESSOA_ID___#", String.valueOf(estabelecimento.getPessoaId()));
		variables.put("#___NOME_FANTASIA___#", estabelecimento.getNomeFantasia());
		variables.put("#___CNPJ_CPF_ID_ESTRANGEIRO___#", estabelecimento.getCnpj() != null ? StringHelper.format(estabelecimento.getCnpj(), "##.###.###/####-##") : estabelecimento.getCpf() != null ? StringHelper.format(estabelecimento.getCpf(), "###.###.###-##") : estabelecimento.getIdEstrangeiro());
		variables.put("#___USUARIO_APELIDO___#", estabelecimento.getUsuarios().get(0).getApelido());
		
		getEmailService().sendAsynchronously(email, "MaisCâmbio - Perfil ativado", loadEmailTemplateWithVariables("email_estabelecimento_activated.html", variables));
	}
	
	private void fixEstabelecimentoForSaving(Estabelecimento estabelecimento)
	{
		if(getUsuarioService().getFromRequest(getRequest()) == null)
		{
			estabelecimento.setPai(null);
		}
		else if(getEstabelecimentoFromRequest() != null)
		{
			if(getEstabelecimentoFromRequest().getPai() == null)
			{
				estabelecimento.setPai(getEstabelecimentoFromRequest());
				estabelecimento.setNomeFantasia(getEstabelecimentoFromRequest().getNomeFantasia());
			}
			else
			{
				estabelecimento.setPai(getEstabelecimentoFromRequest().getPai());
				estabelecimento.setNomeFantasia(getEstabelecimentoFromRequest().getPai().getNomeFantasia());
			}
		}
		else if(estabelecimento.getPessoaId() != null && estabelecimento.getPai() != null)
		{
			estabelecimento.setNomeFantasia(getEstabelecimentoService().findOne(estabelecimento.getPai().getPessoaId()).getNomeFantasia());
		}
		
		estabelecimento.setData(null);
	}
}
