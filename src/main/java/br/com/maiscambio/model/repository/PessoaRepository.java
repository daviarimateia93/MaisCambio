package br.com.maiscambio.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.Pessoa;
import br.com.maiscambio.model.repository.custom.CustomRepository;

@Repository
public interface PessoaRepository extends CustomRepository<Pessoa, Long>
{
	@Query("SELECT p FROM Pessoa p JOIN p.usuarios us WHERE us.usuarioId = :usuarioId")
	public Pessoa findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
