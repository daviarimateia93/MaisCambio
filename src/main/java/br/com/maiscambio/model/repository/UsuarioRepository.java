package br.com.maiscambio.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.repository.custom.CustomRepository;

@Repository
public interface UsuarioRepository extends CustomRepository<Usuario, Long>
{
	@Query("SELECT u FROM Usuario u WHERE LOWER(u.apelido) = LOWER(:apelido)")
	public Usuario findByApelido(@Param("apelido") String apelido);
	
	@Query("SELECT us FROM Pessoa p JOIN p.usuarios us WHERE p.pessoaId = :pessoaId")
	public List<Usuario> findByPessoaId(@Param("pessoaId") Integer pessoaId);
}
