package br.com.maiscambio.util;

public class Setable<T>
{
    private T value;
    
    public T defaultValue()
    {
        return null;
    }
    
    public Setable()
    {
        value = defaultValue();
    }
    
    public T getValue()
    {
        return value;
    }
    
    public void setValue(T value)
    {
        this.value = value;
    }
}
