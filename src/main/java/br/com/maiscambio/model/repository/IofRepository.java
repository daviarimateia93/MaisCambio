package br.com.maiscambio.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.maiscambio.model.entity.Iof;
import br.com.maiscambio.model.repository.custom.CustomRepository;

@Repository
public interface IofRepository extends CustomRepository<Iof, Long>
{
	@Query("SELECT i FROM Iof i WHERE i.iofId = (SELECT MAX(i.iofId) FROM Iof i)")
	public Iof findLast();
}
