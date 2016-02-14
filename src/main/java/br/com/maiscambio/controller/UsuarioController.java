package br.com.maiscambio.controller;

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
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.service.UsuarioService;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.RepositoryQuery;
import br.com.maiscambio.util.View;

@Controller
@RequestMapping("/usuario")
public class UsuarioController extends BaseController
{
	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	@Autenticacao({ @Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA), @Perfil(Usuario.Perfil.ESTABELECIMENTO_LEITURA) })
	public View index()
	{
		View view = view("full", "usuario", "Novo usuário");
		view.addObject("perfis", getUsuarioService().getPerfisAsStringList());
		view.addObject("status", getUsuarioService().getStatusAsStringList());
		view.addObject("estabelecimentos", getEstabelecimentoService().findAllSortedAscByNomeFantasia());
		
		return view;
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{usuarioId}", method = RequestMethod.GET)
	@Autenticacao({ @Perfil({ Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA, Usuario.Perfil.ESTABELECIMENTO_USUARIO_LEITURA }), @Perfil(Usuario.Perfil.ESTABELECIMENTO_LEITURA) })
	public View edit(@PathVariable Long usuarioId)
	{
		Usuario usuario = getUsuarioService().findOne(usuarioId);
		
		if(usuario == null)
		{
			throw new HttpException(UsuarioService.EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		View view = view("full", "usuario", "Detalhes do usuário");
		view.addObject("usuario", usuario);
		view.addObject("perfis", getUsuarioService().getPerfisAsStringList());
		view.addObject("status", getUsuarioService().getStatusAsStringList());
		view.addObject("estabelecimentos", getEstabelecimentoService().findAllSortedAscByNomeFantasia());
		
		return view;
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Autenticacao({ @Perfil(Usuario.Perfil.ADMIN), @Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_LEITURA), @Perfil(Usuario.Perfil.ESTABELECIMENTO_LEITURA) })
	public View list()
	{
		return view("full", "usuario_grid", "Buscar usuário");
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/search", method = { RequestMethod.GET })
	@Autenticacao({ @Perfil(Usuario.Perfil.ADMIN), @Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_LEITURA), @Perfil(Usuario.Perfil.ESTABELECIMENTO_LEITURA) })
	public @ResponseBody Page<Map<String, Object>> findAll()
	{
		RepositoryQuery<Usuario> repositoryQuery = getRepositoryQuery(Usuario.class);
		
		return getUsuarioService().findAll(repositoryQuery.toCustomRepositorySelector(), repositoryQuery.toSpecification(), repositoryQuery.toPageable());
	}
	
	@Transactional
	@RequestMapping(value = "/{usuarioId}/changeSenha", method = { RequestMethod.POST, RequestMethod.PUT })
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA))
	public @ResponseBody Usuario changeSenha(@PathVariable Long usuarioId, @RequestParam String newSenha)
	{
		Usuario usuario = getUsuarioService().changeSenha(usuarioId, newSenha);
		
		if(getUsuarioService().getFromRequest(getRequest()).getUsuarioId().equals(usuarioId))
		{
			getAutenticacaoService().loginEncrypted(getRequest(), usuario.getApelido(), usuario.getSenha(), false, false);
		}
		
		return usuario;
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA))
	public @ResponseBody Usuario save(Usuario usuario)
	{
		return getUsuarioService().saveAsInsertByEstabelecimento(usuario, true);
	}
	
	@Transactional
	@RequestMapping(value = "/{usuarioId}", method = { RequestMethod.POST, RequestMethod.PUT })
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA))
	public @ResponseBody Usuario save(@PathVariable Long usuarioId, Usuario usuario)
	{
		authenticateByUsuarioId(getUsuarioService().getFromRequest(getRequest()).getUsuarioId());
		
		Usuario foundUsuario = getUsuarioService().findOne(usuarioId);
		
		if(foundUsuario == null)
		{
			throw new HttpException(UsuarioService.EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		boolean isUpdatingSenha = (usuario.getSenha() != null);
		
		if(!isUpdatingSenha)
		{
			usuario.setSenha(foundUsuario.getSenha());
			
			getUsuarioService().saveAsUpdateByEstabelecimento(usuarioId, usuario, false);
			
			if(getUsuarioService().getFromRequest(getRequest()).getUsuarioId().equals(usuarioId))
			{
				getAutenticacaoService().loginEncrypted(getRequest(), usuario.getApelido(), usuario.getSenha(), false, false);
			}
		}
		else
		{
			getUsuarioService().saveAsUpdateByEstabelecimento(usuarioId, usuario, true);
			
			if(getUsuarioService().getFromRequest(getRequest()).getUsuarioId().equals(usuarioId))
			{
				getAutenticacaoService().login(getRequest(), usuario.getApelido(), usuario.getSenha(), false, false);
			}
		}
		
		return usuario;
	}
	
	@Transactional
	@RequestMapping(value = "/{usuarioId}/delete", method = { RequestMethod.POST, RequestMethod.DELETE })
	@ResponseStatus(value = HttpStatus.OK)
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA))
	public void delete(@PathVariable Long usuarioId)
	{
		if(getUsuarioService().getFromRequest(getRequest()).getUsuarioId().equals(usuarioId))
		{
			throw new HttpException(UsuarioService.EXCEPTION_USUARIO_MUST_NOT_BE_THE_SAME_FROM_THE_LOGGED_ONE, HttpStatus.NOT_ACCEPTABLE);
		}
		else
		{
			getUsuarioService().delete(usuarioId);
		}
	}
}
