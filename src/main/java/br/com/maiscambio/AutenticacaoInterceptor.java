package br.com.maiscambio;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.com.maiscambio.controller.BaseController;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.View;

public class AutenticacaoInterceptor extends HandlerInterceptorAdapter
{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		if(handler instanceof HandlerMethod)
		{
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			
			if(handlerMethod.getBean() instanceof BaseController)
			{
				Autenticacao autenticacao = handlerMethod.getMethodAnnotation(Autenticacao.class);
				
				if(autenticacao != null)
				{
					BaseController baseController = (BaseController) handlerMethod.getBean();
					Class<?> returnType = handlerMethod.getReturnType().getParameterType();
					
					try
					{
						baseController.authenticate(autenticacao);
						
						return true;
					}
					catch(HttpException httpException)
					{
						if(returnType.isAssignableFrom(View.class))
						{
							baseController.handleHttpException(httpException, response);
						}
						else
						{
							httpException.write(response);
						}
						
						return false;
					}
				}
			}
		}
		
		return super.preHandle(request, response, handler);
	}
}
