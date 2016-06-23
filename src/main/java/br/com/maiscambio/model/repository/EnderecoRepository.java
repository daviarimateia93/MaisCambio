package br.com.maiscambio.model.repository;

import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.Endereco;
import me.gerenciar.util.CustomRepository;

@Repository
public interface EnderecoRepository extends CustomRepository<Endereco, Long>
{

}
