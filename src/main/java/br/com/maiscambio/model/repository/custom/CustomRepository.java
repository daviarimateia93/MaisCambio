package br.com.maiscambio.model.repository.custom;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>
{
	public List<Map<String, Object>> findAll(CustomRepositorySelector<T> customRepositorySelector, Specification<T> specification);
	
	public Page<Map<String, Object>> findAll(CustomRepositorySelector<T> customRepositorySelector, Specification<T> specification, Pageable pageable);
}
