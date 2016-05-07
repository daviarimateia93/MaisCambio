package br.com.maiscambio.model.repository.custom;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.hibernate.internal.util.StringHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.PersistenceProvider;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.jpa.util.Jpa21Utils;

import br.com.maiscambio.model.repository.custom.CustomRepositorySelectorResult.Handmade;
import br.com.maiscambio.model.repository.custom.CustomRepositorySelectorResult.SelectionWrapper;
import br.com.maiscambio.util.Constants;

public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomRepository<T, ID>
{
	public static final String METHOD_NAME_HANDMADE_RESOLVER = "customRepositoryHandmadeResolver";
	
	private final JpaEntityInformation<T, ?> jpaEntityInformation;
	private final EntityManager entityManager;
	private final PersistenceProvider persistenceProvider;
	
	public CustomRepositoryImpl(Class<T> domainClass, EntityManager entityManager)
	{
		this(JpaEntityInformationSupport.getMetadata(domainClass, entityManager), entityManager);
	}
	
	public CustomRepositoryImpl(JpaEntityInformation<T, ?> jpaEntityInformation, EntityManager entityManager)
	{
		super(jpaEntityInformation, entityManager);
		
		this.jpaEntityInformation = jpaEntityInformation;
		this.entityManager = entityManager;
		this.persistenceProvider = PersistenceProvider.fromEntityManager(entityManager);
	}
	
	public JpaEntityInformation<T, ?> getJpaEntityInformation()
	{
		return jpaEntityInformation;
	}
	
	public EntityManager getEntityManager()
	{
		return entityManager;
	}
	
	public PersistenceProvider getPersistenceProvider()
	{
		return persistenceProvider;
	}
	
	@Override
	public List<Map<String, Object>> findAll(CustomRepositorySelector<T> customRepositorySelector, Specification<T> specification)
	{
		return findAll(customRepositorySelector, specification, null).getContent();
	}
	
	@Override
	public Page<Map<String, Object>> findAll(CustomRepositorySelector<T> customRepositorySelector, Specification<T> specification, Pageable pageable)
	{
		Sort sort = pageable == null ? null : pageable.getSort();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		
		Root<T> root = criteriaQuery.from(getDomainClass());
		
		CustomRepositorySelectorResult customRepositorySelectorResult = customRepositorySelector.select(root);
		List<SelectionWrapper<?>> selectionWrappers = customRepositorySelectorResult.getSelectionWrappers();
		
		List<Selection<?>> selections = new ArrayList<>();
		List<String> aliases = new ArrayList<>();
		
		if(selectionWrappers != null)
		{
			for(SelectionWrapper<?> selectionWrapper : selectionWrappers)
			{
				selections.add(selectionWrapper.getSelection());
				aliases.add(selectionWrapper.getCustomAlias());
			}
		}
		
		criteriaQuery.multiselect(selections);
		
		if(specification != null)
		{
			Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
			
			if(predicate != null)
			{
				criteriaQuery.where(predicate);
			}
		}
		
		if(sort != null)
		{
			criteriaQuery.orderBy(toOrders(sort, root, criteriaBuilder));
		}
		
		TypedQuery<Object> typedQuery = entityManager.createQuery(criteriaQuery);
		
		if(getRepositoryMethodMetadata() != null)
		{
			LockModeType type = getRepositoryMethodMetadata().getLockModeType();
			typedQuery = type == null ? typedQuery : typedQuery.setLockMode(type);
			
			for(Entry<String, Object> hint : getRepositoryMethodMetadata().getQueryHints().entrySet())
			{
				typedQuery.setHint(hint.getKey(), hint.getValue());
			}
			
			typedQuery = Jpa21Utils.tryConfigureFetchGraph(entityManager, typedQuery, getRepositoryMethodMetadata().getEntityGraph());
		}
		
		if(pageable != null)
		{
			typedQuery.setFirstResult(pageable.getOffset());
			typedQuery.setMaxResults(pageable.getPageSize());
		}
		
		List<Object> listContent = typedQuery.getResultList();
		List<Map<String, Object>> content = new ArrayList<>();
		
		if(listContent != null)
		{
			for(int i = 0; i < listContent.size(); i++)
			{
				Map<String, Object> map = new HashMap<>();
				
				if(listContent.get(i) instanceof Object[])
				{
					Object[] array = (Object[]) listContent.get(i);
					
					for(int j = 0; j < array.length; j++)
					{
						String alias = aliases.get(j).split(String.valueOf(Constants.CHAR_UNDERLINE))[0];
						
						populate(map, alias, array[j]);
					}
				}
				else
				{
					populate(map, aliases.get(0).split(String.valueOf(Constants.CHAR_UNDERLINE))[0], listContent.get(i));
				}
				
				for(Handmade handmade : customRepositorySelectorResult.getHandmades())
				{
					if(StringHelper.isNotEmpty(handmade.getFilterValue()))
					{
						String[] fragments = handmade.getFilterValue().split(String.valueOf(Constants.CHAR_AMP));
						
						Map<String, String> values = new HashMap<>();
						
						for(String fragment : fragments)
						{
							int equalsPosition = fragment.indexOf(String.valueOf(Constants.CHAR_EQUALS));
							
							String name = fragment.substring(0, equalsPosition);
							String value = fragment.substring(equalsPosition + 1, fragment.length());
							
							if(StringHelper.isEmpty(value))
							{
								values.put(name, String.valueOf(search(map, name)));
							}
							else
							{
								values.put(name, value);
							}
						}
						
						try
						{
							Method method = handmade.getType().getMethod(METHOD_NAME_HANDMADE_RESOLVER, String.class, Map.class);
							
							Object result = method.invoke(null, handmade.getFieldName(), values);
							
							populate(map, handmade.getFilterName(), result);
						}
						catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception)
						{
							
						}
					}
				}
				
				content.add(map);
			}
		}
		
		if(pageable == null)
		{
			return new PageImpl<Map<String, Object>>(content);
		}
		else
		{
			Long total = QueryUtils.executeCountQuery(getCountQuery(specification));
			content = total > pageable.getOffset() ? content : Collections.<Map<String, Object>> emptyList();
			
			return new PageImpl<Map<String, Object>>(content, pageable, total);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object search(Map<String, Object> map, String alias)
	{
		for(String fragment : alias.split(Constants.TEXT_PATTERN_DOT))
		{
			if(map.containsKey(fragment))
			{
				if(map.get(fragment) instanceof Map)
				{
					map = (Map) map.get(fragment);
				}
				else
				{
					return map.get(fragment);
				}
			}
			else
			{
				break;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> populate(Map<String, Object> map, String alias, Object value)
	{
		String[] fragments = alias.split(Constants.TEXT_PATTERN_DOT);
		
		if(fragments.length == 1)
		{
			map.put(fragments[0], value);
		}
		else
		{
			Map<String, Object> auxMap = map;
			
			for(int k = 0; k < fragments.length; k++)
			{
				if(k == fragments.length - 1)
				{
					auxMap.put(fragments[k], value);
				}
				else
				{
					if(auxMap.containsKey(fragments[k]))
					{
						auxMap = (Map) auxMap.get(fragments[k]);
						
						continue;
					}
					
					Map<String, Object> nestedAuxMap = new HashMap<>();
					
					auxMap.put(fragments[k], nestedAuxMap);
					
					auxMap = nestedAuxMap;
				}
			}
		}
		
		return map;
	}
}
