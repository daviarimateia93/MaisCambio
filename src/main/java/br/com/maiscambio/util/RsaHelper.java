package br.com.maiscambio.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RsaHelper
{
	public static byte[] encrypt(byte[] bytes, PrivateKey privateKey)
	{
		try
		{
			Cipher cipher = Cipher.getInstance(Constants.TEXT_ALGORITHM_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			
			return cipher.doFinal(bytes);
		}
		catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException exception)
		{
			return null;
		}
	}
	
	public static byte[] decrypt(byte[] bytes, PublicKey publicKey)
	{
		try
		{
			Cipher cipher = Cipher.getInstance(Constants.TEXT_ALGORITHM_RSA);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			
			return cipher.doFinal(bytes);
		}
		catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException exception)
		{
			return null;
		}
	}
}
