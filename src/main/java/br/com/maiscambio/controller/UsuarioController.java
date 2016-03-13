package br.com.maiscambio.controller;

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
import br.com.maiscambio.model.entity.Estabelecimento;
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
		Estabelecimento estabelecimento = getEstabelecimentoFromRequest();
		Long pessoaId = estabelecimento != null ? estabelecimento.getPessoaId() : null;
		Long paiPessoaId = estabelecimento != null ? estabelecimento.getPai() != null ? estabelecimento.getPai().getPessoaId() : null : null;
		
		View view = view("full", "usuario", "Novo usuário");
		view.addObject("readonly", false);
		view.addObject("perfis", getUsuarioService().getPerfisAsStringList());
		view.addObject("status", getUsuarioService().getStatusAsStringList());
		view.addObject("showEstabelecimento", paiPessoaId == null);
		
		if(UsuarioService.hasPerfil(getUsuarioService().getFromRequest(getRequest()), Usuario.Perfil.ADMIN))
		{
			view.addObject("estabelecimentos", getEstabelecimentoService().findAllSortedAscByNomeFantasia());
		}
		else
		{
			view.addObject("estabelecimentos", getEstabelecimentoService().findAllSortedAscByNomeFantasia(pessoaId, pessoaId));
		}
		
		return view;
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{usuarioId}", method = RequestMethod.GET)
	@Autenticacao({ @Perfil({ Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA, Usuario.Perfil.ESTABELECIMENTO_USUARIO_LEITURA }), @Perfil(Usuario.Perfil.ESTABELECIMENTO_LEITURA) })
	public View edit(@PathVariable Long usuarioId)
	{
		Estabelecimento estabelecimento = getEstabelecimentoFromRequest();
		Long pessoaId = estabelecimento != null ? estabelecimento.getPessoaId() : null;
		Long paiPessoaId = estabelecimento != null ? estabelecimento.getPai() != null ? estabelecimento.getPai().getPessoaId() : null : null;
		
		Usuario usuario = getUsuarioService().findOne(usuarioId);
		
		if(usuario == null)
		{
			throw new HttpException(UsuarioService.EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		boolean canEdit = canEdit(usuario);
		
		if(!canEdit)
		{
			throw new HttpException(UsuarioService.EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		View view = view("full", "usuario", "Detalhes do usuário");
		view.addObject("usuario", usuario);
		view.addObject("readonly", !canEdit);
		view.addObject("perfis", getUsuarioService().getPerfisAsStringList());
		view.addObject("status", getUsuarioService().getStatusAsStringList());
		view.addObject("showEstabelecimento", paiPessoaId == null);
		
		if(UsuarioService.hasPerfil(getUsuarioService().getFromRequest(getRequest()), Usuario.Perfil.ADMIN))
		{
			view.addObject("estabelecimentos", getEstabelecimentoService().findAllSortedAscByNomeFantasia());
		}
		else
		{
			view.addObject("estabelecimentos", getEstabelecimentoService().findAllSortedAscByNomeFantasia(pessoaId, pessoaId));
		}
		
		return view;
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Autenticacao({ @Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_LEITURA), @Perfil(Usuario.Perfil.ESTABELECIMENTO_LEITURA) })
	public View list()
	{
		return view("full", "usuario_grid", "Buscar usuário");
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/search", method = { RequestMethod.GET })
	@Autenticacao({ @Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_LEITURA), @Perfil(Usuario.Perfil.ESTABELECIMENTO_LEITURA) })
	public @ResponseBody Page<Map<String, Object>> findAll()
	{
		Estabelecimento estabelecimento = getEstabelecimentoFromRequest();
		Long pessoaId = estabelecimento != null ? estabelecimento.getPessoaId() : null;
		
		RepositoryQuery<Usuario> repositoryQuery = getRepositoryQuery(Usuario.class);
		
		if(UsuarioService.hasPerfil(getUsuarioService().getFromRequest(getRequest()), Usuario.Perfil.ADMIN))
		{
			return getUsuarioService().findAll(repositoryQuery.toCustomRepositorySelector(), repositoryQuery.toSpecification(), repositoryQuery.toPageable());
		}
		else
		{
			return getUsuarioService().findAll(repositoryQuery.toCustomRepositorySelector(), repositoryQuery.toSpecification(), repositoryQuery.toPageable(), pessoaId, pessoaId);
		}
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
		validateAndFixForSaving(usuario);
		
		return getUsuarioService().saveAsInsertByEstabelecimento(usuario, true);
	}
	
	@Transactional
	@RequestMapping(value = "/{usuarioId}", method = { RequestMethod.POST, RequestMethod.PUT })
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA))
	public @ResponseBody Usuario save(@PathVariable Long usuarioId, Usuario usuario)
	{
		validateAndFixForSaving(usuario);
		
		Usuario foundUsuario = getUsuarioService().findOne(usuarioId);
		
		if(foundUsuario == null)
		{
			throw new HttpException(UsuarioService.EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(!canEdit(foundUsuario))
		{
			throw new HttpException(UsuarioService.EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		boolean isUpdatingSenha = (usuario.getSenha() != null);
		
		if(!isUpdatingSenha)
		{
			usuario.setSenha(foundUsuario.getSenha());
			
			if(UsuarioService.hasPerfil(getUsuarioService().getFromRequest(getRequest()), Usuario.Perfil.ADMIN))
			{
				getUsuarioService().saveAsUpdate(usuarioId, usuario, false);
			}
			else
			{
				getUsuarioService().saveAsUpdateByEstabelecimento(usuarioId, usuario, false);
			}
			
			if(getUsuarioService().getFromRequest(getRequest()).getUsuarioId().equals(usuarioId))
			{
				getAutenticacaoService().loginEncrypted(getRequest(), usuario.getApelido(), usuario.getSenha(), false, false);
			}
		}
		else
		{
			if(UsuarioService.hasPerfil(getUsuarioService().getFromRequest(getRequest()), Usuario.Perfil.ADMIN))
			{
				getUsuarioService().saveAsUpdate(usuarioId, usuario, true);
			}
			else
			{
				getUsuarioService().saveAsUpdateByEstabelecimento(usuarioId, usuario, true);
			}
			
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
	
	@Transactional(readOnly = true)
	private void validateAndFixForSaving(Usuario usuario)
	{
		Estabelecimento estabelecimento = getEstabelecimentoFromRequest();
		Long pessoaId = estabelecimento != null ? estabelecimento.getPessoaId() : null;
		
		if(estabelecimento != null)
		{
			if(estabelecimento.getPai() != null)
			{
				usuario.setPessoa(estabelecimento);
			}
			
			List<Estabelecimento> availableEstabelecimentos = getEstabelecimentoService().findAllSortedAscByNomeFantasia(pessoaId, pessoaId);
			
			if(usuario.getPessoa() != null)
			{
				boolean contains = false;
				
				if(availableEstabelecimentos != null)
				{
					for(Estabelecimento availableEstabelecimento : availableEstabelecimentos)
					{
						if(usuario.getPessoa().getPessoaId().equals(availableEstabelecimento.getPessoaId()))
						{
							contains = true;
							
							break;
						}
					}
				}
				
				if(!contains)
				{
					throw new HttpException(UsuarioService.EXCEPTION_USUARIO_PESSOA_INVALID, HttpStatus.NOT_ACCEPTABLE);
				}
			}
			
		}
	}
	
	private boolean canEdit(Usuario usuario)
	{
		if(UsuarioService.hasPerfil(getUsuarioService().getFromRequest(getRequest()), Usuario.Perfil.ADMIN))
		{
			return true;
		}
		else
		{
			if(usuario.getPessoa() != null)
			{
				return canEdit(usuario.getPessoa().getPessoaId(), Usuario.Perfil.ESTABELECIMENTO_USUARIO_ESCRITA);
			}
			else
			{
				return false;
			}
		}
	}
}
