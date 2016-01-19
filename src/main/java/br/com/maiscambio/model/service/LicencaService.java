package br.com.maiscambio.model.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sun.syndication.io.impl.Base64;

import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.util.Constants;
import br.com.maiscambio.util.DateHelper;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.RsaHelper;

@Service
public class LicencaService implements BaseService
{
	private static final String PATH_PUBLIC_KEY = "public.key";
	
	public static final String EXCEPTION_LICENCA_IS_INVALID = "LICENCA_IS_INVALID";
	public static final String EXCEPTION_LICENCA_IS_EXPIRED = "LICENCA_IS_EXPIRED";
	public static final String EXCEPTION_ESTABELECIMENTO_NOT_FOUND = "ESTABELECIMENTO_NOT_FOUND";
	public static final String EXCEPTION_USUARIO_DOES_NOT_BELONG_TO_ANY_ESTABELECIMENTO = "USUARIO_DOES_NOT_BELONG_TO_ANY_ESTABELECIMENTO";
	public static final String EXCEPTION_LICENCA_DOES_NOT_BELONG_TO_YOU = "LICENCA_DOES_NOT_BELONG_TO_YOU";
	public static final String EXCEPTION_LICENCA_WAS_ALREADY_ACTIVATED = "LICENCA_WAS_ALREADY_ACTIVATED";
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	public void activate(Usuario usuario, String licenca)
	{
		String decrypted = null;
		
		try
		{
			decrypted = decrypt(licenca);
		}
		catch(Exception exception)
		{
			throw new HttpException(EXCEPTION_LICENCA_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
		}
		
		String[] parts = decrypted.split(String.valueOf(Constants.CHAR_SEMICOLON));
		
		int dias = Integer.valueOf(parts[0]);
		String cnpjOrCpfOrIdEstrangeiro = parts[2];
		Calendar data = DateHelper.toCalendar(DateHelper.parse(parts[3]));
		int validade = Integer.valueOf(parts[4]);
		
		data.add(Calendar.DATE, validade);
		
		if(data.getTime().before(new Date()))
		{
			throw new HttpException(EXCEPTION_LICENCA_IS_EXPIRED, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Estabelecimento foundEstabelecimentoByUsuarioId = estabelecimentoService.findByUsuarioId(usuario.getUsuarioId());
		
		if(foundEstabelecimentoByUsuarioId == null)
		{
			throw new HttpException(EXCEPTION_USUARIO_DOES_NOT_BELONG_TO_ANY_ESTABELECIMENTO, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Estabelecimento foundEstabelecimentoByCnpjOrCpfOrIdEstrangeiro = estabelecimentoService.findByCnpjOrCpfOrIdEstrangeiro(cnpjOrCpfOrIdEstrangeiro, cnpjOrCpfOrIdEstrangeiro, cnpjOrCpfOrIdEstrangeiro);
		
		if(foundEstabelecimentoByCnpjOrCpfOrIdEstrangeiro == null)
		{
			throw new HttpException(EXCEPTION_ESTABELECIMENTO_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(!foundEstabelecimentoByCnpjOrCpfOrIdEstrangeiro.getPessoaId().equals(foundEstabelecimentoByUsuarioId.getPessoaId()))
		{
			throw new HttpException(EXCEPTION_LICENCA_DOES_NOT_BELONG_TO_YOU, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(foundEstabelecimentoByCnpjOrCpfOrIdEstrangeiro.getLicencas() != null)
		{
			if(foundEstabelecimentoByCnpjOrCpfOrIdEstrangeiro.getLicencas().contains(licenca))
			{
				throw new HttpException(EXCEPTION_LICENCA_WAS_ALREADY_ACTIVATED, HttpStatus.NOT_ACCEPTABLE);
			}
			
			foundEstabelecimentoByCnpjOrCpfOrIdEstrangeiro.getLicencas().add(licenca);
		}
		else
		{
			List<String> licencas = new ArrayList<>();
			licencas.add(licenca);
			
			foundEstabelecimentoByCnpjOrCpfOrIdEstrangeiro.setLicencas(licencas);
		}
		
		Calendar estabelecimentoNewData = DateHelper.toCalendar(new Date());
		estabelecimentoNewData.add(Calendar.DATE, dias);
		
		foundEstabelecimentoByCnpjOrCpfOrIdEstrangeiro.setData(estabelecimentoNewData.getTime());
		
		estabelecimentoService.saveAsUpdate(foundEstabelecimentoByCnpjOrCpfOrIdEstrangeiro);
	}
	
	private String decrypt(String licenca) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream inputStream = null;
		
		try
		{
			inputStream = new ObjectInputStream(getClass().getClassLoader().getResourceAsStream(PATH_PUBLIC_KEY));
			
			return new String(RsaHelper.decrypt(Base64.decode(licenca.getBytes()), (PublicKey) inputStream.readObject()), Constants.TEXT_CHARSET_UTF_8);
		}
		finally
		{
			if(inputStream != null)
			{
				inputStream.close();
			}
		}
	}
}
