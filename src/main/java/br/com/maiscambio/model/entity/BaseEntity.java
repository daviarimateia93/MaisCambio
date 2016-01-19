package br.com.maiscambio.model.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import br.com.maiscambio.util.JsonHelper;
import br.com.maiscambio.util.ReflectionHelper;

public abstract class BaseEntity implements Serializable, Cloneable
{
	private static final long serialVersionUID = 7239211429741812347L;
	
	private Field getField(String fieldName)
	{
		for(Field field : ReflectionHelper.getFields(this))
		{
			if(field.getName().equals(fieldName))
			{
				return field;
			}
		}
		
		return null;
	}
	
	private void setFieldValue(Field field, Object value)
	{
		try
		{
			field.setAccessible(true);
			field.set(this, value);
		}
		catch(IllegalArgumentException | IllegalAccessException | SecurityException exception)
		{
		
		}
	}
	
	private List<Field> getIdFields()
	{
		List<Field> idFields = new ArrayList<>();
		List<Field> fields = ReflectionHelper.getFields(this);
		
		for(Field field : fields)
		{
			if(field.isAnnotationPresent(Id.class))
			{
				idFields.add(field);
			}
		}
		
		return idFields;
	}
	
	private Object getFieldValue(Field field)
	{
		field.setAccessible(true);
		
		try
		{
			return field.get(this);
		}
		catch(IllegalArgumentException | IllegalAccessException exception)
		{
			return null;
		}
	}
	
	private List<Object> getIdFieldsValues()
	{
		List<Object> idFieldsValues = new ArrayList<>();
		
		for(Field field : getIdFields())
		{
			idFieldsValues.add(getFieldValue(field));
		}
		
		return idFieldsValues;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> void setList(String fieldName, List<T> newBaseEntities)
	{
		Field field = getField(fieldName);
		
		List<T> oldBaseEntities = (List<T>) getFieldValue(field);
		
		if(oldBaseEntities == null || newBaseEntities == null)
		{
			setFieldValue(field, newBaseEntities);
		}
		else
		{
			List<Integer> removeIndexes = new ArrayList<>();
			List<Integer[]> refreshIndexes = new ArrayList<>();
			
			for(int i = 0; i < oldBaseEntities.size(); i++)
			{
				int index = -1;
				
				for(int j = 0; j < newBaseEntities.size(); j++)
				{
					if(oldBaseEntities.get(i).equals(newBaseEntities.get(j)))
					{
						index = j;
						
						break;
					}
				}
				
				if(index == -1)
				{
					removeIndexes.add(i);
				}
				else
				{
					refreshIndexes.add(new Integer[] { i, index });
				}
			}
			
			for(int i = 0; i < newBaseEntities.size(); i++)
			{
				int index = -1;
				
				for(int j = 0; j < oldBaseEntities.size(); j++)
				{
					if(newBaseEntities.get(i).equals(oldBaseEntities.get(j)))
					{
						index = j;
						
						break;
					}
				}
				
				if(index == -1)
				{
					oldBaseEntities.add(i, newBaseEntities.get(i));
				}
			}
			
			for(Integer index : removeIndexes)
			{
				oldBaseEntities.remove((int) index);
			}
			
			for(Integer[] indexes : refreshIndexes)
			{
				T oldBaseEntity = oldBaseEntities.get(indexes[0]);
				T newBaseEntity = newBaseEntities.get(indexes[1]);
				
				oldBaseEntities.set(indexes[0], oldBaseEntity.refresh(newBaseEntity));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T refresh(T baseEntity)
	{
		if(equals(baseEntity))
		{
			List<Field> fields = ReflectionHelper.getFields(this);
			
			for(Field field : fields)
			{
				field.setAccessible(true);
				
				try
				{
					field.set(this, field.get(baseEntity));
				}
				catch(IllegalArgumentException | IllegalAccessException exception)
				{
					// ignore it
				}
			}
		}
		
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T copy()
	{
		try
		{
			ObjectOutputStream objectOutputStream = null;
			ObjectInputStream objectInputStream = null;
			
			try
			{
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(this);
				objectOutputStream.flush();
				objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
				
				return (T) objectInputStream.readObject();
			}
			finally
			{
				objectOutputStream.close();
				objectInputStream.close();
			}
		}
		catch(ClassNotFoundException | IOException exception)
		{
			return null;
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		for(Object idFieldValue : getIdFieldsValues())
		{
			if(idFieldValue != null)
			{
				stringBuilder.append(idFieldValue);
			}
		}
		
		return stringBuilder.toString();
	}
	
	@Override
	public boolean equals(Object object)
	{
		return toString().equals(object.toString());
	}
	
	public boolean equalsIgnoreCase(Object object)
	{
		return toString().equalsIgnoreCase(object.toString());
	}
	
	public boolean identical(Object object)
	{
		return JsonHelper.toString(this).equals(JsonHelper.toString(object));
	}
	
	public boolean identicalIgnoreCase(Object object)
	{
		return JsonHelper.toString(this).equalsIgnoreCase(JsonHelper.toString(object));
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		
		int result = 1;
		
		for(Object idFieldValue : getIdFieldsValues())
		{
			result = prime * result + (idFieldValue == null ? 0 : idFieldValue.hashCode());
		}
		
		return result;
	}
}
