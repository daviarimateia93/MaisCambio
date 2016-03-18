package br.com.maiscambio.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.maiscambio.model.entity.Taxa;
import br.com.maiscambio.model.entity.Taxa.Finalidade;
import br.com.maiscambio.model.entity.Taxa.Moeda;
import br.com.maiscambio.model.repository.custom.CustomRepository;

public interface TaxaRepository extends CustomRepository<Taxa, Long>
{
	@Query("SELECT t FROM Taxa t WHERE t.estabelecimento.pessoaId = :estabelecimentoPessoaId AND t.moeda = :moeda AND t.finalidade = :finalidade AND t.taxaId = (SELECT MAX(t.taxaId) FROM Taxa t WHERE t.estabelecimento.pessoaId = :estabelecimentoPessoaId AND t.moeda = :moeda AND t.finalidade = :finalidade)")
	public Taxa findLastByEstabelecimentoPessoaIdAndMoedaAndFinalidade(@Param("estabelecimentoPessoaId") Long estabelecimentoPessoaId, @Param("moeda") Moeda moeda, @Param("finalidade") Finalidade finalidade);
}
