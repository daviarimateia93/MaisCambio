package br.com.maiscambio.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpException extends RuntimeException
{
	private static final long serialVersionUID = -6447762394307272573L;
	
	private Exception exception;
	private String message;
	private HttpStatus httpStatus;
	
	public HttpException(HttpStatus httpStatus)
	{
		this((String) null, httpStatus);
	}
	
	public HttpException(Exception exception, HttpStatus httpStatus)
	{
		super(exception);
		
		this.exception = exception;
		this.httpStatus = httpStatus;
	}
	
	public HttpException(String message, HttpStatus httpStatus)
	{
		super(message);
		
		this.message = message;
		this.httpStatus = httpStatus;
	}
	
	public Exception getException()
	{
		return exception;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public HttpStatus getHttpStatus()
	{
		return httpStatus;
	}
	
	public void setHttpStatus(HttpStatus httpStatus)
	{
		this.httpStatus = httpStatus;
	}
	
	public ResponseEntity<Object> getResponseEntity()
	{
		return new ResponseEntity<Object>(message, new HttpHeaders(), httpStatus);
	}
	
	public void write(HttpServletResponse response)
	{
		response.setStatus(httpStatus.value());
		
		if(message != null)
		{
			response.setContentLength(message.length());
			response.setContentType(Constants.TEXT_CONTENT_TYPE_TEXT_HTML);
			
			try
			{
				response.getOutputStream().write(message.getBytes());
			}
			catch(IOException ioException)
			{
				
			}
		}
	}
}
