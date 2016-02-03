package br.com.maiscambio.model.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Pessoa;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.repository.EstabelecimentoRepository;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.StringHelper;

@Service
public class EstabelecimentoService extends PessoaService implements GlobalBaseEntityService<Estabelecimento, Long>
{
	public static final String EXCEPTION_ESTABELECIMENTO_MUST_NOT_BE_NULL = "ESTABELECIMENTO_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ESTABELECIMENTO_RAZAO_SOCIAL_MUST_BE_NULL = "ESTABELECIMENTO_RAZAO_SOCIAL_MUST_BE_NULL";
	public static final String EXCEPTION_ESTABELECIMENTO_RAZAO_SOCIAL_MUST_NOT_BE_EMPTY = "ESTABELECIMENTO_RAZAO_SOCIAL_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_ESTABELECIMENTO_RAZAO_SOCIAL_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS = "ESTABELECIMENTO_RAZAO_SOCIAL_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS";
	public static final String EXCEPTION_ESTABELECIMENTO_NOME_FANTASIA_MUST_NOT_BE_EMPTY = "ESTABELECIMENTO_NOME_FANTASIA_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_ESTABELECIMENTO_NOME_FANTASIA_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS = "ESTABELECIMENTO_NOME_FANTASIA_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS";
	public static final String EXCEPTION_ESTABELECIMENTO_MUST_HAVE_AT_LEAST_1_USUARIO = "ESTABELECIMENTO_MUST_HAVE_AT_LEAST_1_USUARIO";
	public static final String EXCEPTION_ESTABELECIMENTO_EMAIL_MUST_NOT_BE_EMPTY = "ESTABELECIMENTO_EMAIL_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_ESTABELECIMENTO_NOT_FOUND = "ESTABELECIMENTO_NOT_FOUND";
	public static final String EXCEPTION_USUARIO_DOES_NOT_HAS_PERMISSION = "USUARIO_DOES_NOT_HAS_PERMISSION";
	public static final String EXCEPTION_ESTABELECIMENTO_ALREADY_EXISTS = "ESTABELECIMENTO_ALREADY_EXISTS";
	public static final String EXCEPTION_ESTABELECIMENTO_IS_ALREADY_IN_USE = "ESTABELECIMENTO_IS_ALREADY_IN_USE";
	public static final String EXCEPTION_ESTABELECIMENTO_USUARIO_DOES_NOT_BELONG_TO_YOU = "ESTABELECIMENTO_USUARIO_DOES_NOT_BELONG_TO_YOU";
	public static final String EXCEPTION_ESTABELECIMENTO_PAI_PESSOA_ID_MUST_NOT_BE_NULL = "ESTABELECIMENTO_PAI_PESSOA_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ESTABELECIMENTO_PAI_NOT_FOUND = "ESTABELECIMENTO_PAI_NOT_FOUND";
	public static final String EXCEPTION_ESTABELECIMENTO_PAI_PAI_MUST_BE_NULL = "ESTABELECIMENTO_PAI_PAI_MUST_BE_NULL";
	public static final String EXCEPTION_ESTABELECIMENTO_NOME_FANTASIA_MUST_BE_THE_SAME_FROM_PAI = "ESTABELECIMENTO_NOME_FANTASIA_MUST_BE_THE_SAME_FROM_PAI";
	public static final String EXCEPTION_ESTABELECIMENTO_NOT_FOUND_OR_NOT_ABLE_TO_DO_THIS = "ESTABELECIMENTO_NOT_FOUND_OR_NOT_ABLE_TO_DO_THIS";
	
	@Autowired
	private EstabelecimentoRepository estabelecimentoRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	@Transactional(readOnly = true)
	public Estabelecimento findOne(Long pessoaId)
	{
		return estabelecimentoRepository.findOne(pessoaId);
	}
	
	@Transactional(readOnly = true)
	public List<Estabelecimento> findAll()
	{
		return estabelecimentoRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<Estabelecimento> findAllSortedAscByNomeFantasia()
	{
		return estabelecimentoRepository.findAll(new Sort(new Order(Direction.ASC, "nomeFantasia")));
	}
	
	@Transactional(readOnly = true)
	public Estabelecimento findByCnpjOrCpfOrIdEstrangeiro(String cnpj, String cpf, String idEstrangeiro)
	{
		return estabelecimentoRepository.findByCnpjOrCpfOrIdEstrangeiro(cnpj, cpf, idEstrangeiro);
	}
	
	@Transactional(readOnly = true)
	public Estabelecimento findByUsuarioId(Long usuarioId)
	{
		return estabelecimentoRepository.findByUsuarioId(usuarioId);
	}
	
	@Transactional(readOnly = true)
	public List<Estabelecimento> findByUsuarioStatusWherePaiIsNullAndUsuariosSizeIsOne(Usuario.Status usuarioStatus)
	{
		return estabelecimentoRepository.findByUsuarioStatusWherePaiIsNullAndUsuariosSizeIsOne(usuarioStatus);
	}
	
	@Transactional(readOnly = true)
	public Estabelecimento findByPessoaIdAndUsuarioStatusWherePaiIsNullAndUsuariosSizeIsOne(Long pessoaId, Usuario.Status usuarioStatus)
	{
		return estabelecimentoRepository.findByPessoaIdAndUsuarioStatusWherePaiIsNullAndUsuariosSizeIsOne(pessoaId, usuarioStatus);
	}
	
	@Transactional
	public Estabelecimento saveAsInsert(Estabelecimento estabelecimento)
	{
		validateAsInsert(estabelecimento);
		
		return save(estabelecimento);
	}
	
	@Transactional
	public Estabelecimento saveAsUpdate(Estabelecimento estabelecimento)
	{
		validateAsUpdate(estabelecimento);
		
		return save(estabelecimento);
	}
	
	@Transactional
	public Estabelecimento saveAsUpdate(Long pessoaId, Estabelecimento estabelecimento)
	{
		if(estabelecimentoRepository.findOne(pessoaId) != null)
		{
			estabelecimento.setPessoaId(pessoaId);
			
			return saveAsUpdate(estabelecimento);
		}
		else
		{
			throw new HttpException(EXCEPTION_ESTABELECIMENTO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	private Estabelecimento save(Estabelecimento estabelecimento)
	{
		return estabelecimentoRepository.save(estabelecimento);
	}
	
	@Transactional
	public Estabelecimento activate(Long pessoaId)
	{
		Estabelecimento foundEstabelecimento = findByPessoaIdAndUsuarioStatusWherePaiIsNullAndUsuariosSizeIsOne(pessoaId, Usuario.Status.INATIVO);
		
		if(foundEstabelecimento == null)
		{
			throw new HttpException(EXCEPTION_ESTABELECIMENTO_NOT_FOUND_OR_NOT_ABLE_TO_DO_THIS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(foundEstabelecimento.getUsuarios() != null)
		{
			for(Usuario usuario : foundEstabelecimento.getUsuarios())
			{
				usuario.setStatus(Usuario.Status.ATIVO);
			}
		}
		
		return saveAsUpdate(foundEstabelecimento);
	}
	
	@Transactional(readOnly = true)
	public Estabelecimento getFromRequest(HttpServletRequest request)
	{
		Pessoa pessoa = super.getFromRequest(request);
		
		return pessoa != null ? estabelecimentoRepository.findOne(pessoa.getPessoaId()) : null;
	}
	
	@Transactional(readOnly = true)
	public Estabelecimento getPaiFromRequest(HttpServletRequest request)
	{
		Estabelecimento estabelecimento = getFromRequest(request);
		
		if(estabelecimento.getPai() != null)
		{
			estabelecimento = estabelecimento.getPai();
		}
		
		return estabelecimento;
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsert(Estabelecimento estabelecimento)
	{
		validateIgnoringId(estabelecimento);
		
		super.validateAsInsert(estabelecimento, true);
		
		Estabelecimento foundEstabelecimento = findByCnpjOrCpfOrIdEstrangeiro(estabelecimento.getCnpj(), estabelecimento.getCpf(), estabelecimento.getIdEstrangeiro());
		
		if(foundEstabelecimento != null)
		{
			throw new HttpException(EXCEPTION_ESTABELECIMENTO_ALREADY_EXISTS, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateAsUpdate(Estabelecimento estabelecimento)
	{
		validateIgnoringId(estabelecimento);
		
		super.validateAsUpdate(estabelecimento, true);
		
		Estabelecimento foundEstabelecimento = findByCnpjOrCpfOrIdEstrangeiro(estabelecimento.getCnpj(), estabelecimento.getCpf(), estabelecimento.getIdEstrangeiro());
		
		if(foundEstabelecimento != null)
		{
			if(!estabelecimento.getPessoaId().equals(foundEstabelecimento.getPessoaId()))
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(estabelecimento.getUsuarios() != null)
		{
			if(!estabelecimento.getUsuarios().isEmpty())
			{
				if(foundEstabelecimento.getUsuarios() != null)
				{
					if(!foundEstabelecimento.getUsuarios().isEmpty())
					{
						if(!foundEstabelecimento.getUsuarios().get(0).getUsuarioId().equals(estabelecimento.getUsuarios().get(0).getUsuarioId()))
						{
							throw new HttpException(EXCEPTION_ESTABELECIMENTO_USUARIO_DOES_NOT_BELONG_TO_YOU, HttpStatus.NOT_ACCEPTABLE);
						}
					}
				}
			}
		}
	}
	
	@Transactional(readOnly = true)
	public void validateIgnoringId(Estabelecimento estabelecimento)
	{
		if(estabelecimento == null)
		{
			throw new HttpException(EXCEPTION_ESTABELECIMENTO_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		super.validateIgnoringId(estabelecimento);
		
		if(estabelecimento.getCnpj() == null && estabelecimento.getRazaoSocial() != null)
		{
			throw new HttpException(EXCEPTION_ESTABELECIMENTO_RAZAO_SOCIAL_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(estabelecimento.getRazaoSocial() != null)
		{
			if(StringHelper.isBlank(estabelecimento.getRazaoSocial()))
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_RAZAO_SOCIAL_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
			}
			
			if(estabelecimento.getRazaoSocial().length() > 120)
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_RAZAO_SOCIAL_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(estabelecimento.getPai() == null)
		{
			if(StringHelper.isBlank(estabelecimento.getNomeFantasia()))
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_NOME_FANTASIA_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
			}
			
			if(estabelecimento.getNomeFantasia().length() > 120)
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_NOME_FANTASIA_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
			}
			
			if(estabelecimento.getUsuarios() == null)
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_MUST_HAVE_AT_LEAST_1_USUARIO, HttpStatus.NOT_ACCEPTABLE);
			}
			else
			{
				if(estabelecimento.getUsuarios().isEmpty())
				{
					throw new HttpException(EXCEPTION_ESTABELECIMENTO_MUST_HAVE_AT_LEAST_1_USUARIO, HttpStatus.NOT_ACCEPTABLE);
				}
			}
		}
		else
		{
			if(estabelecimento.getPai().getPessoaId() == null)
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_PAI_PESSOA_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
			}
			
			Estabelecimento foundEstabelecimento = findOne(estabelecimento.getPai().getPessoaId());
			
			if(foundEstabelecimento == null)
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_PAI_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
			}
			
			estabelecimento.setPai(foundEstabelecimento);
			
			if(estabelecimento.getPai().getPai() != null)
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_PAI_PAI_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
			}
			
			if(!estabelecimento.getPai().getNomeFantasia().equals(estabelecimento.getNomeFantasia()))
			{
				throw new HttpException(EXCEPTION_ESTABELECIMENTO_NOME_FANTASIA_MUST_BE_THE_SAME_FROM_PAI, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(estabelecimento.getUsuarios() != null)
		{
			for(Usuario usuario : estabelecimento.getUsuarios())
			{
				usuario.setSenha(usuarioService.encryptSenha(usuario.getSenha()));
			}
		}
	}
}
