package br.com.maiscambio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.maiscambio.util.HttpException;

@ControllerAdvice
public class HttpExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(value = { HttpException.class })
	protected ResponseEntity<Object> handle(HttpException httpException, WebRequest webRequest)
	{
		if(httpException.getException() != null)
		{
			throw httpException;
		}
		else
		{
			return httpException.getResponseEntity();
		}
	}
}
