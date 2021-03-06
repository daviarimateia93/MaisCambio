package br.com.maiscambio.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.MaskFormatter;

import org.apache.commons.lang3.StringUtils;

import com.sun.syndication.io.impl.Base64;

public class StringHelper
{
    public static String base64Encode(String string)
    {
        try
        {
            return Base64.encode(string);
        }
        catch(Exception exception)
        {
            return null;
        }
    }
    
    public static String base64Decode(String string)
    {
        try
        {
            return Base64.decode(string);
        }
        catch(Exception exception)
        {
            return null;
        }
    }
    
    public static String urlEncode(String string)
    {
        try
        {
            return URLEncoder.encode(string, Constants.TEXT_CHARSET_UTF_8);
        }
        catch(Exception exception)
        {
            return null;
        }
    }
    
    public static String urlDecode(String string)
    {
        try
        {
            return URLDecoder.decode(string, Constants.TEXT_CHARSET_UTF_8);
        }
        catch(Exception exception)
        {
            return null;
        }
    }
    
    public static String fixToHtml(String string)
    {
        return string == null ? null : string.trim().replace(String.valueOf(Constants.CHAR_LT), Constants.TEXT_HTML_CODE_LT).replace(String.valueOf(Constants.CHAR_GT), Constants.TEXT_HTML_CODE_GT).replace(String.valueOf(Constants.CHAR_AMP), Constants.TEXT_HTML_CODE_AMP).replace(String.valueOf(Constants.CHAR_QUOTE), Constants.TEXT_HTML_CODE_QUOTE).replace(String.valueOf(Constants.CHAR_SINGLE_QUOTE), Constants.TEXT_HTML_CODE_SINGLE_QUOTE).replace(String.valueOf(Constants.CHAR_EQUALS), Constants.TEXT_HTML_CODE_EQUALS);
    }
    
    public static String shuffle(String string)
    {
        if(string == null)
        {
            return null;
        }
        
        if(string.length() > 0)
        {
            List<Character> characters = new ArrayList<>();
            
            for(char character : string.toCharArray())
            {
                characters.add(character);
            }
            
            StringBuilder output = new StringBuilder(string.length());
            
            while(!characters.isEmpty())
            {
                output.append(characters.remove((int) (Math.random() * characters.size())));
            }
            
            return output.toString();
        }
        else
        {
            return string;
        }
    }
    
    public static String hex(byte[] stringBytes)
    {
        if(stringBytes == null)
        {
            return null;
        }
        
        StringBuilder output = new StringBuilder();
        
        for(int i = 0; i < stringBytes.length; i++)
        {
            output.append(Integer.toString((stringBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return output.toString();
    }
    
    public static String onlyNumbers(String string)
    {
        if(!isEmpty(string))
        {
            return string.replaceAll(Constants.TEXT_PATTERN_NON_NUMERIC_ALL, Constants.TEXT_EMPTY);
        }
        
        return null;
    }
    
    public static String format(String string, String pattern)
    {
        if(string == null)
        {
            return null;
        }
        
        try
        {
            MaskFormatter mask = new MaskFormatter(pattern);
            mask.setValueContainsLiteralCharacters(false);
            
            return mask.valueToString(string);
        }
        catch(Exception exception)
        {
            return string;
        }
    }
    
    public static boolean isBlank(String string)
    {
        return StringUtils.isBlank(string);
    }
    
    public static boolean isEmpty(String string)
    {
        return StringUtils.isEmpty(string);
    }
    
    public static String encryptSHA512AndHex(String string)
    {
        if(string == null)
        {
            return null;
        }
        
        MessageDigest sha512 = null;
        
        try
        {
            sha512 = MessageDigest.getInstance(Constants.TEXT_ALGORITHM_SHA_512);
            
            return StringHelper.hex(sha512.digest(string.getBytes()));
        }
        catch(Exception exception)
        {
            return null;
        }
    }
}
