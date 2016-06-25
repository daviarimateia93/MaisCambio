package br.com.maiscambio;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.com.maiscambio.controller.BaseController;
import br.com.maiscambio.util.HttpException;
import br.com.maiscambio.util.View;

public class CustomHandlerInterceptorAdapter extends HandlerInterceptorAdapter
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
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
        super.postHandle(request, response, handler, modelAndView);
    }
    
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        super.afterConcurrentHandlingStarted(request, response, handler);
        
        if(handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            
            PreHandle preHandle = handlerMethod.getMethodAnnotation(PreHandle.class);
            
            if(preHandle != null)
            {
                Class<? extends Runnable> preHandleValue = preHandle.value();
                
                if(preHandleValue != null)
                {
                    preHandleValue.newInstance().run();
                }
            }
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        super.afterCompletion(request, response, handler, ex);
        
        if(handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            
            PostHandle postHandle = handlerMethod.getMethodAnnotation(PostHandle.class);
            
            if(postHandle != null)
            {
                Class<? extends Runnable> postHandleValue = postHandle.value();
                
                if(postHandleValue != null)
                {
                    postHandleValue.newInstance().run();
                }
            }
        }
    }
}
