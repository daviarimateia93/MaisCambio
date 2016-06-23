package br.com.maiscambio.model.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Iof;
import br.com.maiscambio.model.entity.Iof.Finalidade;
import br.com.maiscambio.model.entity.Iof.Status;
import br.com.maiscambio.model.repository.IofRepository;
import me.gerenciar.util.GlobalBaseEntityService;
import me.gerenciar.util.HttpException;

@Service
public class IofService implements GlobalBaseEntityService<Iof, Long>
{
	public static final String EXCEPTION_IOF_MUST_NOT_BE_NULL = "IOF_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_IOF_VALOR_ESPECIE_AND_VALOR_CARTAO_BOTH_MUST_NOT_BE_EMPTY_ = "IOF_VALOR_ESPECIE_AND_VALOR_CARTAO_BOTH_MUST_NOT_BE_EMPTY_";
	public static final String EXCEPTION_IOF_VALOR_ESPECIE_MUST_BE_BIGGER_THAN_ZERO = "IOF_VALOR_ESPECIE_MUST_BE_BIGGER_THAN_ZERO";
	public static final String EXCEPTION_IOF_VALOR_ESPECIE_TOO_LARGE = "IOF_VALOR_ESPECIE_TOO_LARGE";
	public static final String EXCEPTION_IOF_VALOR_CARTAO_MUST_BE_BIGGER_THAN_ZERO = "IOF_VALOR_CARTAO_MUST_BE_BIGGER_THAN_ZERO";
	public static final String EXCEPTION_IOF_VALOR_CARTAO_TOO_LARGE = "IOF_VALOR_CARTAO_TOO_LARGE";
	public static final String EXCEPTION_IOF_DATA_MUST_NOT_BE_NULL = "IOF_DATA_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_IOF_STATUS_MUST_NOT_BE_NULL = "IOF_STATUS_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_IOF_FINALIDADE_MUST_NOT_BE_NULL = "IOF_FINALIDADE_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_IOF_IOF_ID_MUST_BE_NULL = "IOF_IOF_ID_MUST_BE_NULL";
	
	@Autowired
	private IofRepository iofRepository;
	
	@Override
	public Iof findOne(Long iofId)
	{
		return iofRepository.findOne(iofId);
	}
	
	@Transactional(readOnly = true)
	public Iof findLastByFinalidade(Finalidade finalidade)
	{
		return iofRepository.findLastByFinalidade(finalidade);
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsert(Iof iof)
	{
		validateIgnoringId(iof);
		
		if(iof.getIofId() != null)
		{
			throw new HttpException(EXCEPTION_IOF_IOF_ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	public Iof saveAsInsert(Iof iof)
	{
		validateAsInsert(iof);
		
		Iof foundIof = findLastByFinalidade(iof.getFinalidade());
		
		if(foundIof != null)
		{
			foundIof.setStatus(Status.INATIVO);
			
			save(foundIof);
		}
		
		return save(iof);
	}
	
	@Transactional
	private Iof save(Iof iof)
	{
		return iofRepository.save(iof);
	}
	
	@Transactional(readOnly = true)
	public void validateIgnoringId(Iof iof)
	{
		if(iof == null)
		{
			throw new HttpException(EXCEPTION_IOF_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(iof.getValorEspecie() == null && iof.getValorCartao() == null)
		{
			throw new HttpException(EXCEPTION_IOF_VALOR_ESPECIE_AND_VALOR_CARTAO_BOTH_MUST_NOT_BE_EMPTY_, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(iof.getValorEspecie() != null)
		{
			if(iof.getValorEspecie().compareTo(BigDecimal.ZERO) <= 0)
			{
				throw new HttpException(EXCEPTION_IOF_VALOR_ESPECIE_MUST_BE_BIGGER_THAN_ZERO, HttpStatus.NOT_ACCEPTABLE);
			}
			
			if(iof.getValorEspecie().compareTo(new BigDecimal("999.99")) > 0)
			{
				throw new HttpException(EXCEPTION_IOF_VALOR_ESPECIE_TOO_LARGE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(iof.getValorCartao() != null)
		{
			if(iof.getValorCartao().compareTo(BigDecimal.ZERO) <= 0)
			{
				throw new HttpException(EXCEPTION_IOF_VALOR_CARTAO_MUST_BE_BIGGER_THAN_ZERO, HttpStatus.NOT_ACCEPTABLE);
			}
			
			if(iof.getValorCartao().compareTo(new BigDecimal("999.99")) > 0)
			{
				throw new HttpException(EXCEPTION_IOF_VALOR_CARTAO_TOO_LARGE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(iof.getData() == null)
		{
			throw new HttpException(EXCEPTION_IOF_DATA_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(iof.getStatus() == null)
		{
			throw new HttpException(EXCEPTION_IOF_STATUS_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(iof.getFinalidade() == null)
		{
			throw new HttpException(EXCEPTION_IOF_FINALIDADE_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
	}
}