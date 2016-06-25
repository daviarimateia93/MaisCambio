package br.com.maiscambio.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Estado;
import br.com.maiscambio.model.entity.primarykey.EstadoPrimaryKey;
import br.com.maiscambio.model.repository.EstadoRepository;

@Service
public class EstadoService implements GlobalBaseEntityService<Estado, EstadoPrimaryKey>
{
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Estado findOne(EstadoPrimaryKey estadoPrimaryKey)
	{
		return estadoRepository.findOne(estadoPrimaryKey);
	}
	
	@Transactional(readOnly = true)
	public Estado findOne(String estadoId, String paisId)
	{
		return findOne(new EstadoPrimaryKey(estadoId, paisId));
	}
	
	@Transactional(readOnly = true)
	public List<Estado> findByPaisId(String paisId)
	{
		return estadoRepository.findByPaisId(paisId);
	}
	
	@Transactional(readOnly = true)
	public List<Estado> findByPaisIdSortedAscByNome(String paisId)
	{
		return estadoRepository.findByPaisIdSortedAscByNome(paisId);
	}
}
