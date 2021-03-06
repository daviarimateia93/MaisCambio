package br.com.maiscambio.model.repository.custom;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class CustomRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I>
{
	@SuppressWarnings("rawtypes")
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager)
	{
		return new CustomRepositoryFactory(entityManager);
	}
	
	private static class CustomRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory
	{
		private EntityManager entityManager;
		
		public CustomRepositoryFactory(EntityManager entityManager)
		{
			super(entityManager);
			
			this.entityManager = entityManager;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected Object getTargetRepository(RepositoryMetadata repositoryMetadata)
		{
			return new CustomRepositoryImpl<T, I>((Class<T>) repositoryMetadata.getDomainType(), entityManager);
		}
		
		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata repositoryMetadata)
		{
			return CustomRepositoryImpl.class;
		}
	}
}
