package br.com.maiscambio.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.repository.custom.CustomRepository;

@Repository
public interface EstabelecimentoRepository extends CustomRepository<Estabelecimento, Long>
{
	@Query("SELECT e FROM Estabelecimento e JOIN e.usuarios us WHERE us.usuarioId = :usuarioId")
	public Estabelecimento findByUsuarioId(@Param("usuarioId") Long usuarioId);
	
	@Query("SELECT e FROM Estabelecimento e WHERE e.cnpj = :cnpj OR e.cpf = :cpf OR e.idEstrangeiro = :idEstrangeiro")
	public Estabelecimento findByCnpjOrCpfOrIdEstrangeiro(@Param("cnpj") String cnpj, @Param("cpf") String cpf, @Param("idEstrangeiro") String idEstrangeiro);
}
