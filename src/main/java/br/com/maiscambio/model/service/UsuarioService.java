package br.com.maiscambio.model.service;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Pessoa;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.repository.UsuarioRepository;
import br.com.maiscambio.model.repository.custom.CustomRepositorySelector;
import br.com.maiscambio.util.Constants;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.ReflectionHelper;
import br.com.maiscambio.util.StringHelper;

@Service
public class UsuarioService implements GlobalBaseEntityService<Usuario, Long>
{
	public static final String EXCEPTION_USUARIO_NOT_FOUND = "USUARIO_NOT_FOUND";
	public static final String EXCEPTION_USUARIO_OLD_SENHA_DOES_NOT_MATCH = "EXCEPTION_USUARIO_OLD_SENHA_DOES_NOT_MATCH";
	public static final String EXCEPTION_USUARIO_MUST_NOT_BE_NULL = "USUARIO_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_USUARIO_USUARIO_ID_MUST_BE_NULL = "USUARIO_USUARIO_ID_MUST_BE_NULL";
	public static final String EXCEPTION_USUARIO_USUARIO_ID_MUST_NOT_BE_NULL = "USUARIO_USUARIO_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_USUARIO_APELIDO_IS_ALREADY_IN_USE = "USUARIO_APELIDO_IS_ALREADY_IN_USE";
	public static final String EXCEPTION_USUARIO_APELIDO_MUST_NOT_BE_EMPTY = "USUARIO_APELIDO_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_USUARIO_APELIDO_MUST_NOT_BE_BIGGER_THAN_45_CHARACTERS = "USUARIO_APELIDO_MUST_NOT_BE_BIGGER_THAN_45_CHARACTERS";
	public static final String EXCEPTION_USUARIO_APELIDO_MUST_CONTAINS_ONLY_LETTERS_NUMBERS_UNDERLINES_DASHES_AND_POINTS = "USUARIO_APELIDO_MUST_CONTAINS_ONLY_LETTERS_NUMBERS_UNDERLINES_DASHES_AND_POINTS";
	public static final String EXCEPTION_USUARIO_SENHA_MUST_NOT_BE_EMPTY = "USUARIO_SENHA_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_USUARIO_SENHA_MUST_NOT_BE_BIGGER_THAN_128_CHARACTERS = "USUARIO_SENHA_MUST_NOT_BE_BIGGER_THAN_128_CHARACTERS";
	public static final String EXCEPTION_USUARIO_STATUS_MUST_NOT_BE_NULL = "USUARIO_STATUS_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_USUARIO_PESSOA_MUST_NOT_BE_NULL = "USUARIO_PESSOA_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_USUARIO_PESSOA_PESSOA_ID_MUST_NOT_BE_NULL = "USUARIO_PESSOA_PESSOA_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_USUARIO_PESSOA_NOT_FOUND = "USUARIO_PESSOA_NOT_FOUND";
	public static final String EXCEPTION_USUARIO_MUST_NOT_BE_THE_SAME_FROM_THE_LOGGED_ONE = "USUARIO_MUST_NOT_BE_THE_SAME_FROM_THE_LOGGED_ONE";
	public static final String EXCEPTION_USUARIO_MUST_NOT_CONTAINS_DUPLICATED_PERFIS = "USUARIO_MUST_NOT_CONTAINS_DUPLICATED_PERFIS";
	public static final String EXCEPTION_USUARIO_PESSOA_MUST_BE_ESTABELECIMENTO = "USUARIO_PESSOA_MUST_BE_ESTABELECIMENTO";
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	@Override
	@Transactional(readOnly = true)
	public Usuario findOne(Long usuarioId)
	{
		return usuarioRepository.findOne(usuarioId);
	}
	
	@Transactional(readOnly = true)
	public Long count()
	{
		return usuarioRepository.count();
	}
	
	@Transactional(readOnly = true)
	public Page<Usuario> findAll(Specification<Usuario> specification, Pageable pageable)
	{
		return usuarioRepository.findAll(Specifications.where(specification), pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<Map<String, Object>> findAll(CustomRepositorySelector<Usuario> customRepositorySelector, Specification<Usuario> specification, Pageable pageable)
	{
		return usuarioRepository.findAll(customRepositorySelector, Specifications.where(specification), pageable);
	}
	
	@Transactional(readOnly = true)
	public Usuario findByApelido(String apelido)
	{
		return usuarioRepository.findByApelido(apelido);
	}
	
	@Transactional
	public Usuario saveAsInsert(Usuario usuario, boolean encrypt)
	{
		validateAsInsert(usuario);
		
		return save(usuario, encrypt);
	}
	
	@Transactional
	public Usuario saveAsInsertByEstabelecimento(Usuario usuario, boolean encrypt)
	{
		validateAsInsertByEstabelecimento(usuario);
		
		return save(usuario, encrypt);
	}
	
	@Transactional
	public Usuario saveAsUpdate(Usuario usuario, boolean encrypt)
	{
		validateAsUpdate(usuario);
		
		return save(usuario, encrypt);
	}
	
	@Transactional
	public Usuario saveAsUpdateByEstabelecimento(Usuario usuario, boolean encrypt)
	{
		validateAsUpdateByEstabelecimento(usuario);
		
		return save(usuario, encrypt);
	}
	
	@Transactional
	public Usuario saveAsUpdate(Long usuarioId, Usuario usuario, boolean encrypt)
	{
		if(usuarioRepository.findOne(usuarioId) != null)
		{
			usuario.setUsuarioId(usuarioId);
			
			return saveAsUpdate(usuario, encrypt);
		}
		else
		{
			throw new HttpException(EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	public Usuario saveAsUpdateByEstabelecimento(Long usuarioId, Usuario usuario, boolean encrypt)
	{
		if(usuarioRepository.findOne(usuarioId) != null)
		{
			usuario.setUsuarioId(usuarioId);
			
			return saveAsUpdateByEstabelecimento(usuario, encrypt);
		}
		else
		{
			throw new HttpException(EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	private Usuario save(Usuario usuario, boolean encrypt)
	{
		return usuarioRepository.save(encrypt ? encrypt(usuario) : usuario);
	}
	
	@Transactional
	public void delete(Long usuarioId)
	{
		if(findOne(usuarioId) != null)
		{
			usuarioRepository.delete(usuarioId);
		}
	}
	
	public static boolean hasPerfil(Usuario usuario, Usuario.Perfil perfil)
	{
		// null safe for jsp :-)
		if(usuario != null)
		{
			for(Usuario.Perfil usuarioPerfil : usuario.getPerfis())
			{
				if(usuarioPerfil == perfil)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean hasPerfil(HttpServletRequest request, String perfil)
	{
		return hasPerfil(request, Usuario.Perfil.valueOf(perfil));
	}
	
	public static boolean hasPerfil(Usuario usuario, String perfil)
	{
		return hasPerfil(usuario, Usuario.Perfil.valueOf(perfil));
	}
	
	public static boolean hasPerfil(HttpServletRequest request, Usuario.Perfil perfil)
	{
		Usuario usuario = getLoggedIn(request);
		
		return usuario != null ? hasPerfil(usuario, perfil) : false;
	}
	
	public boolean hasPerfis(Usuario usuario, Usuario.Perfil... perfis)
	{
		for(Usuario.Perfil usuarioPerfil : perfis)
		{
			if(!hasPerfil(usuario, usuarioPerfil))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isInPerfis(Usuario usuario, Usuario.Perfil... perfis)
	{
		for(Usuario.Perfil usuarioPerfil : perfis)
		{
			for(Usuario.Perfil perfil : usuario.getPerfis())
			{
				if(usuarioPerfil == perfil)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static Usuario getLoggedIn(HttpServletRequest request)
	{
		if(AutenticacaoService.isLoggedIn(request))
		{
			return (Usuario) request.getSession().getAttribute(AutenticacaoService.SESSION_ATTRIBUTE_NAME_USUARIO);
		}
		else
		{
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public Usuario getFromRequest(HttpServletRequest request)
	{
		Usuario loggedInUsuario = getLoggedIn(request);
		
		if(loggedInUsuario != null)
		{
			return loggedInUsuario;
		}
		else
		{
			String[] userCredentials = null;
			try
			{
				userCredentials = AutenticacaoService.getUserCredentialsFromRequest(request);
			}
			catch(HttpException httpException)
			{
			
			}
			
			if(userCredentials != null)
			{
				return findByApelido(userCredentials[1]);
			}
			else
			{
				return null;
			}
		}
	}
	
	public String encryptSenha(String decryptedSenha)
	{
		return StringHelper.encryptSHA512AndHex(decryptedSenha);
	}
	
	public Usuario encrypt(Usuario decryptedUsuario)
	{
		decryptedUsuario.setSenha(encryptSenha(decryptedUsuario.getSenha()));
		
		return decryptedUsuario;
	}
	
	@Transactional
	public Usuario changeSenha(Long usuarioId, String oldSenha, String newSenha)
	{
		Usuario usuario = findOne(usuarioId);
		
		if(usuario == null)
		{
			throw new HttpException(EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(usuario.getSenha().equals(encryptSenha(oldSenha)))
		{
			validateSenha(newSenha);
			
			usuario.setSenha(newSenha);
			
			return saveAsUpdate(usuario, true);
		}
		else
		{
			throw new HttpException(EXCEPTION_USUARIO_OLD_SENHA_DOES_NOT_MATCH, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	public Usuario changeSenha(Long usuarioId, String newSenha)
	{
		Usuario usuario = findOne(usuarioId);
		
		if(usuario == null)
		{
			throw new HttpException(EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		usuario.setSenha(newSenha);
		
		return saveAsUpdate(usuario, true);
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsert(Usuario usuario)
	{
		validateAsInsert(usuario, false);
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsertIgnoringPessoa(Usuario usuario)
	{
		validateAsInsert(usuario, true);
	}
	
	@Transactional(readOnly = true)
	private void validateAsInsert(Usuario usuario, boolean ignorePessoa)
	{
		if(ignorePessoa)
		{
			validateIgnoringIdAndPessoa(usuario);
		}
		else
		{
			validateIgnoringId(usuario);
		}
		
		if(usuario.getUsuarioId() != null)
		{
			throw new HttpException(EXCEPTION_USUARIO_USUARIO_ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(findByApelido(usuario.getApelido()) != null)
		{
			throw new HttpException(EXCEPTION_USUARIO_APELIDO_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsertByEstabelecimento(Usuario usuario)
	{
		validateAsInsert(usuario);
		validateIgnoringIdByEstabelecimento(usuario);
	}
	
	@Transactional(readOnly = true)
	public void validateIgnoringIdByEstabelecimento(Usuario usuario)
	{
		validateIgnoringId(usuario);
		
		if(estabelecimentoService.findOne(usuario.getPessoa().getPessoaId()) == null)
		{
			throw new HttpException(EXCEPTION_USUARIO_PESSOA_MUST_BE_ESTABELECIMENTO, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateAsUpdate(Usuario usuario)
	{
		validateAsUpdate(usuario, false);
	}
	
	@Transactional(readOnly = true)
	public void validateAsUpdateIgnoringPessoa(Usuario usuario)
	{
		validateAsUpdate(usuario, true);
	}
	
	@Transactional(readOnly = true)
	private void validateAsUpdate(Usuario usuario, boolean ignorePessoa)
	{
		if(ignorePessoa)
		{
			validateIgnoringIdAndPessoa(usuario);
		}
		else
		{
			validateIgnoringId(usuario);
		}
		
		if(usuario.getUsuarioId() == null)
		{
			throw new HttpException(EXCEPTION_USUARIO_USUARIO_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(findOne(usuario.getUsuarioId()) == null)
		{
			throw new HttpException(EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Usuario foundUsuarioByApelido = findByApelido(usuario.getApelido());
		
		if(foundUsuarioByApelido != null)
		{
			if(!foundUsuarioByApelido.getUsuarioId().equals(usuario.getUsuarioId()))
			{
				throw new HttpException(EXCEPTION_USUARIO_APELIDO_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
	}
	
	@Transactional(readOnly = true)
	public void validateAsUpdateByEstabelecimento(Usuario usuario)
	{
		validateAsUpdate(usuario);
		validateIgnoringIdByEstabelecimento(usuario);
	}
	
	@Transactional(readOnly = true)
	public void validateIgnoringId(Usuario usuario)
	{
		validateIgnoringIdAndPessoa(usuario);
		
		if(usuario.getPessoa() != null)
		{
			if(usuario.getPessoa().getPessoaId() == null)
			{
				throw new HttpException(EXCEPTION_USUARIO_PESSOA_PESSOA_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
			}
			
			Pessoa foundPessoa = pessoaService.findOne(usuario.getPessoa().getPessoaId());
			
			if(foundPessoa == null)
			{
				throw new HttpException(EXCEPTION_USUARIO_PESSOA_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
			}
			
			usuario.setPessoa(foundPessoa);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateIgnoringIdAndPessoa(Usuario usuario)
	{
		if(usuario == null)
		{
			throw new HttpException(EXCEPTION_USUARIO_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(usuario.getStatus() == null)
		{
			throw new HttpException(EXCEPTION_USUARIO_STATUS_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringHelper.isBlank(usuario.getApelido()))
		{
			throw new HttpException(EXCEPTION_USUARIO_APELIDO_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(usuario.getApelido().length() > 45)
		{
			throw new HttpException(EXCEPTION_USUARIO_APELIDO_MUST_NOT_BE_BIGGER_THAN_45_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(!usuario.getApelido().matches(Constants.TEXT_PATTERN_APELIDO))
		{
			throw new HttpException(EXCEPTION_USUARIO_APELIDO_MUST_CONTAINS_ONLY_LETTERS_NUMBERS_UNDERLINES_DASHES_AND_POINTS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		validateSenha(usuario.getSenha());
		
		if(usuario.getPerfis() != null)
		{
			for(int i = 0; i < usuario.getPerfis().size(); i++)
			{
				for(int j = 0; j < usuario.getPerfis().size(); j++)
				{
					if(j != i)
					{
						if(usuario.getPerfis().get(i) == usuario.getPerfis().get(j))
						{
							throw new HttpException(EXCEPTION_USUARIO_MUST_NOT_CONTAINS_DUPLICATED_PERFIS, HttpStatus.NOT_ACCEPTABLE);
						}
					}
				}
			}
		}
	}
	
	private void validateSenha(String senha)
	{
		if(StringHelper.isBlank(senha))
		{
			throw new HttpException(EXCEPTION_USUARIO_SENHA_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(senha.length() > 128)
		{
			throw new HttpException(EXCEPTION_USUARIO_SENHA_MUST_NOT_BE_BIGGER_THAN_128_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	public List<String> getPerfisAsStringList()
	{
		List<String> perfis = ReflectionHelper.fieldsToNames(ReflectionHelper.getEnumFieldsConstants(Usuario.Perfil.class));
		
		return perfis;
	}
	
	public List<String> getStatusAsStringList()
	{
		List<String> status = ReflectionHelper.fieldsToNames(ReflectionHelper.getEnumFieldsConstants(Usuario.Status.class));
		
		return status;
	}
	
	@Transactional(readOnly = true)
	private Specification<Usuario> pessoaIdEquals(final Long pessoaId)
	{
		return new Specification<Usuario>()
		{
			@Override
			public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
			{
				return criteriaBuilder.equal(root.get("pessoa").get("pessoaId"), pessoaId);
			}
		};
	}
	
	@Transactional(readOnly = true)
	private Specification<Usuario> childrenBelongsToPessoaId(final Long pessoaId)
	{
		Pessoa pessoa = pessoaService.findOne(pessoaId);
		
		if(pessoa instanceof Estabelecimento)
		{
			Estabelecimento pai = null;
			
			Specifications<Usuario> pessoaIdSpecifications = Specifications.where(pessoaIdEquals(pessoaId));
			
			while((pai = pai == null ? ((Estabelecimento) pessoa).getPai() : pai.getPai()) != null)
			{
				pessoaIdSpecifications = pessoaIdSpecifications.or(pessoaIdEquals(pai.getPessoaId()));
			}
			
			return pessoaIdSpecifications;
		}
		else
		{
			return Specifications.where(pessoaIdEquals(pessoaId));
		}
	}
	
	@Transactional(readOnly = true)
	private Specification<Usuario> usuarioIdEquals(final Long usuarioId)
	{
		return new Specification<Usuario>()
		{
			@Override
			public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
			{
				return criteriaBuilder.equal(root.get("usuarioId"), usuarioId);
			}
		};
	}
}