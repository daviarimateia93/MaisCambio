package br.com.maiscambio.model.repository.custom;

import javax.persistence.criteria.Root;

public abstract class CustomRepositorySelector<T>
{
	public abstract CustomRepositorySelectorResult select(Root<T> root);
}
