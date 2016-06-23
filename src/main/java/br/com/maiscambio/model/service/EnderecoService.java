package br.com.maiscambio.model.service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Endereco;
import br.com.maiscambio.model.entity.MetaLogradouro;
import br.com.maiscambio.model.entity.Pessoa;
import br.com.maiscambio.model.repository.EnderecoRepository;
import me.gerenciar.util.Constants;
import me.gerenciar.util.GlobalBaseEntityService;
import me.gerenciar.util.HttpException;
import me.gerenciar.util.StringHelper;

@Service
public class EnderecoService implements GlobalBaseEntityService<Endereco, Long>
{
	public static final String EXCEPTION_ENDERECO_NOT_FOUND = "ENDERECO_NOT_FOUND";
	public static final String EXCEPTION_ENDERECO_ALREADY_EXISTS = "ENDERECO_ALREADY_EXISTS";
	public static final String EXCEPTION_ENDERECO_CIDADE_MUST_NOT_BE_NULL = "ENDERECO_CIDADE_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ENDERECO_CIDADE_ID_MUST_NOT_BE_NULL = "ENDERECO_CIDADE_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ENDERECO_CIDADE_NOT_FOUND = "ENDERECO_CIDADE_NOT_FOUND";
	public static final String EXCEPTION_ENDERECO_CIDADE_ESTADO_MUST_NOT_BE_NULL = "ENDERECO_CIDADE_ESTADO_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ENDERECO_CIDADE_ESTADO_ID_MUST_NOT_BE_NULL = "ENDERECO_CIDADE_ESTADO_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ENDERECO_CIDADE_ESTADO_NOT_FOUND = "ENDERECO_CIDADE_ESTADO_NOT_FOUND";
	public static final String EXCEPTION_ENDERECO_CIDADE_ESTADO_PAIS_MUST_NOT_BE_NULL = "ENDERECO_CIDADE_ESTADO_PAIS_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ENDERECO_CIDADE_ESTADO_PAIS_ID_MUST_NOT_BE_NULL = "ENDERECO_CIDADE_ESTADO_PAIS_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ENDERECO_CIDADE_ESTADO_PAIS_NOT_FOUND = "ENDERECO_CIDADE_ESTADO_PAIS_NOT_FOUND";
	public static final String EXCEPTION_ENDERECO_LOGRADOURO_MUST_NOT_BE_EMPTY = "ENDERECO_LOGRADOURO_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_ENDERECO_LOGRADOURO_MUST_NOT_BE_BIGGER_THAN_125_CHARACTERS = "ENDERECO_LOGRADOURO_MUST_NOT_BE_BIGGER_THAN_125_CHARACTERS";
	public static final String EXCEPTION_ENDERECO_COMPLEMENTO_MUST_NOT_BE_EMPTY = "ENDERECO_COMPLEMENTO_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_ENDERECO_COMPLEMENTO_MUST_NOT_BE_BIGGER_THAN_45_CHARACTERS = "ENDERECO_COMPLEMENTO_MUST_NOT_BE_BIGGER_THAN_45_CHARACTERS";
	public static final String EXCEPTION_ENDERECO_NUMERO_MUST_NOT_BE_EMPTY = "ENDERECO_NUMERO_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_ENDERECO_NUMERO_MUST_NOT_BE_BIGGER_THAN_20_CHARACTERS = "ENDERECO_NUMERO_MUST_NOT_BE_BIGGER_THAN_20_CHARACTERS";
	public static final String EXCEPTION_ENDERECO_CEP_MUST_NOT_BE_NULL = "ENDERECO_CEP_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ENDERECO_CEP_MUST_BE_ONLY_NUMBERS_AND_8_CHARACTERS = "ENDERECO_CEP_MUST_BE_ONLY_NUMBERS_AND_8_CHARACTERS";
	public static final String EXCEPTION_ENDERECO_ENDERECO_ID_MUST_BE_NULL = "ENDERECO_ENDERECO_ID_MUST_BE_NULL";
	public static final String EXCEPTION_ENDERECO_ENDERECO_ID_MUST_NOT_BE_NULL = "ENDERECO_ENDERECO_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_ENDERECO_MUST_NOT_BE_NULL = "ENDERECO_MUST_NOT_BE_NULL";
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private MetaLogradouroService metaLogradouroService;
	
	@Override
	@Transactional(readOnly = true)
	public Endereco findOne(Long enderecoId)
	{
		return enderecoRepository.findOne(enderecoId);
	}
	
	@Transactional(readOnly = true)
	public Endereco findOne(Long pessoaId, Long enderecoId)
	{
		return enderecoRepository.findOne(Specifications.where(pessoaIdEquals(pessoaId)).and(enderecoIdEquals(enderecoId)));
	}
	
	@Transactional(readOnly = true)
	public Endereco findByCep(String cep)
	{
		MetaLogradouro metaLogradouro = metaLogradouroService.findOne(cep);
		
		if(metaLogradouro != null)
		{
			Endereco endereco = new Endereco();
			endereco.setCidade(metaLogradouro.getMetaBairro().getCidade());
			endereco.setCep(cep);
			endereco.setLogradouro(metaLogradouro.getTipoLogradouro() + (metaLogradouro.getTipoLogradouro() != null ? " " : "") + metaLogradouro.getLogradouro());
			endereco.setBairro(metaLogradouro.getMetaBairro().getNome());
			
			return endereco;
		}
		else
		{
			return null;
		}
	}
	
	@Transactional
	public Endereco saveAsInsert(Endereco endereco)
	{
		validateAsInsert(endereco);
		
		return save(endereco);
	}
	
	@Transactional
	public Endereco saveAsUpdate(Long pessoaId, Endereco endereco)
	{
		validateAsUpdate(pessoaId, endereco);
		
		return save(endereco);
	}
	
	@Transactional
	public Endereco saveAsUpdate(Long pessoaId, Long enderecoId, Endereco endereco)
	{
		if(findOne(pessoaId, enderecoId) != null)
		{
			endereco.setEnderecoId(enderecoId);
			
			return saveAsUpdate(pessoaId, endereco);
		}
		else
		{
			throw new HttpException(EXCEPTION_ENDERECO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	private Endereco save(Endereco endereco)
	{
		return enderecoRepository.save(endereco);
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsert(Endereco endereco)
	{
		validateIgnoringId(endereco);
		
		if(endereco.getEnderecoId() != null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_ENDERECO_ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateAsUpdate(Long pessoaId, Endereco endereco)
	{
		validateIgnoringId(endereco);
		
		if(endereco.getEnderecoId() == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_ENDERECO_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(findOne(pessoaId, endereco.getEnderecoId()) == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateIgnoringId(Endereco endereco)
	{
		if(endereco == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getCidade() == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CIDADE_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getCidade().getCidadeId() == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CIDADE_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(cidadeService.findOne(endereco.getCidade().getCidadeId()) == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CIDADE_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getCidade().getEstado() == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CIDADE_ESTADO_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getCidade().getEstado().getEstadoId() == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CIDADE_ESTADO_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getCidade().getEstado().getPais() == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CIDADE_ESTADO_PAIS_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getCidade().getEstado().getPais().getPaisId() == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CIDADE_ESTADO_PAIS_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(paisService.findOne(endereco.getCidade().getEstado().getPais().getPaisId()) == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CIDADE_ESTADO_PAIS_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(estadoService.findOne(endereco.getCidade().getEstado().getEstadoId(), endereco.getCidade().getEstado().getPais().getPaisId()) == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CIDADE_ESTADO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringHelper.isBlank(endereco.getLogradouro()))
		{
			throw new HttpException(EXCEPTION_ENDERECO_LOGRADOURO_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getLogradouro().length() > 125)
		{
			throw new HttpException(EXCEPTION_ENDERECO_LOGRADOURO_MUST_NOT_BE_BIGGER_THAN_125_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getComplemento() != null)
		{
			if(endereco.getComplemento().isEmpty())
			{
				throw new HttpException(EXCEPTION_ENDERECO_COMPLEMENTO_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
			}
			
			if(endereco.getComplemento().length() > 45)
			{
				throw new HttpException(EXCEPTION_ENDERECO_COMPLEMENTO_MUST_NOT_BE_BIGGER_THAN_45_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(endereco.getNumero().isEmpty())
		{
			throw new HttpException(EXCEPTION_ENDERECO_NUMERO_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getNumero().length() > 20)
		{
			throw new HttpException(EXCEPTION_ENDERECO_NUMERO_MUST_NOT_BE_BIGGER_THAN_20_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endereco.getCep() == null)
		{
			throw new HttpException(EXCEPTION_ENDERECO_CEP_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(!endereco.getCep().matches(Constants.TEXT_PATTERN_CEP))
		{
			throw new HttpException(EXCEPTION_ENDERECO_CEP_MUST_BE_ONLY_NUMBERS_AND_8_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	private Specification<Endereco> pessoaIdEquals(final Long pessoaId)
	{
		return new Specification<Endereco>()
		{
			@Override
			public Predicate toPredicate(Root<Endereco> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
			{
				Subquery<Long> pessoaQuery = criteriaQuery.subquery(Long.class);
				Root<Pessoa> pessoaRoot = pessoaQuery.from(Pessoa.class);
				Join<Pessoa, Endereco> endereco = pessoaRoot.join("endereco");
				pessoaQuery.select(endereco.get("enderecoId").as(Long.class));
				pessoaQuery.where(criteriaBuilder.equal(pessoaRoot.get("pessoaId"), pessoaId));
				
				return criteriaBuilder.in(root.get("enderecoId")).value(pessoaQuery);
			}
		};
	}
	
	@Transactional(readOnly = true)
	private Specification<Endereco> enderecoIdEquals(final Long enderecoId)
	{
		return new Specification<Endereco>()
		{
			@Override
			public Predicate toPredicate(Root<Endereco> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
			{
				return criteriaBuilder.equal(root.get("enderecoId"), enderecoId);
			}
		};
	}
	
	public static String format(Endereco endereco)
	{
		// null safe for jsp :-)
		if(endereco != null)
		{
			return endereco.getLogradouro() + (endereco.getComplemento() != null ? " - " + endereco.getComplemento() : "") + ", " + endereco.getNumero() + " - " + endereco.getBairro() + " (" + endereco.getCidade().getNome() + "/" + endereco.getCidade().getEstado().getNome() + ")";
		}
		
		return Constants.TEXT_EMPTY;
	}
}
