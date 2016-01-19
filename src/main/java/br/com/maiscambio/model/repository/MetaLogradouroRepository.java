package br.com.maiscambio.model.repository;

import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.MetaLogradouro;
import br.com.maiscambio.model.repository.custom.CustomRepository;

@Repository
public interface MetaLogradouroRepository extends CustomRepository<MetaLogradouro, String>
{
	
}
