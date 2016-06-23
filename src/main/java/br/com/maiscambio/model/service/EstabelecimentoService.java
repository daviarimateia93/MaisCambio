package br.com.maiscambio.model.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Pessoa;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.repository.EstabelecimentoRepository;
import me.gerenciar.util.CustomRepositorySelector;
import me.gerenciar.util.DateHelper;
import me.gerenciar.util.GlobalBaseEntityService;
import me.gerenciar.util.HttpException;
import me.gerenciar.util.StringHelper;

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
	public static final String EXCEPTION_ESTABELECIMENTO_PAI_PESSOA_ID_MUST_NOT_BE_NULL = "ESTABELECIMENTO_PAI_PESSOA_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ESTABELECIMENTO_PAI_NOT_FOUND = "ESTABELECIMENTO_PAI_NOT_FOUND";
	public static final String EXCEPTION_ESTABELECIMENTO_PAI_PAI_MUST_BE_NULL = "ESTABELECIMENTO_PAI_PAI_MUST_BE_NULL";
	public static final String EXCEPTION_ESTABELECIMENTO_NOME_FANTASIA_MUST_BE_THE_SAME_FROM_PAI = "ESTABELECIMENTO_NOME_FANTASIA_MUST_BE_THE_SAME_FROM_PAI";
	public static final String EXCEPTION_ESTABELECIMENTO_NOT_FOUND_OR_NOT_ABLE_TO_DO_THIS = "ESTABELECIMENTO_NOT_FOUND_OR_NOT_ABLE_TO_DO_THIS";
	public static final String EXCEPTION_ESTABELECIMENTO_MUST_NOT_BE_THE_SAME_FROM_THE_LOGGED_ONE = "ESTABELECIMENTO_MUST_NOT_BE_THE_SAME_FROM_THE_LOGGED_ONE";
	public static final String EXCEPTION_ESTABELECIMENTO_TIME_ZONE_MUST_NOT_BE_EMPTY = "ESTABELECIMENTO_TIME_ZONE_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_ESTABELECIMENTO_TIME_ZONE_IS_INVALID = "ESTABELECIMENTO_TIME_ZONE_IS_INVALID";
	
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
	public List<Estabelecimento> findAllSortedAscByNomeFantasia(Long pessoaId, Long paiPessoaId)
	{
		return estabelecimentoRepository.findAll(pessoaIdEqualsOrPaiPessoaIdEquals(pessoaId, paiPessoaId), new Sort(new Order(Direction.ASC, "nomeFantasia")));
	}
	
	@Transactional(readOnly = true)
	public Page<Map<String, Object>> findAll(CustomRepositorySelector<Estabelecimento> customRepositorySelector, Specification<Estabelecimento> specification, Pageable pageable)
	{
		return estabelecimentoRepository.findAll(customRepositorySelector, Specifications.where(specification), pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<Map<String, Object>> findAll(CustomRepositorySelector<Estabelecimento> customRepositorySelector, Specification<Estabelecimento> specification, Pageable pageable, Long pessoaId, Long paiPessoaId)
	{
		return estabelecimentoRepository.findAll(customRepositorySelector, Specifications.where(specification).and(pessoaIdEqualsOrPaiPessoaIdEquals(pessoaId, paiPessoaId)), pageable);
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
	public void delete(Long pessoaId)
	{
		if(findOne(pessoaId) != null)
		{
			estabelecimentoRepository.delete(pessoaId);
		}
	}
	
	@Transactional
	public Estabelecimento activate(Long pessoaId)
	{
		Estabelecimento foundEstabelecimento = findByPessoaIdAndUsuarioStatusWherePaiIsNullAndUsuariosSizeIsOne(pessoaId, Usuario.Status.INATIVO);
		
		if(foundEstabelecimento == null)
		{
			throw new HttpException(EXCEPTION_ESTABELECIMENTO_NOT_FOUND_OR_NOT_ABLE_TO_DO_THIS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Usuario usuario = foundEstabelecimento.getUsuarios().get(0);
		usuario.setStatus(Usuario.Status.ATIVO);
		
		usuarioService.saveAsUpdate(usuario, false);
		
		return foundEstabelecimento;
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
		
		if(StringHelper.isBlank(estabelecimento.getTimeZone()))
		{
			throw new HttpException(EXCEPTION_ESTABELECIMENTO_TIME_ZONE_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(TimeZone.getTimeZone(estabelecimento.getTimeZone()) == null)
		{
			throw new HttpException(EXCEPTION_ESTABELECIMENTO_TIME_ZONE_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	private Specification<Estabelecimento> pessoaIdEquals(final Long pessoaId)
	{
		return new Specification<Estabelecimento>()
		{
			@Override
			public Predicate toPredicate(Root<Estabelecimento> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
			{
				return criteriaBuilder.equal(root.get("pessoaId"), pessoaId);
			}
		};
	}
	
	@Transactional(readOnly = true)
	private Specification<Estabelecimento> pessoaIdEqualsOrPaiPessoaIdEquals(final Long pessoaId, final Long paiPessoaId)
	{
		Specification<Estabelecimento> pessoaIdEqualsSpecification = pessoaIdEquals(pessoaId);
		Specification<Estabelecimento> paiPessoaIdEqualsSpecification = new Specification<Estabelecimento>()
		{
			@Override
			public Predicate toPredicate(Root<Estabelecimento> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
			{
				return criteriaBuilder.equal(root.get("pai").get("pessoaId"), paiPessoaId);
			}
		};
		
		return Specifications.where(pessoaIdEqualsSpecification).or(paiPessoaIdEqualsSpecification);
	}
	
	@Transactional(readOnly = true)
	public Date now(Long pessoaId)
	{
		return pessoaId != null ? now(findOne(pessoaId)) : now((Estabelecimento) null);
	}
	
	public static Date now(Estabelecimento estabelecimento)
	{
		Date now = new Date();
		
		return estabelecimento != null ? DateHelper.setTimeZone(now, estabelecimento.getTimeZone()) : now;
	}
}
