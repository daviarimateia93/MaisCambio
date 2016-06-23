package br.com.maiscambio.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.Cidade;
import me.gerenciar.util.CustomRepository;

@Repository
public interface CidadeRepository extends CustomRepository<Cidade, Long>
{
	@Query("SELECT c FROM Cidade c WHERE c.estado.estadoId = :estadoId AND c.estado.pais.paisId = :paisId")
	public List<Cidade> findByEstadoIdAndPaisId(@Param("estadoId") String estadoId, @Param("paisId") String paisId);
	
	@Query("SELECT c FROM Cidade c WHERE c.estado.estadoId = :estadoId AND c.estado.pais.paisId = :paisId ORDER BY c.nome ASC")
	public List<Cidade> findByEstadoIdAndPaisIdSortedAscByNome(@Param("estadoId") String estadoId, @Param("paisId") String paisId);
	
	@Query("SELECT c FROM Cidade c WHERE c.nome = :nome AND c.estado.estadoId = :estadoId AND c.estado.pais.paisId = :paisId")
	public Cidade findByNomeAndEstadoIdAndPaisId(@Param("nome") String nome, @Param("estadoId") String estadoId, @Param("paisId") String paisId);
}
