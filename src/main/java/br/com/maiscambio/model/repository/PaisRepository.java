package br.com.maiscambio.model.repository;

import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.Pais;
import br.com.maiscambio.model.repository.custom.CustomRepository;

@Repository
public interface PaisRepository extends CustomRepository<Pais, String>
{
	
}
