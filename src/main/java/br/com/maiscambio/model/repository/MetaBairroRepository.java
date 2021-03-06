package br.com.maiscambio.model.repository;

import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.MetaBairro;
import br.com.maiscambio.model.repository.custom.CustomRepository;

@Repository
public interface MetaBairroRepository extends CustomRepository<MetaBairro, Long>
{
	
}
