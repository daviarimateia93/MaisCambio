package br.com.maiscambio.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.model.entity.Cidade;
import br.com.maiscambio.model.repository.CidadeRepository;

@Service
public class CidadeService implements GlobalBaseEntityService<Cidade, Long>
{
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Cidade findOne(Long cidadeId)
	{
		return cidadeRepository.findOne(cidadeId);
	}
	
	@Transactional(readOnly = true)
	public List<Cidade> findByEstadoIdAndPaisId(String estadoId, String paisId)
	{
		return cidadeRepository.findByEstadoIdAndPaisId(estadoId, paisId);
	}
	
	@Transactional(readOnly = true)
	public List<Cidade> findByEstadoIdAndPaisIdSortedAscByNome(String estadoId, String paisId)
	{
		return cidadeRepository.findByEstadoIdAndPaisIdSortedAscByNome(estadoId, paisId);
	}
	
	@Transactional(readOnly = true)
	public Cidade findByNomeAndEstadoIdAndPaisId(String nome, String estadoId, String paisId)
	{
		return cidadeRepository.findByNomeAndEstadoIdAndPaisId(nome, estadoId, paisId);
	}
}
