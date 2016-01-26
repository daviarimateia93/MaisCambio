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
	public static final String EXCEPTION_PESSOA_MUST_NOT_CONTAINS_CPF_AND_INSCRICAO_ESTADUAL = "PESSOA_MUST_NOT_CONTAINS_CPF_AND_INSCRICAO_ESTADUAL";
	public static final String EXCEPTION_PESSOA_CNPJ_MUST_NOT_BE_NULL = "PESSOA_CNPJ_MUST_NOT_BE_NULL";
	public static final String EXCEPTION_PESSOA_INSCRICAO_ESTADUAL_IS_INVALID = "PESSOA_INSCRICAO_ESTADUAL_IS_INVALID";
	public static final String EXCEPTION_PESSOA_INSCRICAO_SUFRAMA_IS_INVALID = "PESSOA_INSCRICAO_SUFRAMA_IS_INVALID";
	
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
	
	protected boolean validateInscricaoEstadual(String inscricaoEstadual, String estadoId)
	{
		switch(estadoId)
		{
			case "AC":
				return validateInscricaoEstadualAC(inscricaoEstadual);
			case "AL":
				return validateInscricaoEstadualAL(inscricaoEstadual);
			case "AP":
				return validateInscricaoEstadualAP(inscricaoEstadual);
			case "AM":
				return validateInscricaoEstadualAM(inscricaoEstadual);
			case "BA":
				return validateInscricaoEstadualBA(inscricaoEstadual);
			case "CE":
				return validateInscricaoEstadualCE(inscricaoEstadual);
			case "ES":
				return validateInscricaoEstadualES(inscricaoEstadual);
			case "GO":
				return validateInscricaoEstadualGO(inscricaoEstadual);
			case "MA":
				return validateInscricaoEstadualMA(inscricaoEstadual);
			case "MT":
				return validateInscricaoEstadualMT(inscricaoEstadual);
			case "MS":
				return validateInscricaoEstadualMS(inscricaoEstadual);
			case "MG":
				return validateInscricaoEstadualMG(inscricaoEstadual);
			case "PA":
				return validateInscricaoEstadualPA(inscricaoEstadual);
			case "PB":
				return validateInscricaoEstadualPB(inscricaoEstadual);
			case "PR":
				return validateInscricaoEstadualPR(inscricaoEstadual);
			case "PE":
				return validateInscricaoEstadualPE(inscricaoEstadual);
			case "PI":
				return validateInscricaoEstadualPI(inscricaoEstadual);
			case "RJ":
				return validateInscricaoEstadualRJ(inscricaoEstadual);
			case "RN":
				return validateInscricaoEstadualRN(inscricaoEstadual);
			case "RS":
				return validateInscricaoEstadualRS(inscricaoEstadual);
			case "RO":
				return validateInscricaoEstadualRO(inscricaoEstadual);
			case "RR":
				return validateInscricaoEstadualRR(inscricaoEstadual);
			case "SC":
				return validateInscricaoEstadualSC(inscricaoEstadual);
			case "SP":
				return validateInscricaoEstadualSP(inscricaoEstadual);
			case "SE":
				return validateInscricaoEstadualSE(inscricaoEstadual);
			case "TO":
				return validateInscricaoEstadualTO(inscricaoEstadual);
			case "DF":
				return validateInscricaoEstadualDF(inscricaoEstadual);
			default:
				return false;
		}
	}
	
	protected boolean validateInscricaoEstadualAC(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 13)
		{
			return false;
		}
		
		for(int i = 0; i < 2; i++)
		{
			if(Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) != i)
			{
				return false;
			}
		}
		
		int sum = 0;
		int poundInicial = 4;
		int poundFinal = 9;
		int digit1 = 0;
		int digit2 = 0;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			if(i < 3)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * poundInicial;
				poundInicial--;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * poundFinal;
				poundFinal--;
			}
		}
		
		digit1 = 11 - (sum % 11);
		
		if(digit1 == 10 || digit1 == 11)
		{
			digit1 = 0;
		}
		
		sum = digit1 * 2;
		poundInicial = 5;
		poundFinal = 9;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			if(i < 4)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * poundInicial;
				poundInicial--;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * poundFinal;
				poundFinal--;
			}
		}
		
		digit2 = 11 - (sum % 11);
		
		if(digit2 == 10 || digit2 == 11)
		{
			digit2 = 0;
		}
		
		String finalDigit = digit1 + "" + digit2;
		
		if(!finalDigit.equals(inscricaoEstadual.substring(inscricaoEstadual.length() - 2, inscricaoEstadual.length())))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualAL(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		if(!inscricaoEstadual.substring(0, 2).equals("24"))
		{
			return false;
		}
		
		int[] digits = { 0, 3, 5, 7, 8 };
		boolean check = false;
		
		for(int i = 0; i < digits.length; i++)
		{
			if(Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(2))) == digits[i])
			{
				check = true;
				break;
			}
		}
		
		if(!check)
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = 0;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		digit = ((sum * 10) % 11);
		
		if(digit == 10)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualAP(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		if(!inscricaoEstadual.substring(0, 2).equals("03"))
		{
			return false;
		}
		
		int digit1 = -1;
		int sum = -1;
		int pound = 9;
		long x = Long.parseLong(inscricaoEstadual.substring(0, inscricaoEstadual.length() - 1));
		
		if(x >= 3017001L && x <= 3019022L)
		{
			digit1 = 1;
			sum = 9;
		}
		else if(x >= 3000001L && x <= 3017000L)
		{
			digit1 = 0;
			sum = 5;
		}
		else if(x >= 3019023L)
		{
			digit1 = 0;
			sum = 0;
		}
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		int digit = 11 - ((sum % 11));
		
		if(digit == 10)
		{
			digit = 0;
		}
		else if(digit == 11)
		{
			digit = digit1;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualAM(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		if(sum < 11)
		{
			digit = 11 - sum;
		}
		else if((sum % 11) <= 1)
		{
			digit = 0;
		}
		else
		{
			digit = 11 - (sum % 11);
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualBA(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 8 && inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		int modulo = 10;
		int firstDigit = Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(inscricaoEstadual.length() == 8 ? 0 : 1)));
		
		if(firstDigit == 6 || firstDigit == 7 || firstDigit == 9)
			modulo = 11;
			
		int digit2 = -1;
		int sum = 0;
		int pound = inscricaoEstadual.length() == 8 ? 7 : 8;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		int resto = sum % modulo;
		
		if(resto == 0 || (modulo == 11 && resto == 1))
		{
			digit2 = 0;
		}
		else
		{
			digit2 = modulo - resto;
		}
		
		int digit1 = -1;
		sum = digit2 * 2;
		pound = inscricaoEstadual.length() == 8 ? 8 : 9;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		resto = sum % modulo;
		
		if(resto == 0 || (modulo == 11 && resto == 1))
		{
			digit1 = 0;
		}
		else
		{
			digit1 = modulo - resto;
		}
		
		String finalDigit = digit1 + "" + digit2;
		
		if(!finalDigit.equals(inscricaoEstadual.substring(inscricaoEstadual.length() - 2, inscricaoEstadual.length())))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualCE(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		digit = 11 - (sum % 11);
		
		if(digit == 10 || digit == 11)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualES(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		int resto = sum % 11;
		
		if(resto < 2)
		{
			digit = 0;
		}
		else if(resto > 1)
		{
			digit = 11 - resto;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualGO(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		if(!"10".equals(inscricaoEstadual.substring(0, 2)))
		{
			if(!"11".equals(inscricaoEstadual.substring(0, 2)))
			{
				if(!"15".equals(inscricaoEstadual.substring(0, 2)))
				{
					return false;
				}
			}
		}
		
		if(inscricaoEstadual.substring(0, inscricaoEstadual.length() - 1).equals("11094402"))
		{
			if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals("0"))
			{
				if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals("1"))
				{
					return false;
				}
			}
			
			return true;
		}
		else
		{
			int sum = 0;
			int pound = 9;
			int digit = -1;
			
			for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
				pound--;
			}
			
			int resto = sum % 11;
			long faixaInicio = 10103105;
			long faixaFim = 10119997;
			long insc = Long.parseLong(inscricaoEstadual.substring(0, inscricaoEstadual.length() - 1));
			
			if(resto == 0)
			{
				digit = 0;
			}
			else if(resto == 1)
			{
				if(insc >= faixaInicio && insc <= faixaFim)
				{
					digit = 1;
				}
				else
				{
					digit = 0;
				}
			}
			else if(resto != 0 && resto != 1)
			{
				digit = 11 - resto;
			}
			
			String finalDigit = digit + "";
			
			if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
			{
				return false;
			}
			
			return true;
		}
	}
	
	protected boolean validateInscricaoEstadualMA(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		if(!inscricaoEstadual.substring(0, 2).equals("12"))
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		digit = 11 - (sum % 11);
		
		if((sum % 11) == 0 || (sum % 11) == 1)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualMT(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 11)
		{
			return false;
		}
		
		int sum = 0;
		int poundInicial = 3;
		int poundFinal = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			if(i < 2)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * poundInicial;
				poundInicial--;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * poundFinal;
				poundFinal--;
			}
		}
		
		digit = 11 - (sum % 11);
		
		if((sum % 11) == 0 || (sum % 11) == 1)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualMS(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		if(!inscricaoEstadual.substring(0, 2).equals("28"))
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		int resto = sum % 11;
		int result = 11 - resto;
		
		if(resto == 0)
		{
			digit = 0;
		}
		else if(resto > 0)
		{
			if(result > 9)
			{
				digit = 0;
			}
			else if(result < 10)
			{
				digit = result;
			}
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualMG(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 13)
		{
			return false;
		}
		
		String str = "";
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			if(Character.isDigit(inscricaoEstadual.charAt(i)))
			{
				if(i == 3)
				{
					str += "0";
					str += inscricaoEstadual.charAt(i);
				}
				else
				{
					str += inscricaoEstadual.charAt(i);
				}
			}
		}
		
		int sum = 0;
		int initialPound = 1;
		int finalPound = 2;
		int digit1 = -1;
		
		for(int i = 0; i < str.length(); i++)
		{
			if(i % 2 == 0)
			{
				int x = Integer.parseInt(String.valueOf(str.charAt(i))) * initialPound;
				String strX = Integer.toString(x);
				
				for(int j = 0; j < strX.length(); j++)
				{
					sum += Integer.parseInt(String.valueOf(strX.charAt(j)));
				}
			}
			else
			{
				int y = Integer.parseInt(String.valueOf(str.charAt(i))) * finalPound;
				String strY = Integer.toString(y);
				
				for(int j = 0; j < strY.length(); j++)
				{
					sum += Integer.parseInt(String.valueOf(strY.charAt(j)));
				}
			}
		}
		
		int dezenaExata = sum;
		
		while(dezenaExata % 10 != 0)
		{
			dezenaExata++;
		}
		
		digit1 = dezenaExata - sum;
		
		sum = digit1 * 2;
		initialPound = 3;
		finalPound = 11;
		int digit2 = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			if(i < 2)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * initialPound;
				initialPound--;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * finalPound;
				finalPound--;
			}
		}
		
		digit2 = 11 - (sum % 11);
		
		if((sum % 11 == 0) || (sum % 11 == 1))
		{
			digit2 = 0;
		}
		
		String finalDigit = digit1 + "" + digit2;
		
		if(!finalDigit.equals(inscricaoEstadual.substring(inscricaoEstadual.length() - 2, inscricaoEstadual.length())))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualPA(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		if(!inscricaoEstadual.substring(0, 2).equals("15"))
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		digit = 11 - (sum % 11);
		
		if((sum % 11) == 0 || (sum % 11) == 1)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualPB(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		digit = 11 - (sum % 11);
		
		if(digit == 10 || digit == 11)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualPR(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 10)
		{
			return false;
		}
		
		int sum = 0;
		int initialPound = 3;
		int finalPound = 7;
		int digit1 = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			if(i < 2)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * initialPound;
				initialPound--;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * finalPound;
				finalPound--;
			}
		}
		
		digit1 = 11 - (sum % 11);
		
		if((sum % 11) == 0 || (sum % 11) == 1)
		{
			digit1 = 0;
		}
		
		sum = digit1 * 2;
		initialPound = 4;
		finalPound = 7;
		int digit2 = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			if(i < 3)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * initialPound;
				initialPound--;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * finalPound;
				finalPound--;
			}
		}
		
		digit2 = 11 - (sum % 11);
		
		if((sum % 11) == 0 || (sum % 11) == 1)
		{
			digit2 = 0;
		}
		
		String finalDigit = digit1 + "" + digit2;
		
		if(!finalDigit.equals(inscricaoEstadual.substring(inscricaoEstadual.length() - 2, inscricaoEstadual.length())))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualPE(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		int sum = 0;
		int initialPound = 8;
		int finalPound = 9;
		int digit1 = -1;
		int digit2 = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * initialPound;
			initialPound--;
		}
		
		digit1 = sum % 11;
		
		if(digit1 == 0 || digit1 == 1)
		{
			digit1 = 0;
		}
		else
		{
			digit1 = 11 - digit1;
		}
		
		sum = 0;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * finalPound;
			finalPound--;
		}
		
		digit2 = sum % 11;
		
		if(digit2 == 0 || digit2 == 1)
		{
			digit2 = 0;
		}
		else
		{
			digit2 = 11 - digit2;
		}
		
		String finalDigit = "" + digit1 + digit2;
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 2, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualPI(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		digit = 11 - (sum % 11);
		
		if(digit == 11 || digit == 10)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualRJ(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 8)
		{
			return false;
		}
		
		int sum = 0;
		int pound = 7;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			if(i == 0)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * 2;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
				pound--;
			}
		}
		
		digit = 11 - (sum % 11);
		
		if((sum % 11) <= 1)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualRN(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 10 && inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		if(!inscricaoEstadual.substring(0, 2).equals("20"))
		{
			return false;
		}
		
		if(inscricaoEstadual.length() == 9)
		{
			int sum = 0;
			int pound = 9;
			int digit = -1;
			
			for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
				pound--;
			}
			
			digit = ((sum * 10) % 11);
			
			if(digit == 10)
			{
				digit = 0;
			}
			
			String finalDigit = digit + "";
			
			if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
			{
				return false;
			}
		}
		else
		{
			int sum = 0;
			int pound = 10;
			int digit = -1;
			
			for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
				pound--;
			}
			
			digit = ((sum * 10) % 11);
			
			if(digit == 10)
			{
				digit = 0;
			}
			
			String finalDigit = digit + "";
			
			if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
			{
				return false;
			}
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualRS(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 10)
		{
			return false;
		}
		
		int sum = Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(0))) * 2;
		int pound = 9;
		int digit = -1;
		
		for(int i = 1; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		digit = 11 - (sum % 11);
		
		if(digit == 10 || digit == 11)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualRO(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 14)
		{
			return false;
		}
		
		int sum = 0;
		int initialPound = 6;
		int finalPound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			if(i < 5)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * initialPound;
				initialPound--;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * finalPound;
				finalPound--;
			}
		}
		
		digit = 11 - (sum % 11);
		
		if(digit == 11 || digit == 10)
		{
			digit -= 10;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualRR(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		if(!inscricaoEstadual.substring(0, 2).equals("24"))
		{
			return false;
		}
		
		int sum = 0;
		int pound = 1;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound++;
		}
		
		digit = sum % 9;
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualSC(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		digit = 11 - (sum % 11);
		
		if((sum % 11) == 0 || (sum % 11) == 1)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualSP(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 12 && inscricaoEstadual.length() != 13)
		{
			return false;
		}
		
		if(inscricaoEstadual.length() == 12)
		{
			int sum = 0;
			int pound = 1;
			int digit1 = -1;
			
			for(int i = 0; i < inscricaoEstadual.length() - 4; i++)
			{
				if(i == 1 || i == 7)
				{
					sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * ++pound;
					pound++;
				}
				else
				{
					sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
					pound++;
				}
			}
			
			digit1 = sum % 11;
			String strdigit1 = Integer.toString(digit1);
			digit1 = Integer.parseInt(String.valueOf(strdigit1.charAt(strdigit1.length() - 1)));
			
			sum = 0;
			int initialPound = 3;
			int finalPound = 10;
			int digit2 = -1;
			
			for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
			{
				if(i < 2)
				{
					sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * initialPound;
					initialPound--;
				}
				else
				{
					sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * finalPound;
					finalPound--;
				}
			}
			
			digit2 = sum % 11;
			String strdigit2 = Integer.toString(digit2);
			digit2 = Integer.parseInt(String.valueOf(strdigit2.charAt(strdigit2.length() - 1)));
			
			if(!inscricaoEstadual.substring(8, 9).equals(digit1 + ""))
			{
				return false;
			}
			
			if(!inscricaoEstadual.substring(11, 12).equals(digit2 + ""))
			{
				return false;
			}
			
		}
		else
		{
			if(inscricaoEstadual.charAt(0) != 'P')
			{
				return false;
			}
			
			String strinscricaoEstadual = inscricaoEstadual.substring(1, 10);
			
			int sum = 0;
			int pound = 1;
			int digit1 = -1;
			
			for(int i = 0; i < strinscricaoEstadual.length() - 1; i++)
			{
				if(i == 1 || i == 7)
				{
					sum += Integer.parseInt(String.valueOf(strinscricaoEstadual.charAt(i))) * ++pound;
					pound++;
				}
				else
				{
					sum += Integer.parseInt(String.valueOf(strinscricaoEstadual.charAt(i))) * pound;
					pound++;
				}
			}
			
			digit1 = sum % 11;
			String strdigit1 = Integer.toString(digit1);
			digit1 = Integer.parseInt(String.valueOf(strdigit1.charAt(strdigit1.length() - 1)));
			
			if(!inscricaoEstadual.substring(9, 10).equals(digit1 + ""))
			{
				return false;
			}
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualSE(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9)
		{
			return false;
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
			pound--;
		}
		
		digit = 11 - (sum % 11);
		
		if(digit == 11 || digit == 11 || digit == 10)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualTO(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 9 && inscricaoEstadual.length() != 11)
		{
			return false;
		}
		else if(inscricaoEstadual.length() == 9)
		{
			inscricaoEstadual = inscricaoEstadual.substring(0, 2) + "02" + inscricaoEstadual.substring(2);
		}
		
		int sum = 0;
		int pound = 9;
		int digit = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 1; i++)
		{
			if(i != 2 && i != 3)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * pound;
				pound--;
			}
		}
		
		digit = 11 - (sum % 11);
		
		if((sum % 11) < 2)
		{
			digit = 0;
		}
		
		String finalDigit = digit + "";
		
		if(!inscricaoEstadual.substring(inscricaoEstadual.length() - 1, inscricaoEstadual.length()).equals(finalDigit))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoEstadualDF(String inscricaoEstadual)
	{
		if(inscricaoEstadual.length() != 13)
		{
			return false;
		}
		
		int sum = 0;
		int initialPound = 4;
		int finalPound = 9;
		int digit1 = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			if(i < 3)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * initialPound;
				initialPound--;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * finalPound;
				finalPound--;
			}
		}
		
		digit1 = 11 - (sum % 11);
		
		if(digit1 == 11 || digit1 == 10)
		{
			digit1 = 0;
		}
		
		sum = digit1 * 2;
		initialPound = 5;
		finalPound = 9;
		int digit2 = -1;
		
		for(int i = 0; i < inscricaoEstadual.length() - 2; i++)
		{
			if(i < 4)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * initialPound;
				initialPound--;
			}
			else
			{
				sum += Integer.parseInt(String.valueOf(inscricaoEstadual.charAt(i))) * finalPound;
				finalPound--;
			}
		}
		
		digit2 = 11 - (sum % 11);
		
		if(digit2 == 11 || digit2 == 10)
		{
			digit2 = 0;
		}
		
		String finalDigit = digit1 + "" + digit2;
		
		if(!finalDigit.equals(inscricaoEstadual.substring(inscricaoEstadual.length() - 2, inscricaoEstadual.length())))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean validateInscricaoSuframa(String inscricaoSuframa, String estadoId, String cidadeNome)
	{
		if(("AC".equals(estadoId)) || ("AM".equals(estadoId)) || ("RO".equals(estadoId)) || ("RR".equals(estadoId)) || (("AP".equals(estadoId)) && (!StringHelper.isBlank(cidadeNome)) && (("Macapá".equals(cidadeNome)) || ("Santana".equals(cidadeNome)))))
		{
			if(inscricaoSuframa.length() == 8)
			{
				inscricaoSuframa = "0" + inscricaoSuframa;
			}
			
			if(inscricaoSuframa.length() != 9)
			{
				return false;
			}
			
			int sum = 0;
			int pound = 9;
			int digit = -1;
			
			for(int i = 0; i < inscricaoSuframa.length() - 1; i++)
			{
				sum += Integer.parseInt(String.valueOf(inscricaoSuframa.charAt(i))) * pound;
				pound--;
			}
			
			digit = 11 - (sum % 11);
			
			if(digit == 11 || digit == 10)
			{
				digit = 0;
			}
			
			String finalDigit = digit + "";
			
			if(!inscricaoSuframa.substring(inscricaoSuframa.length() - 1, inscricaoSuframa.length()).equals(finalDigit))
			{
				return false;
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
}
