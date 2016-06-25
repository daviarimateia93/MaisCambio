package br.com.maiscambio.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERString;

public class CertificateHelper
{
	private static final String EXTENSION_VALUE_2_5_29_19 = "2.5.29.19";
	private static final String EXTENSION_VALUE_2_16_76_1_3_3 = "2.16.76.1.3.3";
	private static final String EXTENSION_VALUE_1_3_6_1_5_5_7_3_2 = "1.3.6.1.5.5.7.3.2";
	private static final String EXTENSION_OTHERS_NAMES_EMAIL_KEY = "Email";
	
	public static boolean validateVersion(X509Certificate x509Certificate)
	{
		return x509Certificate.getVersion() != 3;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean validateBasicRestrictions(X509Certificate x509Certificate)
	{
		DERBoolean derBoolean = new DERBoolean(false);
		
		try
		{
			byte[] extensionValues = x509Certificate.getExtensionValue(EXTENSION_VALUE_2_5_29_19);
			
			if(extensionValues != null)
			{
				DERObject derObject = toDERObject(extensionValues);
				DEROctetString derOctetString = (DEROctetString) derObject;
				DERSequence derSequence = (DERSequence) toDERObject(derOctetString.getOctets());
				Enumeration<DERObject> enumeration = derSequence.getObjects();
				
				if(enumeration.hasMoreElements())
				{
					derBoolean = (DERBoolean) enumeration.nextElement();
				}
				
				if(enumeration.hasMoreElements())
				{
					enumeration.nextElement();
				}
			}
		}
		catch(Exception exception)
		{
		
		}
		
		if(derBoolean.isTrue())
		{
			return false;
		}
		
		return true;
	}
	
	public static boolean validateNonRepudiation(X509Certificate x509Certificate)
	{
		return x509Certificate.getKeyUsage()[1];
	}
	
	public static boolean validateSignKeyUsage(X509Certificate x509Certificate)
	{
		return x509Certificate.getKeyUsage()[0];
	}
	
	public static boolean validatePeriod(X509Certificate x509Certificate)
	{
		Date currentDate = new Date();
		
		return x509Certificate.getNotBefore().before(currentDate) && x509Certificate.getNotAfter().after(currentDate);
	}
	
	public static boolean validateOthersNames(X509Certificate x509Certificate)
	{
		Map<String, String> othersNames = null;
		
		try
		{
			othersNames = getOthersNames(x509Certificate);
		}
		catch(CertificateParsingException | IOException exception)
		{
			return false;
		}
		
		return othersNames.get(EXTENSION_VALUE_2_16_76_1_3_3) != null;
	}
	
	public static boolean validateAdvancedKeyUsage(X509Certificate x509Certificate)
	{
		try
		{
			return x509Certificate.getExtendedKeyUsage().contains(EXTENSION_VALUE_1_3_6_1_5_5_7_3_2);
		}
		catch(CertificateParsingException certificateParsingException)
		{
			return false;
		}
	}
	
	public static String getCnpj(X509Certificate x509Certificate)
	{
		Map<String, String> othersNames = null;
		
		try
		{
			othersNames = getOthersNames(x509Certificate);
		}
		catch(CertificateParsingException | IOException exception)
		{
			return null;
		}
		
		return othersNames.get(EXTENSION_VALUE_2_16_76_1_3_3);
	}
	
	public static String getSubjectDN(X509Certificate x509Certificate)
	{
		return x509Certificate.getSubjectDN().toString();
	}
	
	public static String getIssuerDN(X509Certificate x509Certificate)
	{
		return x509Certificate.getIssuerDN().toString();
	}
	
	public static Date getNotAfter(X509Certificate x509Certificate)
	{
		return x509Certificate.getNotAfter();
	}
	
	public static Date getNotBefore(X509Certificate x509Certificate)
	{
		return x509Certificate.getNotBefore();
	}
	
	public static X509Certificate toX509Certificate(byte[] bytes)
	{
		try
		{
			CertificateFactory certificateFactory = CertificateFactory.getInstance(Constants.TEXT_CERTIFICATE_TYPE_X509);
			
			return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(bytes));
		}
		catch(CertificateException certificateException)
		{
			return null;
		}
	}
	
	public static DERObject toDERObject(byte[] bytes) throws IOException
	{
		ASN1InputStream asn1InputStream = null;
		
		try
		{
			asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(bytes));
			
			return asn1InputStream.readObject();
		}
		finally
		{
			asn1InputStream.close();
		}
	}
	
	public static Map<X509Certificate, PrivateKey> getCertificates(byte[] bytes, String senha)
	{
		Map<X509Certificate, PrivateKey> certificates = new HashMap<>();
		
		try
		{
			KeyStore keyStore = KeyStore.getInstance(Constants.TEXT_KEYSTORE_INSTANCE_TYPE_PKCS12);
			
			keyStore.load(new ByteArrayInputStream(bytes), senha.toCharArray());
			
			for(Enumeration<String> aliases = keyStore.aliases(); aliases.hasMoreElements();)
			{
				String alias = (String) aliases.nextElement();
				X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
				PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, senha.toCharArray());
				certificates.put(x509Certificate, privateKey);
			}
			
			return certificates;
		}
		catch(NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException | UnrecoverableKeyException exception)
		{
			return null;
		}
	}
	
	public static <T, U> Map<String, String> getOthersNames(X509Certificate x509Certificate) throws CertificateParsingException, IOException
	{
		Map<String, String> othersNamesMap = new HashMap<>();
		
		Collection<List<?>> collectionSubjectAlternativeName = x509Certificate.getSubjectAlternativeNames();
		
		if(collectionSubjectAlternativeName != null)
		{
			for(List<?> subjectAlternativeName : collectionSubjectAlternativeName)
			{
				switch(((Number) subjectAlternativeName.get(0)).intValue())
				{
					case 0:
					{
						OthersNames<DERObjectIdentifier, String> othersNames = OthersNames.getOtherName((byte[]) subjectAlternativeName.get(1));
						othersNamesMap.put(((DERObjectIdentifier) othersNames.first).getId(), othersNames.second);
						break;
					}
					
					case 1:
					{
						othersNamesMap.put(EXTENSION_OTHERS_NAMES_EMAIL_KEY, (String) subjectAlternativeName.get(1));
					}
				}
			}
		}
		
		return othersNamesMap;
	}
	
	private static class OthersNames<T, U>
	{
		private T first;
		private U second;
		
		private OthersNames(T first, U second)
		{
			this.first = first;
			this.second = second;
		}
		
		@SuppressWarnings("rawtypes")
		private static OthersNames<DERObjectIdentifier, String> getOtherName(byte[] encoded) throws IOException
		{
			ASN1InputStream asn1InputStream = new ASN1InputStream(encoded);
			DERSequence derSequence = null;
			DERObjectIdentifier derObjectIdentifier = null;
			String content = Constants.TEXT_EMPTY;
			derSequence = (DERSequence) asn1InputStream.readObject();
			asn1InputStream.close();
			Enumeration enumeration = derSequence.getObjects();
			derObjectIdentifier = (DERObjectIdentifier) enumeration.nextElement();
			DERObject derObject = ((ASN1TaggedObject) ((ASN1TaggedObject) enumeration.nextElement()).getObject()).getObject();
			
			if(derObject instanceof DERString)
			{
				content = ((DERString) derObject).getString();
			}
			else if(derObject instanceof DEROctetString)
			{
				content = new String(((DEROctetString) derObject).getOctets(), Constants.TEXT_CHARSET_ISO_8859_1);
			}
			
			return new OthersNames<DERObjectIdentifier, String>(derObjectIdentifier, content);
		}
	}
}
