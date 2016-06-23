package br.com.maiscambio.model.repository;

import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.MetaLogradouro;
import me.gerenciar.util.CustomRepository;

@Repository
public interface MetaLogradouroRepository extends CustomRepository<MetaLogradouro, String>
{
	
}
