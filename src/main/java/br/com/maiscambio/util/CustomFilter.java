package br.com.maiscambio.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import br.com.maiscambio.WebMvcConfig;

public class CustomFilter extends OncePerRequestFilter
{
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
	{
		String url = request.getRequestURL().toString();
		
		if(request.getQueryString() != null)
		{
			url += Constants.CHAR_QUESTION_MARK + request.getQueryString();
		}
		
		response.addHeader("X-App-Name", WebMvcConfig.APP_NAME);
		response.addHeader("X-App-Version", WebMvcConfig.APP_VERSION);
		response.addHeader("X-Url", url);
		response.addHeader("X-Uri", request.getRequestURI());
		
		filterChain.doFilter(request, response);
	}
}
