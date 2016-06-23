package br.com.maiscambio;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.service.AutenticacaoService;
import me.gerenciar.util.Constants;
import me.gerenciar.util.ControllerHelper;

// we are not using singleton pattern because of HttpSessionListener (web.xml)
// that forces a public constructor :-( so we are using an static concurrent map
public class UsuarioSessionManager implements HttpSessionListener
{
	private static final Object lock = new Object();
	private static final Map<String, Entry> entries = new ConcurrentHashMap<>();
	
	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent)
	{
		// ignore
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent)
	{
		// we want to prevent a leak on concurrent map
		remove(httpSessionEvent.getSession());
	}
	
	public void put(HttpServletRequest request)
	{
		put(getEstabelecimentoPessoaIdFromRequest(request), getUsuarioFromRequest(request), request.getSession());
	}
	
	public void put(HttpSession session)
	{
		put(getEstabelecimentoPessoaIdFromSession(session), getUsuarioFromSession(session), session);
	}
	
	private void put(Long estabelecimentoPessoaId, Usuario usuario, HttpSession session)
	{
		synchronized(lock)
		{
			if(usuario != null)
			{
				Entry entry = entries.get(getHash(estabelecimentoPessoaId, usuario.getUsuarioId()));
				
				if(entry != null)
				{
					try
					{
						entry.getSession().invalidate();
					}
					catch(IllegalStateException illegalStateException)
					{
						// ignore
					}
				}
				
				entries.put(getHash(estabelecimentoPessoaId, usuario.getUsuarioId()), new Entry(usuario.getUsuarioId(), session));
			}
		}
	}
	
	public void remove(HttpServletRequest request)
	{
		remove(getEstabelecimentoPessoaIdFromRequest(request), getUsuarioFromRequest(request));
	}
	
	public void remove(HttpSession session)
	{
		remove(getEstabelecimentoPessoaIdFromSession(session), getUsuarioFromSession(session));
	}
	
	private void remove(Long estabelecimentoPessoaId, Usuario usuario)
	{
		synchronized(lock)
		{
			if(usuario != null)
			{
				entries.remove(getHash(estabelecimentoPessoaId, usuario.getUsuarioId()));
			}
		}
	}
	
	private Long getEstabelecimentoPessoaIdFromRequest(HttpServletRequest request)
	{
		return getEstabelecimentoPessoaIdFromSession(request.getSession());
	}
	
	private Long getEstabelecimentoPessoaIdFromSession(HttpSession session)
	{
		return (Long) session.getAttribute(AutenticacaoService.SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_PESSOA_ID);
	}
	
	private Usuario getUsuarioFromRequest(HttpServletRequest request)
	{
		return getUsuarioFromSession(request.getSession());
	}
	
	private Usuario getUsuarioFromSession(HttpSession session)
	{
		return (Usuario) session.getAttribute(AutenticacaoService.SESSION_ATTRIBUTE_NAME_USUARIO);
	}
	
	private String getHash(Long estabelecimentoPessoaId, Long usuarioId)
	{
		return estabelecimentoPessoaId + String.valueOf(Constants.CHAR_SEMICOLON) + usuarioId;
	}
	
	public void iterate(Iterator iterator)
	{
		for(Map.Entry<String, Entry> entry : entries.entrySet())
		{
			iterator.iterate(Long.valueOf(entry.getKey().split(String.valueOf(Constants.CHAR_SEMICOLON))[0]), entry.getValue().getUsuarioId(), ControllerHelper.getSid(entry.getValue().getSession()));
		}
	}
	
	public static class Entry
	{
		private Long usuarioId;
		private HttpSession session;
		
		public Entry(Long usuarioId, HttpSession session)
		{
			this.usuarioId = usuarioId;
			this.session = session;
		}
		
		public Long getUsuarioId()
		{
			return usuarioId;
		}
		
		public HttpSession getSession()
		{
			return session;
		}
	}
	
	public static interface Iterator
	{
		public void iterate(Long estabelecimentoPessoaId, Long usuarioId, String sid);
	}
}
