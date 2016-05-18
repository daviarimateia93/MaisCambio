package br.com.maiscambio.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ControllerHelper
{
    public static String getParameter(String name, HttpServletRequest request)
    {
        String parameter = request.getParameter(name);
        
        return parameter != null ? !StringHelper.isBlank(parameter) ? parameter : null : null;
    }
    
    public static String[] getParameterValues(String name, HttpServletRequest request)
    {
        String[] parameterValues = request.getParameterValues(name);
        
        return parameterValues != null ? parameterValues.length > 0 ? parameterValues : null : null;
    }
    
    public static Cookie getCookie(String name, HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        
        if(cookies != null)
        {
            for(Cookie cookie : request.getCookies())
            {
                if(cookie.getName().equals(name))
                {
                    return cookie;
                }
            }
        }
        
        return null;
    }
    
    public static String fileUpload(String relativeFilePath, byte[] bytes, HttpServletRequest request) throws NoSuchAlgorithmException, IOException
    {
        String slash = String.valueOf(Constants.CHAR_SLASH);
        String dot = String.valueOf(Constants.CHAR_DOT);
        
        if(relativeFilePath == null)
        {
            return null;
        }
        else if(relativeFilePath.isEmpty())
        {
            return null;
        }
        
        if(relativeFilePath.startsWith(slash))
        {
            relativeFilePath = relativeFilePath.substring(1, relativeFilePath.length());
        }
        
        String dir = Constants.TEXT_EMPTY;
        String name = StringHelper.hex(MessageDigest.getInstance(Constants.TEXT_ALGORITHM_SHA_512).digest(UUID.randomUUID().toString().getBytes()));
        String extension = Constants.TEXT_EMPTY;
        
        if(relativeFilePath.contains(slash))
        {
            dir = relativeFilePath.substring(0, relativeFilePath.lastIndexOf(slash));
            
            String nameAndExtension = relativeFilePath.substring(relativeFilePath.lastIndexOf(slash), relativeFilePath.length());
            
            if(relativeFilePath.contains(dot))
            {
                extension = nameAndExtension.substring(nameAndExtension.lastIndexOf(dot), nameAndExtension.length());
            }
        }
        
        String basePath = request.getServletContext().getRealPath(slash);
        
        File fileDir = new File(basePath + dir);
        
        if(!fileDir.exists())
        {
            fileDir.mkdirs();
        }
        
        OutputStream outputStream = new FileOutputStream(new File(basePath + dir + slash + name + extension));
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        
        return request.getContextPath() + slash + dir + slash + name + extension;
    }
    
    public static String getSid(HttpServletRequest request)
    {
        return getSid(request.getSession());
    }
    
    public static String getSid(HttpSession session)
    {
        return StringHelper.encryptSHA512AndHex(session.getId());
    }
}
