package br.com.maiscambio.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CloneHelper
{
    @SuppressWarnings("unchecked")
    public static <T> T clone(T object)
    {
        try
        {
            ObjectOutputStream objectOutputStream = null;
            ObjectInputStream objectInputStream = null;
            
            try
            {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                
                return (T) objectInputStream.readObject();
            }
            finally
            {
                if(objectOutputStream != null)
                {
                    objectOutputStream.close();
                }
                
                if(objectInputStream != null)
                {
                    objectInputStream.close();
                }
            }
        }
        catch(ClassNotFoundException | IOException exception)
        {
            return null;
        }
    }
}
