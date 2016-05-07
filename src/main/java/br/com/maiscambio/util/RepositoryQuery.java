package br.com.maiscambio.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.cassandra.cql3.CQL3Type.Collection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;

import br.com.maiscambio.model.entity.BaseEntity;
import br.com.maiscambio.model.repository.custom.CustomRepositorySelector;
import br.com.maiscambio.model.repository.custom.CustomRepositorySelectorResult;
import br.com.maiscambio.model.repository.custom.CustomRepositorySelectorResult.Handmade;
import br.com.maiscambio.model.repository.custom.CustomRepositorySelectorResult.SelectionWrapper;

public class RepositoryQuery<T>
{
	public static final String REPOSITORY_QUERY_STARTER_INDEX_PARAMETER = "repositoryQueryStarterIndex";
	public static final String REPOSITORY_QUERY_END_INDEX_PARAMETER = "repositoryQueryEndIndex";
	public static final String REPOSITORY_QUERY_FILTER_GLUE_OR = "OR";
	public static final String REPOSITORY_QUERY_FILTER_GLUE_AND = "AND";
	public static final String REPOSITORY_QUERY_FILTER_ORDER_ASC = "ASC";
	public static final String REPOSITORY_QUERY_FILTER_ORDER_DESC = "DESC";
	public static final String REPOSITORY_QUERY_FILTER_OPERATOR_EQUALS = "EQUALS";
	public static final String REPOSITORY_QUERY_FILTER_OPERATOR_NOT_EQUALS = "NOT_EQUALS";
	public static final String REPOSITORY_QUERY_FILTER_OPERATOR_GREATER = "GREATER";
	public static final String REPOSITORY_QUERY_FILTER_OPERATOR_GREATER_OR_EQUALS = "GREATER_OR_EQUALS";
	public static final String REPOSITORY_QUERY_FILTER_OPERATOR_LESS = "LESS";
	public static final String REPOSITORY_QUERY_FILTER_OPERATOR_LESS_OR_EQUALS = "LESS_OR_EQUALS";
	public static final String REPOSITORY_QUERY_FILTER_OPERATOR_NOT_LIKE = "NOT_LIKE";
	public static final String REPOSITORY_QUERY_FILTER_OPERATOR_LIKE = "LIKE";
	public static final String REPOSITORY_QUERY_FILTER_OPERATOR_ILIKE = "ILIKE";
	public static final String REPOSITORY_QUERY_FILTER_PARAMETER = "repositoryQueryFilter";
	public static final String REPOSITORY_QUERY_FILTER_PARAMETER_NAME = "name";
	public static final String REPOSITORY_QUERY_FILTER_PARAMETER_ORDER = "order";
	public static final String REPOSITORY_QUERY_FILTER_PARAMETER_OPERATOR = "operator";
	public static final String REPOSITORY_QUERY_FILTER_PARAMETER_GLUE = "glue";
	public static final String REPOSITORY_QUERY_FILTER_PARAMETER_VALUE = "value";
	public static final String REPOSITORY_QUERY_FILTER_PARAMETER_NULL_SUPPRESSED = "nullSuppressed";
	public static final String REPOSITORY_QUERY_FILTER_PARAMETER_EMPTY_SUPPRESSED = "emptySuppressed";
	public static final String REPOSITORY_QUERY_FILTER_PARAMETER_FIXED = "fixed";
	
	public static final String EXCEPTION_REPOSITORY_QUERY_STARTER_INDEX_PARAMETER_CAN_NOT_BE_NULL = "REPOSITORY_QUERY_STARTER_INDEX_PARAMETER_CAN_NOT_BE_NULL";
	public static final String EXCEPTION_REPOSITORY_QUERY_STARTER_INDEX_PARAMETER_MUST_BE_NUMERIC = "REPOSITORY_QUERY_STARTER_INDEX_PARAMETER_MUST_BE_NUMERIC";
	public static final String EXCEPTION_REPOSITORY_QUERY_END_INDEX_PARAMETER_MUST_BE_NUMERIC = "REPOSITORY_QUERY_END_INDEX_PARAMETER_MUST_BE_NUMERIC";
	public static final String EXCEPTION_REPOSITORY_QUERY_END_INDEX_PARAMETER_CAN_NOT_BE_NULL = "REPOSITORY_QUERY_END_INDEX_PARAMETER_CAN_NOT_BE_NULL";
	public static final String EXCEPTION_REPOSITORY_QUERY_FILTER_PARAMETER_NAME_CAN_NOT_BE_EMPTY = "REPOSITORY_QUERY_FILTER_PARAMETER_NAME_CAN_NOT_BE_EMPTY";
	public static final String EXCEPTION_REPOSITORY_QUERY_FILTER_PARAMETER_ORDER_NOT_ACCEPTABLE = "REPOSITORY_QUERY_FILTER_PARAMETER_ORDER_NOT_ACCEPTABLE";
	public static final String EXCEPTION_REPOSITORY_QUERY_FILTER_PARAMETER_OPERATOR_NOT_ACCEPTABLE = "REPOSITORY_QUERY_FILTER_PARAMETER_OPERATOR_NOT_ACCEPTABLE";
	public static final String EXCEPTION_REPOSITORY_QUERY_FILTER_PARAMETER_GLUE_NOT_ACCEPTABLE = "REPOSITORY_QUERY_FILTER_PARAMETER_GLUE_NOT_ACCEPTABLE";
	public static final String EXCEPTION_REPOSITORY_QUERY_LIMIT_IS_OVERPOWERING = "REPOSITORY_QUERY_LIMIT_IS_OVERPOWERING";
	
	public static final Long REPOSITORY_QUERY_LIMIT = 500L;
	
	public static class Filter
	{
		private static final String NULL_VALUE = "repositoryQuery.filter.null";
		
		public static enum Operator
		{
			EQUALS, NOT_EQUALS, GREATER, GREATER_OR_EQUALS, LESS, LESS_OR_EQUALS, NOT_LIKE, LIKE, ILIKE
		}
		
		public static enum Order
		{
			ASC, DESC
		}
		
		public static enum Glue
		{
			OR, AND
		}
		
		private String name;
		private String value;
		private Operator operator;
		private Order order;
		private Glue glue;
		private Boolean nullSuppressed;
		private Boolean emptySuppressed;
		private boolean fixed;
		
		public Filter()
		{
			nullSuppressed = false;
			emptySuppressed = false;
		}
		
		public String getName()
		{
			return name;
		}
		
		public void setName(String name)
		{
			this.name = name;
		}
		
		public String getValue()
		{
			return value;
		}
		
		public void setValue(String value)
		{
			if(!isNullValue(value))
			{
				this.value = value;
			}
		}
		
		public Operator getOperator()
		{
			return operator;
		}
		
		public void setOperator(Operator operator)
		{
			this.operator = operator;
		}
		
		public Order getOrder()
		{
			return order;
		}
		
		public void setOrder(Order order)
		{
			this.order = order;
		}
		
		public Glue getGlue()
		{
			return glue;
		}
		
		public void setGlue(Glue glue)
		{
			this.glue = glue;
		}
		
		public Boolean isNullSuppressed()
		{
			return nullSuppressed;
		}
		
		public void setNullSuppressed(Boolean nullSuppressed)
		{
			this.nullSuppressed = nullSuppressed;
		}
		
		public Boolean isEmptySuppressed()
		{
			return emptySuppressed;
		}
		
		public void setEmptySuppressed(Boolean emptySuppressed)
		{
			this.emptySuppressed = emptySuppressed;
		}
		
		public boolean isFixed()
		{
			return fixed;
		}
		
		public void setFixed(boolean fixed)
		{
			this.fixed = fixed;
		}
		
		public boolean shouldSuppress()
		{
			return (nullSuppressed && value == null) || (emptySuppressed && Constants.TEXT_EMPTY.equals(value));
		}
		
		private static boolean isNullValue(String value)
		{
			return NULL_VALUE.equals(value);
		}
	}
	
	private Class<T> type;
	private HttpServletRequest request;
	private Long starterIndex;
	private Long endIndex;
	private Filter[] filters;
	
	private RepositoryQuery(Class<T> type)
	{
		this.type = type;
	}
	
	public HttpServletRequest getRequest()
	{
		return request;
	}
	
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}
	
	public Long getStarterIndex()
	{
		return starterIndex;
	}
	
	public void setStarterIndex(Long starterIndex)
	{
		this.starterIndex = starterIndex;
	}
	
	public Long getEndIndex()
	{
		return endIndex;
	}
	
	public void setEndIndex(Long endIndex)
	{
		this.endIndex = endIndex;
	}
	
	public Filter[] getFilters()
	{
		return filters;
	}
	
	public void setFilters(Filter[] filters)
	{
		this.filters = filters;
	}
	
	public CustomRepositorySelector<T> toCustomRepositorySelector()
	{
		return new CustomRepositorySelector<T>()
		{
			@Override
			public CustomRepositorySelectorResult select(Root<T> root)
			{
				List<SelectionWrapper<?>> selectionWrappers = new ArrayList<>();
				List<Handmade> handmades = new ArrayList<>();
				
				RepositoryQuery.Filter[] filters = getFromRequest(type, request).getFilters();
				
				for(int i = 0; i < filters.length; i++)
				{
					Class<?> fieldType = getSpecificationFieldType(root.getJavaType(), filters[i].getName());
					String fieldName = getSpecificationFieldName(fieldType, filters[i].getName());
					
					try
					{
						selectionWrappers.add(new SelectionWrapper<>(filters[i].getName() + String.valueOf(Constants.CHAR_UNDERLINE) + i, getSpecificationPath(root, filters[i].getName(), (filters[i].isNullSuppressed() || filters[i].isEmptySuppressed())).get(fieldName)));
					}
					catch(Exception exception)
					{
						Map<String, Class<?>> fieldsType = getSpecificationFieldsType(root.getJavaType(), filters[i].getName());
						
						Class<?> type = null;
						
						if(fieldsType != null)
						{
							int counter = 1;
							
							for(Map.Entry<String, Class<?>> entry : fieldsType.entrySet())
							{
								if(counter == fieldsType.size())
								{
									break;
								}
								
								type = entry.getValue();
								
								counter++;
							}
						}
						
						if(type == null)
						{
							type = root.getJavaType();
						}
						
						handmades.add(new Handmade(type, fieldName, filters[i].getName(), filters[i].getValue()));
					}
				}
				
				return new CustomRepositorySelectorResult(selectionWrappers, handmades);
			}
		};
	}
	
	public Pageable toPageable()
	{
		RepositoryQuery<T> repositoryQuery = getFromRequest(type, request);
		
		long size = repositoryQuery.getEndIndex() - repositoryQuery.getStarterIndex();
		long page = ((repositoryQuery.getEndIndex() + 1) / size) - 1;
		
		List<String> idFields = new ArrayList<>();
		
		for(Field field : ReflectionHelper.getFields(type))
		{
			if(field.isAnnotationPresent(Id.class))
			{
				idFields.add(field.getName());
			}
		}
		
		List<Integer> removeIndexes = new ArrayList<>();
		
		List<Order> orders = new ArrayList<>();
		
		for(RepositoryQuery.Filter filter : repositoryQuery.getFilters())
		{
			if(filter.getOrder() != null)
			{
				switch(filter.getOrder())
				{
					case DESC:
						orders.add(new Order(Direction.DESC, filter.getName()));
						break;
					
					case ASC:
						orders.add(new Order(Direction.ASC, filter.getName()));
						break;
				}
				
				for(int i = 0; i < idFields.size(); i++)
				{
					if(idFields.get(i).equals(filter.getName()))
					{
						removeIndexes.add(i);
					}
				}
			}
		}
		
		for(Integer removeIndex : removeIndexes)
		{
			idFields.remove(removeIndex);
		}
		
		for(String idField : idFields)
		{
			orders.add(new Order(Direction.ASC, idField));
		}
		
		return orders.isEmpty() ? new PageRequest((int) page, (int) size) : new PageRequest((int) page, (int) size, new Sort(orders));
	}
	
	public Specification<T> toSpecification()
	{
		final RepositoryQuery<T> repositoryQuery = this;
		
		if(repositoryQuery.getFilters().length == 0)
		{
			return null;
		}
		else
		{
			Specifications<T> fixedSpecifications = Specifications.where(null);
			Specifications<T> specifications = Specifications.where(null);
			
			for(RepositoryQuery.Filter filter : repositoryQuery.getFilters())
			{
				if(!filter.shouldSuppress())
				{
					Specification<T> specification = toQuerySpecification(filter);
					
					switch(filter.getGlue())
					{
						case OR:
						{
							if(filter.isFixed())
							{
								fixedSpecifications = fixedSpecifications.or(specification);
							}
							else
							{
								specifications = specifications.or(specification);
							}
							
							break;
						}
						
						case AND:
						{
							if(filter.isFixed())
							{
								fixedSpecifications = fixedSpecifications.and(specification);
							}
							else
							{
								specifications = specifications.and(specification);
							}
							
							break;
						}
					}
				}
			}
			
			return fixedSpecifications.and(specifications);
		}
	}
	
	private Map<String, Class<?>> getSpecificationFieldsType(Class<?> baseType, String attributePath)
	{
		Map<String, Class<?>> fieldsType = new LinkedHashMap<>();
		
		String[] fieldNames = attributePath.indexOf(String.valueOf(Constants.CHAR_DOT)) > -1 ? attributePath.split(Constants.TEXT_PATTERN_DOT) : new String[] { attributePath };
		
		for(int i = 0; i < fieldNames.length; i++)
		{
			for(Field field : ReflectionHelper.getFields(baseType))
			{
				field.setAccessible(true);
				
				if(field.getName().equals(fieldNames[i]))
				{
					fieldsType.put(String.valueOf(i) + String.valueOf(Constants.CHAR_DOT) + fieldNames[i], field.getType());
					
					baseType = field.getType();
				}
			}
		}
		
		return fieldsType;
	}
	
	private Class<?> getSpecificationFieldType(Class<?> baseType, String attributePath)
	{
		Map<String, Class<?>> fieldsType = getSpecificationFieldsType(baseType, attributePath);
		
		Class<?> fieldType = null;
		
		for(Map.Entry<String, Class<?>> entry : fieldsType.entrySet())
		{
			fieldType = entry.getValue();
		}
		
		return fieldType;
	}
	
	private String getSpecificationFieldName(Class<?> baseType, String attributePath)
	{
		int index = attributePath.lastIndexOf(String.valueOf(Constants.CHAR_DOT));
		
		return index > -1 ? attributePath.substring(index + 1) : attributePath;
	}
	
	private Object getSpecificationFieldTypeValue(Class<?> fieldType, String value) throws ParseException
	{
		if(Date.class.isAssignableFrom(fieldType))
		{
			return new SimpleDateFormat(DateHelper.DATE_TIME_FORMAT_PATTERN).parse(value);
		}
		else if(BigDecimal.class.isAssignableFrom(fieldType))
		{
			return new BigDecimal(value);
		}
		else
		{
			return ReflectionHelper.generateBasicValue(value, fieldType);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Path<T> getSpecificationPath(Path<T> root, String attributePath, boolean leftJoin)
	{
		Map<String, Class<?>> fieldsType = getSpecificationFieldsType(root.getJavaType(), attributePath);
		
		// find the next to last
		int counter = 1;
		
		for(Map.Entry<String, Class<?>> entry : fieldsType.entrySet())
		{
			if(counter == fieldsType.size())
			{
				break;
			}
			
			String fixedKey = entry.getKey().split(Constants.TEXT_PATTERN_DOT)[1];
			
			if((Collection.class.isAssignableFrom(entry.getValue()) || BaseEntity.class.isAssignableFrom(entry.getValue())) && (root instanceof Root || root instanceof Join))
			{
				if(root instanceof Root)
				{
					root = ((Root) root).join(fixedKey, leftJoin ? JoinType.LEFT : JoinType.INNER);
				}
				else if(root instanceof Join)
				{
					root = ((Join) root).join(fixedKey, leftJoin ? JoinType.LEFT : JoinType.INNER);
				}
			}
			else
			{
				root = root.get(fixedKey);
			}
			
			counter++;
		}
		
		return root;
	}
	
	private Specification<T> toQuerySpecification(final RepositoryQuery.Filter filter)
	{
		return new Specification<T>()
		{
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
			{
				Predicate predicate = null;
				
				try
				{
					Class<?> fieldType = getSpecificationFieldType(root.getJavaType(), filter.getName());
					String fieldName = getSpecificationFieldName(fieldType, filter.getName());
					Object fieldValue = getSpecificationFieldTypeValue(fieldType, filter.getValue());
					Path<T> path = getSpecificationPath(root, filter.getName(), (filter.isNullSuppressed() || filter.isEmptySuppressed()));
					
					switch(filter.getOperator())
					{
						case EQUALS:
							predicate = criteriaBuilder.equal(path.get(fieldName).as(fieldType), fieldValue);
							break;
						
						case NOT_EQUALS:
							predicate = criteriaBuilder.notEqual(path.get(fieldName).as(fieldType), fieldValue);
							break;
						
						case GREATER:
							if(fieldValue instanceof Date)
								predicate = criteriaBuilder.greaterThan(path.<Date> get(fieldName), (Date) fieldValue);
							else
								predicate = criteriaBuilder.gt(path.<Number> get(fieldName), NumberHelper.valueOf(filter.getValue()));
							break;
						
						case GREATER_OR_EQUALS:
							if(fieldValue instanceof Date)
								predicate = criteriaBuilder.greaterThanOrEqualTo(path.<Date> get(fieldName), (Date) fieldValue);
							else
								predicate = criteriaBuilder.ge(path.<Number> get(fieldName), NumberHelper.valueOf(filter.getValue()));
							break;
						
						case LESS:
							if(fieldValue instanceof Date)
								predicate = criteriaBuilder.lessThan(path.<Date> get(fieldName), (Date) fieldValue);
							else
								predicate = criteriaBuilder.lt(path.<Number> get(fieldName), NumberHelper.valueOf(filter.getValue()));
							break;
						
						case LESS_OR_EQUALS:
							if(fieldValue instanceof Date)
								predicate = criteriaBuilder.lessThanOrEqualTo(path.<Date> get(fieldName), (Date) fieldValue);
							else
								predicate = criteriaBuilder.le(path.<Number> get(fieldName), NumberHelper.valueOf(filter.getValue()));
							break;
						
						case NOT_LIKE:
							predicate = criteriaBuilder.notLike(path.<String> get(fieldName), filter.getValue());
							break;
						
						case LIKE:
							predicate = criteriaBuilder.like(path.<String> get(fieldName), filter.getValue());
							break;
						
						case ILIKE:
							predicate = criteriaBuilder.like(criteriaBuilder.lower(path.<String> get(fieldName)), (filter.getValue() != null ? filter.getValue().toLowerCase() : null));
							break;
					}
				}
				catch(Exception exception)
				{
					// lets ignore the parser exception
				}
				
				return predicate;
			}
		};
	}
	
	public static <T> RepositoryQuery<T> getFromRequest(Class<T> type, HttpServletRequest request)
	{
		// Request Parameters - Example
		// repositoryQueryStarterIndex = 0
		// repositoryQueryEndIndex = 10
		// repositoryQueryFilter =
		// name=nome;order=ASC;operator=LIKE;glue=OR;value=%Cliente%;[nullSuppressed=false|true][emptySuppressed=false|true];
		
		// validation starter and end indexes
		String starterIndexParameter = request.getParameter(REPOSITORY_QUERY_STARTER_INDEX_PARAMETER);
		String endIndexParameter = request.getParameter(REPOSITORY_QUERY_END_INDEX_PARAMETER);
		
		if(starterIndexParameter == null)
		{
			throw new HttpException(EXCEPTION_REPOSITORY_QUERY_STARTER_INDEX_PARAMETER_CAN_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(starterIndexParameter.matches(Constants.TEXT_PATTERN_NON_NUMERIC))
		{
			throw new HttpException(EXCEPTION_REPOSITORY_QUERY_STARTER_INDEX_PARAMETER_MUST_BE_NUMERIC, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endIndexParameter == null)
		{
			throw new HttpException(EXCEPTION_REPOSITORY_QUERY_END_INDEX_PARAMETER_CAN_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(endIndexParameter.matches(Constants.TEXT_PATTERN_NON_NUMERIC))
		{
			throw new HttpException(EXCEPTION_REPOSITORY_QUERY_END_INDEX_PARAMETER_MUST_BE_NUMERIC, HttpStatus.NOT_ACCEPTABLE);
		}
		
		// indexes
		long starterIndex = Long.valueOf(starterIndexParameter);
		long endIndex = Long.valueOf(endIndexParameter);
		
		if(endIndex - starterIndex > REPOSITORY_QUERY_LIMIT)
		{
			throw new HttpException(EXCEPTION_REPOSITORY_QUERY_LIMIT_IS_OVERPOWERING, HttpStatus.NOT_ACCEPTABLE);
		}
		
		// filters
		List<RepositoryQuery.Filter> filters = new ArrayList<>();
		
		String[] filtersLines = request.getParameterValues(REPOSITORY_QUERY_FILTER_PARAMETER);
		
		if(filtersLines != null)
		{
			for(String filterLine : filtersLines)
			{
				filters.add(parseFilterLine(filterLine));
			}
		}
		
		RepositoryQuery<T> repositoryQuery = new RepositoryQuery<>(type);
		repositoryQuery.setRequest(request);
		repositoryQuery.setStarterIndex(starterIndex);
		repositoryQuery.setEndIndex(endIndex);
		repositoryQuery.setFilters(filters.toArray(new RepositoryQuery.Filter[filters.size()]));
		
		return repositoryQuery;
	}
	
	public static Filter parseFilterLine(String filterLine)
	{
		String[] fragments = filterLine.split(String.valueOf(Constants.CHAR_SEMICOLON));
		
		RepositoryQuery.Filter filter = new RepositoryQuery.Filter();
		
		for(String fragment : fragments)
		{
			// parse
			int equalsPosition = fragment.indexOf(String.valueOf(Constants.CHAR_EQUALS));
			
			String name = fragment.substring(0, equalsPosition);
			String value = fragment.substring(equalsPosition + 1, fragment.length());
			
			switch(name)
			{
				case REPOSITORY_QUERY_FILTER_PARAMETER_NAME:
				{
					if(StringUtils.isEmpty(value))
					{
						throw new HttpException(EXCEPTION_REPOSITORY_QUERY_FILTER_PARAMETER_NAME_CAN_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
					}
					
					filter.setName(value);
					
					break;
				}
				
				case REPOSITORY_QUERY_FILTER_PARAMETER_ORDER:
				{
					switch(value)
					{
						case REPOSITORY_QUERY_FILTER_ORDER_ASC:
							filter.setOrder(Filter.Order.ASC);
							break;
						
						case REPOSITORY_QUERY_FILTER_ORDER_DESC:
							filter.setOrder(Filter.Order.DESC);
							break;
						
						default:
							throw new HttpException(EXCEPTION_REPOSITORY_QUERY_FILTER_PARAMETER_ORDER_NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE);
					}
					
					break;
				}
				
				case REPOSITORY_QUERY_FILTER_PARAMETER_OPERATOR:
				{
					switch(value)
					{
						case REPOSITORY_QUERY_FILTER_OPERATOR_EQUALS:
							filter.setOperator(Filter.Operator.EQUALS);
							break;
						
						case REPOSITORY_QUERY_FILTER_OPERATOR_NOT_EQUALS:
							filter.setOperator(Filter.Operator.NOT_EQUALS);
							break;
						
						case REPOSITORY_QUERY_FILTER_OPERATOR_GREATER:
							filter.setOperator(Filter.Operator.GREATER);
							break;
						
						case REPOSITORY_QUERY_FILTER_OPERATOR_GREATER_OR_EQUALS:
							filter.setOperator(Filter.Operator.GREATER_OR_EQUALS);
							break;
						
						case REPOSITORY_QUERY_FILTER_OPERATOR_LESS:
							filter.setOperator(Filter.Operator.LESS);
							break;
						
						case REPOSITORY_QUERY_FILTER_OPERATOR_LESS_OR_EQUALS:
							filter.setOperator(Filter.Operator.LESS_OR_EQUALS);
							break;
						
						case REPOSITORY_QUERY_FILTER_OPERATOR_NOT_LIKE:
							filter.setOperator(Filter.Operator.NOT_LIKE);
							break;
						
						case REPOSITORY_QUERY_FILTER_OPERATOR_LIKE:
							filter.setOperator(Filter.Operator.LIKE);
							break;
						
						case REPOSITORY_QUERY_FILTER_OPERATOR_ILIKE:
							filter.setOperator(Filter.Operator.ILIKE);
							break;
						
						default:
							throw new HttpException(EXCEPTION_REPOSITORY_QUERY_FILTER_PARAMETER_OPERATOR_NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE);
					}
					
					break;
				}
				
				case REPOSITORY_QUERY_FILTER_PARAMETER_GLUE:
				{
					switch(value)
					{
						case REPOSITORY_QUERY_FILTER_GLUE_OR:
							filter.setGlue(Filter.Glue.OR);
							break;
						
						case REPOSITORY_QUERY_FILTER_GLUE_AND:
							filter.setGlue(Filter.Glue.AND);
							break;
						
						default:
							throw new HttpException(EXCEPTION_REPOSITORY_QUERY_FILTER_PARAMETER_GLUE_NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE);
					}
					
					break;
				}
				
				case REPOSITORY_QUERY_FILTER_PARAMETER_VALUE:
				{
					filter.setValue(value);
					
					break;
				}
				
				case REPOSITORY_QUERY_FILTER_PARAMETER_NULL_SUPPRESSED:
				{
					if(value.matches(Constants.TEXT_PATTERN_BOOLEAN))
					{
						filter.setNullSuppressed(Boolean.valueOf(value));
					}
					
					break;
				}
				
				case REPOSITORY_QUERY_FILTER_PARAMETER_EMPTY_SUPPRESSED:
				{
					if(value.matches(Constants.TEXT_PATTERN_BOOLEAN))
					{
						filter.setEmptySuppressed(Boolean.valueOf(value));
					}
					
					break;
				}
				
				case REPOSITORY_QUERY_FILTER_PARAMETER_FIXED:
				{
					if(value.matches(Constants.TEXT_PATTERN_BOOLEAN))
					{
						filter.setFixed(Boolean.valueOf(value));
					}
					
					break;
				}
			}
		}
		
		return filter;
	}
}
