package br.com.maiscambio.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.MetaLogradouro;
import br.com.maiscambio.model.repository.MetaLogradouroRepository;

@Service
public class MetaLogradouroService implements GlobalBaseEntityService<MetaLogradouro, String>
{
	@Autowired
	private MetaLogradouroRepository metaLogradouroRepository;
	
	@Override
	@Transactional(readOnly = true)
	public MetaLogradouro findOne(String cep)
	{
		return metaLogradouroRepository.findOne(cep);
	}
}
