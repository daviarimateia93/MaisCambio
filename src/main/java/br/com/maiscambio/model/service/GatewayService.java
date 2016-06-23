package br.com.maiscambio.model.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import me.gerenciar.util.BaseService;
import me.gerenciar.util.Constants;
import me.gerenciar.util.HttpException;

@Service
public class GatewayService implements BaseService
{
	public String intercept(String action, String payload, HttpServletRequest request)
	{
		HttpURLConnection httpURLConnection = null;
		
		try
		{
			URL url = new URL(action);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod(request.getMethod());
			httpURLConnection.setRequestProperty(Constants.TEXT_CONTENT_LENGTH, Integer.toString(payload.getBytes().length));
			httpURLConnection.setRequestProperty(Constants.TEXT_CONTENT_TYPE, request.getContentType());
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setDoOutput(true);
			
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), Constants.TEXT_CHARSET_UTF_8);
			outputStreamWriter.write(payload);
			outputStreamWriter.close();
			
			InputStream inputStream = httpURLConnection.getResponseCode() == 200 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();
			
			if(inputStream == null)
			{
				throw new HttpException(HttpStatus.NOT_FOUND);
			}
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			
			while((line = bufferedReader.readLine()) != null)
			{
				stringBuilder.append(line);
				stringBuilder.append('\r');
			}
			
			bufferedReader.close();
			
			if(httpURLConnection.getResponseCode() == 200)
			{
				return stringBuilder.toString();
			}
			else
			{
				throw new HttpException(stringBuilder.toString(), HttpStatus.valueOf(httpURLConnection.getResponseCode()));
			}
		}
		catch(Exception exception)
		{
			if(exception instanceof HttpException)
			{
				throw (HttpException) exception;
			}
			else
			{
				throw new HttpException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		finally
		{
			if(httpURLConnection != null)
			{
				httpURLConnection.disconnect();
			}
		}
	}
}
