package br.com.maiscambio.model.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Taxa;
import br.com.maiscambio.model.entity.Taxa.Finalidade;
import br.com.maiscambio.model.entity.Taxa.Moeda;
import br.com.maiscambio.model.entity.Taxa.Status;
import br.com.maiscambio.model.repository.TaxaRepository;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.ReflectionHelper;

@Service
public class TaxaService implements BaseEntityService<Taxa, Long>
{
	public static final String EXCEPTION_TAXA_MUST_NOT_BE_NULL = "EXCEPTION_TAXA_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_TAXA_ESTABELECIMENTO_MUST_NOT_BE_NULL = "EXCEPTION_TAXA_ESTABELECIMENTO_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_TAXA_ESTABELECIMENTO_PESSOA_ID_MUST_NOT_BE_NULL = "EXCEPTION_TAXA_ESTABELECIMENTO_PESSOA_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_TAXA_ESTABELECIMENTO_NOT_FOUND = "EXCEPTION_TAXA_ESTABELECIMENTO_NOT_FOUND";
	public static final String EXCEPTION_TAXA_MOEDA_MUST_NOT_BE_NULL = "EXCEPTION_TAXA_MOEDA_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_TAXA_VALOR_ESPECIE_AND_VALOR_CARTAO_BOTH_MUST_NOT_BE_EMPTY_ = "EXCEPTION_TAXA_VALOR_ESPECIE_AND_VALOR_CARTAO_BOTH_MUST_NOT_BE_EMPTY_";
	public static final String EXCEPTION_TAXA_VALOR_ESPECIE_MUST_BE_BIGGER_THAN_ZERO = "EXCEPTION_TAXA_VALOR_ESPECIE_MUST_BE_BIGGER_THAN_ZERO";
	public static final String EXCEPTION_TAXA_VALOR_CARTAO_MUST_BE_BIGGER_THAN_ZERO = "EXCEPTION_TAXA_VALOR_CARTAO_MUST_BE_BIGGER_THAN_ZERO";
	public static final String EXCEPTION_TAXA_DATA_MUST_NOT_BE_NULL = "TAXA_DATA_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_TAXA_STATUW_MUST_NOT_BE_NULL = "TAXA_STATUW_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_TAXA_FINALIDADE_MUST_NOT_BE_NULL = "TAXA_FINALIDADE_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_TAXA_TAXA_ID_MUST_BE_NULL = "TAXA_TAXA_ID_MUST_BE_NULL";
	
	@Autowired
	private TaxaRepository taxaRepository;
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	@Override
	public Taxa findOne(Long estabelecimentoPessoaId, Long taxaId)
	{
		return taxaRepository.findOne(Specifications.where(estabelecimentoPessoaIdEquals(estabelecimentoPessoaId)).and(taxaIdEquals(taxaId)));
	}
	
	@Transactional(readOnly = true)
	public Taxa findLastByMoedaAndFinalidade(Long estabelecimentoPessoaId, Moeda moeda, Finalidade finalidade)
	{
		return taxaRepository.findLastByEstabelecimentoPessoaIdAndMoedaAndFinalidade(estabelecimentoPessoaId, moeda, finalidade);
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsert(Taxa taxa)
	{
		validateIgnoringTaxaId(taxa);
		
		if(taxa.getTaxaId() != null)
		{
			throw new HttpException(EXCEPTION_TAXA_TAXA_ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	public Taxa saveAsInsert(Taxa taxa)
	{
		validateAsInsert(taxa);
		
		Taxa foundTaxa = findLastByMoedaAndFinalidade(taxa.getEstabelecimento().getPessoaId(), taxa.getMoeda(), taxa.getFinalidade());
		
		if(foundTaxa != null)
		{
			foundTaxa.setStatus(Status.INATIVO);
			
			save(foundTaxa);
		}
		
		return save(taxa);
	}
	
	@Transactional
	private Taxa save(Taxa taxa)
	{
		return taxaRepository.save(taxa);
	}
	
	@Transactional(readOnly = true)
	public void validateIgnoringTaxaId(Taxa taxa)
	{
		if(taxa == null)
		{
			throw new HttpException(EXCEPTION_TAXA_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(taxa.getEstabelecimento() == null)
		{
			throw new HttpException(EXCEPTION_TAXA_ESTABELECIMENTO_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(taxa.getEstabelecimento().getPessoaId() == null)
		{
			throw new HttpException(EXCEPTION_TAXA_ESTABELECIMENTO_PESSOA_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Estabelecimento foundEstabelecimento = estabelecimentoService.findOne(taxa.getEstabelecimento().getPessoaId());
		
		if(foundEstabelecimento == null)
		{
			throw new HttpException(EXCEPTION_TAXA_ESTABELECIMENTO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		taxa.setEstabelecimento(foundEstabelecimento);
		
		if(taxa.getMoeda() == null)
		{
			throw new HttpException(EXCEPTION_TAXA_MOEDA_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(taxa.getValorEspecie() == null && taxa.getValorCartao() == null)
		{
			throw new HttpException(EXCEPTION_TAXA_VALOR_ESPECIE_AND_VALOR_CARTAO_BOTH_MUST_NOT_BE_EMPTY_, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(taxa.getValorEspecie().compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new HttpException(EXCEPTION_TAXA_VALOR_ESPECIE_MUST_BE_BIGGER_THAN_ZERO, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(taxa.getValorCartao().compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new HttpException(EXCEPTION_TAXA_VALOR_CARTAO_MUST_BE_BIGGER_THAN_ZERO, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(taxa.getData() == null)
		{
			throw new HttpException(EXCEPTION_TAXA_DATA_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(taxa.getStatus() == null)
		{
			throw new HttpException(EXCEPTION_TAXA_STATUW_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(taxa.getFinalidade() == null)
		{
			throw new HttpException(EXCEPTION_TAXA_FINALIDADE_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	private Specification<Taxa> estabelecimentoPessoaIdEquals(final Long taxaId)
	{
		return new Specification<Taxa>()
		{
			@Override
			public Predicate toPredicate(Root<Taxa> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
			{
				return criteriaBuilder.equal(root.get("estabelecimento").get("pessoaId"), taxaId);
			}
		};
	}
	
	@Transactional(readOnly = true)
	private Specification<Taxa> taxaIdEquals(final Long taxaId)
	{
		return new Specification<Taxa>()
		{
			@Override
			public Predicate toPredicate(Root<Taxa> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
			{
				return criteriaBuilder.equal(root.get("taxaId"), taxaId);
			}
		};
	}
	
	public List<String> getMoedasAsStringList()
	{
		List<String> moedas = ReflectionHelper.fieldsToNames(ReflectionHelper.getEnumFieldsConstants(Taxa.Moeda.class));
		
		return moedas;
	}
}
