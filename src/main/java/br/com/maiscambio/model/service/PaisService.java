package br.com.maiscambio.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Pais;
import br.com.maiscambio.model.repository.PaisRepository;

@Service
public class PaisService implements GlobalBaseEntityService<Pais, String>
{
	@Autowired
	private PaisRepository paisRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Pais findOne(String paisId)
	{
		return paisRepository.findOne(paisId);
	}
}
