package br.com.maiscambio.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.Estado;
import br.com.maiscambio.model.entity.primarykey.EstadoPrimaryKey;
import br.com.maiscambio.model.repository.custom.CustomRepository;

@Repository
public interface EstadoRepository extends CustomRepository<Estado, EstadoPrimaryKey>
{
	@Query("SELECT e FROM Estado e WHERE e.pais.paisId = :paisId")
	public List<Estado> findByPaisId(@Param("paisId") String paisId);
	
	@Query("SELECT e FROM Estado e WHERE e.pais.paisId = :paisId ORDER BY e.nome ASC")
	public List<Estado> findByPaisIdSortedAscByNome(@Param("paisId") String paisId);
}
