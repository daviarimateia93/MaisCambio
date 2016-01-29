package br.com.maiscambio.model.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Pessoa;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.repository.PessoaRepository;
import br.com.maiscambio.util.Constants;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.StringHelper;

@Service
public class PessoaService implements BaseService
{
	public static final String EXCEPTION_PESSOA_CNPJ_CPF_ID_ESTRANGEIRO_BOTH_MUST_NOT_BE_EMPTY = "PESSOA_CNPJ_CPF_ID_ESTRANGEIRO_BOTH_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_PESSOA_CNPJ_MUST_BE_ONLY_NUMBERS_AND_14_CHARACTERS = "PESSOA_CNPJ_MUST_BE_ONLY_NUMBERS_AND_14_CHARACTERS";
	public static final String EXCEPTION_PESSOA_CNPJ_IS_INVALID = "PESSOA_CNPJ_IS_INVALID";
	public static final String EXCEPTION_PESSOA_CPF_MUST_BE_ONLY_NUMBERS_AND_11_CHARACTERS = "PESSOA_CPF_MUST_BE_ONLY_NUMBERS_AND_11_CHARACTERS";
	public static final String EXCEPTION_PESSOA_CPF_IS_INVALID = "PESSOA_CPF_IS_INVALID";
	public static final String EXCEPTION_PESSOA_ID_ESTRANGEIRO_IS_INVALID = "PESSOA_ID_ESTRANGEIRO_IS_INVALID";
	public static final String EXCEPTION_PESSOA_TELEFONE1_IS_INVALID = "PESSOA_TELEFONE1_IS_INVALID";
	public static final String EXCEPTION_PESSOA_TELEFONE1_MUST_NOT_BE_EMPTY = "PESSOA_TELEFONE1_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_PESSOA_TELEFONE2_IS_INVALID = "PESSOA_TELEFONE2_IS_INVALID";
	public static final String EXCEPTION_PESSOA_MUST_NOT_BE_NULL = "PESSOA_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_PESSOA_PESSOA_ID_MUST_BE_NULL = "PESSOA_PESSOA_ID_MUST_BE_NULL";
	public static final String EXCEPTION_PESSOA_PESSOA_ID_MUST_NOT_BE_NULL = "PESSOA_PESSOA_ID_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_PESSOA_NOT_FOUND = "PESSOA_NOT_FOUND";
	public static final String EXCEPTION_PESSOA_USUARIO_IS_ALREADY_IN_USE = "PESSOA_USUARIO_IS_ALREADY_IN_USE";
	public static final String EXCEPTION_PESSOA_ENDERECO_MUST_BELONGS_TO_PESSOA = "PESSOA_ENDERECO_MUST_BELONGS_TO_PESSOA";
	public static final String EXCEPTION_PESSOA_EMAIL_MUST_NOT_BE_EMPTY = "PESSOA_EMAIL_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_PESSOA_EMAIL_MUST_NOT_BE_BIGGER_THAN_320_CHARACTERS = "PESSOA_EMAIL_MUST_NOT_BE_BIGGER_THAN_320_CHARACTERS";
	public static final String EXCEPTION_PESSOA_EMAIL_IS_INVALID = "PESSOA_EMAIL_IS_INVALID";
	public static final String EXCEPTION_PESSOA_NOME_CONTATO_MUST_NOT_BE_EMPTY = "PESSOA_NOME_CONTATO_MUST_NOT_BE_EMPTY";
	public static final String EXCEPTION_PESSOA_NOME_CONTATO_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS = "PESSOA_NOME_CONTATO_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS";
	public static final String EXCEPTION_PESSOA_CNPJ_MUST_NOT_BE_NULL = "PESSOA_CNPJ_MUST_NOT_BE_NULL";
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	protected PessoaRepository getPessoaRepository()
	{
		return pessoaRepository;
	}
	
	protected UsuarioService getUsuarioService()
	{
		return usuarioService;
	}
	
	protected EnderecoService enderecoService()
	{
		return enderecoService;
	}
	
	@Transactional(readOnly = true)
	public Pessoa getFromRequest(HttpServletRequest request)
	{
		Usuario usuario = usuarioService.getFromRequest(request);
		
		if(usuario != null)
		{
			return pessoaRepository.findByUsuarioId(usuario.getUsuarioId());
		}
		else
		{
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public Pessoa findOne(Long pessoaId)
	{
		return pessoaRepository.findOne(pessoaId);
	}
	
	@Transactional(readOnly = true)
	public Pessoa findByUsuarioId(Long usuarioId)
	{
		return pessoaRepository.findByUsuarioId(usuarioId);
	}
	
	@Transactional(readOnly = true)
	protected void validateAsInsert(Pessoa pessoa, boolean usuarioIgnoringPessoa)
	{
		validateIgnoringId(pessoa);
		
		if(pessoa.getPessoaId() != null)
		{
			throw new HttpException(EXCEPTION_PESSOA_PESSOA_ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(pessoa.getUsuarios() != null)
		{
			for(Usuario usuario : pessoa.getUsuarios())
			{
				if(usuarioIgnoringPessoa)
				{
					usuarioService.validateAsInsertIgnoringPessoa(usuario);
				}
				else
				{
					usuarioService.validateAsInsert(usuario);
				}
			}
		}
		
		enderecoService.validateAsInsert(pessoa.getEndereco());
	}
	
	@Transactional(readOnly = true)
	protected void validateAsUpdate(Pessoa pessoa, boolean usuarioIgnoringPessoa)
	{
		validateIgnoringId(pessoa);
		
		if(pessoa.getPessoaId() == null)
		{
			throw new HttpException(EXCEPTION_PESSOA_PESSOA_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Pessoa foundPessoa = pessoaRepository.findOne(pessoa.getPessoaId());
		
		if(foundPessoa == null)
		{
			throw new HttpException(EXCEPTION_PESSOA_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(pessoa.getUsuarios() != null)
		{
			for(Usuario usuario : pessoa.getUsuarios())
			{
				Pessoa foundPessoaByUsuarioId = pessoaRepository.findByUsuarioId(usuario.getUsuarioId());
				
				if(foundPessoaByUsuarioId != null)
				{
					if(foundPessoaByUsuarioId.getPessoaId() != pessoa.getPessoaId())
					{
						throw new HttpException(EXCEPTION_PESSOA_USUARIO_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
					}
				}
				
				if(usuarioIgnoringPessoa)
				{
					usuarioService.validateAsUpdateIgnoringPessoa(usuario);
				}
				else
				{
					usuarioService.validateAsUpdate(usuario);
				}
			}
		}
		
		enderecoService.validateAsUpdate(pessoa.getPessoaId(), pessoa.getEndereco());
		
		if(!foundPessoa.getEndereco().getEnderecoId().equals(pessoa.getEndereco().getEnderecoId()))
		{
			throw new HttpException(EXCEPTION_PESSOA_ENDERECO_MUST_BELONGS_TO_PESSOA, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	protected void validateIgnoringId(Pessoa pessoa)
	{
		if(pessoa == null)
		{
			throw new HttpException(EXCEPTION_PESSOA_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringHelper.isBlank(pessoa.getCnpj()) && StringHelper.isBlank(pessoa.getCpf()) && StringHelper.isBlank(pessoa.getIdEstrangeiro()))
		{
			throw new HttpException(EXCEPTION_PESSOA_CNPJ_CPF_ID_ESTRANGEIRO_BOTH_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(pessoa.getCnpj() != null)
		{
			if(!pessoa.getCnpj().matches(Constants.TEXT_PATTERN_CNPJ))
			{
				throw new HttpException(EXCEPTION_PESSOA_CNPJ_MUST_BE_ONLY_NUMBERS_AND_14_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
			}
			
			if(!validateCnpj(pessoa.getCnpj()))
			{
				throw new HttpException(EXCEPTION_PESSOA_CNPJ_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(pessoa.getCpf() != null)
		{
			if(!pessoa.getCpf().matches(Constants.TEXT_PATTERN_CPF))
			{
				throw new HttpException(EXCEPTION_PESSOA_CPF_MUST_BE_ONLY_NUMBERS_AND_11_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
			}
			
			if(!validateCpf(pessoa.getCpf()))
			{
				throw new HttpException(EXCEPTION_PESSOA_CPF_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(pessoa.getIdEstrangeiro() != null)
		{
			if(!pessoa.getIdEstrangeiro().matches(Constants.TEXT_PATTERN_ID_ESTRANGEIRO))
			{
				throw new HttpException(EXCEPTION_PESSOA_ID_ESTRANGEIRO_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(StringHelper.isBlank(pessoa.getTelefone1()))
		{
			throw new HttpException(EXCEPTION_PESSOA_TELEFONE1_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(!pessoa.getTelefone1().matches(Constants.TEXT_PATTERN_TELEFONE))
		{
			throw new HttpException(EXCEPTION_PESSOA_TELEFONE1_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(pessoa.getTelefone2() != null)
		{
			if(!pessoa.getTelefone2().matches(Constants.TEXT_PATTERN_TELEFONE))
			{
				throw new HttpException(EXCEPTION_PESSOA_TELEFONE2_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		if(StringHelper.isBlank(pessoa.getEmail()))
		{
			throw new HttpException(EXCEPTION_PESSOA_EMAIL_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(pessoa.getEmail().length() > 320)
		{
			throw new HttpException(EXCEPTION_PESSOA_EMAIL_MUST_NOT_BE_BIGGER_THAN_320_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(!pessoa.getEmail().matches(Constants.TEXT_PATTERN_EMAIL))
		{
			throw new HttpException(EXCEPTION_PESSOA_EMAIL_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(pessoa.getEndereco() != null)
		{
			enderecoService.validateIgnoringId(pessoa.getEndereco());
		}
	}
	
	protected boolean validateCpf(String cpf)
	{
		try
		{
			int preValue1, preValue2, value1, value2, mod, digit;
			
			preValue1 = preValue2 = value1 = value2 = mod = 0;
			
			String computedMagicNumber;
			
			for(int i = 1; i < cpf.length() - 1; i++)
			{
				digit = Integer.valueOf(cpf.substring(i - 1, i)).intValue();
				
				preValue1 = preValue1 + (11 - i) * digit;
				
				preValue2 = preValue2 + (12 - i) * digit;
			}
			
			mod = (preValue1 % 11);
			
			value1 = mod < 2 ? 0 : 11 - mod;
			
			preValue2 += 2 * value1;
			
			mod = (preValue2 % 11);
			
			value2 = mod < 2 ? 0 : 11 - mod;
			
			String magicNumber = cpf.substring(cpf.length() - 2, cpf.length());
			
			computedMagicNumber = String.valueOf(value1) + String.valueOf(value2);
			
			return magicNumber.equals(computedMagicNumber);
		}
		catch(Exception exception)
		{
			return false;
		}
	}
	
	protected boolean validateCnpj(String cnpj)
	{
		int sum = 0, digit;
		
		String toCalculate = cnpj.substring(0, 12);
		
		if(cnpj.length() != 14)
		{
			return false;
		}
		
		char[] cnpjCharacters = cnpj.toCharArray();
		
		for(int i = 0; i < 4; i++)
		{
			if(cnpjCharacters[i] - 48 >= 0 && cnpjCharacters[i] - 48 <= 9)
			{
				sum += (cnpjCharacters[i] - 48) * (6 - (i + 1));
			}
		}
		
		for(int i = 0; i < 8; i++)
		{
			if(cnpjCharacters[i + 4] - 48 >= 0 && cnpjCharacters[i + 4] - 48 <= 9)
			{
				sum += (cnpjCharacters[i + 4] - 48) * (10 - (i + 1));
			}
		}
		
		digit = 11 - (sum % 11);
		
		toCalculate += digit == 10 || digit == 11 ? Constants.CHAR_0 : Integer.toString(digit);
		
		sum = 0;
		
		for(int i = 0; i < 5; i++)
		{
			if(cnpjCharacters[i] - 48 >= 0 && cnpjCharacters[i] - 48 <= 9)
			{
				sum += (cnpjCharacters[i] - 48) * (7 - (i + 1));
			}
		}
		
		for(int i = 0; i < 8; i++)
		{
			if(cnpjCharacters[i + 5] - 48 >= 0 && cnpjCharacters[i + 5] - 48 <= 9)
			{
				sum += (cnpjCharacters[i + 5] - 48) * (10 - (i + 1));
			}
		}
		
		digit = 11 - (sum % 11);
		
		toCalculate += digit == 10 || digit == 11 ? Constants.CHAR_0 : Integer.toString(digit);
		
		return cnpj.equals(toCalculate);
	}
}