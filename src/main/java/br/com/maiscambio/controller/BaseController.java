package br.com.maiscambio.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.Perfil;
import br.com.maiscambio.WebMvcConfig;
import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.service.AutenticacaoService;
import br.com.maiscambio.model.service.CidadeService;
import br.com.maiscambio.model.service.EmailService;
import br.com.maiscambio.model.service.EnderecoService;
import br.com.maiscambio.model.service.EstabelecimentoService;
import br.com.maiscambio.model.service.EstadoService;
import br.com.maiscambio.model.service.IofService;
import br.com.maiscambio.model.service.LicencaService;
import br.com.maiscambio.model.service.TaxaService;
import br.com.maiscambio.model.service.UsuarioService;
import me.gerenciar.util.Constants;
import me.gerenciar.util.ControllerHelper;
import me.gerenciar.util.DateHelper;
import me.gerenciar.util.HttpException;
import me.gerenciar.util.RepositoryQuery;
import me.gerenciar.util.StringHelper;
import me.gerenciar.util.View;

public abstract class BaseController
{
    private static ThreadLocal<List<Runnable>> preHandleRunnables = new ThreadLocal<List<Runnable>>()
    {
        @Override
        protected List<Runnable> initialValue()
        {
            return new ArrayList<>();
        }
    };
    
    private static ThreadLocal<List<Runnable>> postHandleRunnables = new ThreadLocal<List<Runnable>>()
    {
        @Override
        protected List<Runnable> initialValue()
        {
            return new ArrayList<>();
        }
    };
    
    protected static class PreHandleRunnablesExecutor implements Runnable
    {
        @Override
        public synchronized void run()
        {
            for(Runnable runnable : preHandleRunnables.get())
            {
                runnable.run();
            }
            
            preHandleRunnables.get().clear();
        }
    }
    
    public static class PostHandleRunnablesExecutor implements Runnable
    {
        @Override
        public synchronized void run()
        {
            for(Runnable runnable : postHandleRunnables.get())
            {
                runnable.run();
            }
            
            postHandleRunnables.get().clear();
        }
    }
    
    protected static void addPreHandleRunnable(Runnable runnable)
    {
        preHandleRunnables.get().add(runnable);
    }
    
    protected static void addPostHandleRunnable(Runnable runnable)
    {
        postHandleRunnables.get().add(runnable);
    }
    
    private static final String EXCEPTION_YOU_CAN_NOT_DO_THIS = "YOU_CAN_NOT_DO_THIS";
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private AutenticacaoService autenticacaoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EstadoService estadoService;
    
    @Autowired
    private CidadeService cidadeService;
    
    @Autowired
    private EnderecoService enderecoService;
    
    @Autowired
    private EstabelecimentoService estabelecimentoService;
    
    @Autowired
    private LicencaService licencaService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private TaxaService taxaService;
    
    @Autowired
    private IofService iofService;
    
    protected HttpServletRequest getRequest()
    {
        return request;
    }
    
    protected AutenticacaoService getAutenticacaoService()
    {
        return autenticacaoService;
    }
    
    protected UsuarioService getUsuarioService()
    {
        return usuarioService;
    }
    
    protected EstadoService getEstadoService()
    {
        return estadoService;
    }
    
    protected CidadeService getCidadeService()
    {
        return cidadeService;
    }
    
    protected EnderecoService getEnderecoService()
    {
        return enderecoService;
    }
    
    protected EstabelecimentoService getEstabelecimentoService()
    {
        return estabelecimentoService;
    }
    
    protected LicencaService getLicencaService()
    {
        return licencaService;
    }
    
    protected EmailService getEmailService()
    {
        return emailService;
    }
    
    protected TaxaService getTaxaService()
    {
        return taxaService;
    }
    
    protected IofService getIofService()
    {
        return iofService;
    }
    
    @Transactional(readOnly = true)
    public void authenticate()
    {
        autenticacaoService.authenticate(request);
    }
    
    @Transactional(readOnly = true)
    public void authenticate(Autenticacao autenticacao)
    {
        if(autenticacao.value().length == 0)
        {
            authenticate();
        }
        else
        {
            for(Perfil perfil : autenticacao.value())
            {
                authenticate(perfil.value());
            }
        }
    }
    
    @Transactional(readOnly = true)
    public void authenticate(Usuario.Perfil... usuarioPerfis)
    {
        autenticacaoService.authenticate(request, usuarioPerfis);
    }
    
    @Transactional(readOnly = true)
    public void authenticateByUsuarioId(Long usuarioId)
    {
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(usuarioId);
        
        autenticacaoService.authenticate(request, usuario);
    }
    
    protected View redirect(String path)
    {
        return View.redirect(WebMvcConfig.getInstance(), path);
    }
    
    protected View view(String viewName)
    {
        return view(viewName, null);
    }
    
    protected View view(String viewName, String title)
    {
        return view(null, viewName, title);
    }
    
    protected View view(String layoutName, String partialViewName, String title)
    {
        Date currentDate = AutenticacaoService.isLoggedIn(request) ? EstabelecimentoService.now(estabelecimentoService.getFromRequest(request)) : new Date();
        
        View view = new View(WebMvcConfig.getInstance(), layoutName, partialViewName, title);
        view.addObject("sid", ControllerHelper.getSid(request));
        view.addObject("currentFormattedDate", DateHelper.format(currentDate));
        view.addObject("currentDate", currentDate);
        
        return view;
    }
    
    private String fixAutenticacaoUrl(String autenticacaoUrl)
    {
        if(autenticacaoUrl != null)
        {
            String contextPath = "/" + WebMvcConfig.getInstance().getName();
            
            if(autenticacaoUrl.startsWith(contextPath))
            {
                autenticacaoUrl = autenticacaoUrl.replaceFirst("\\" + contextPath, Constants.TEXT_EMPTY);
            }
        }
        
        return autenticacaoUrl;
    }
    
    private String encodeAutenticacaoUrl(String autenticacaoUrl)
    {
        return autenticacaoUrl != null ? StringHelper.urlEncode(StringHelper.base64Encode(autenticacaoUrl)) : null;
    }
    
    private String decodeAutenticacaoUrl(String autenticacaoUrl)
    {
        return autenticacaoUrl != null ? StringHelper.urlDecode(StringHelper.base64Decode(autenticacaoUrl)) : null;
    }
    
    protected String getAutenticacaoUrl()
    {
        return decodeAutenticacaoUrl(fixAutenticacaoUrl(ControllerHelper.getParameter(AutenticacaoController.PARAMETER_URL, request)));
    }
    
    protected View autenticacao()
    {
        return autenticacao(null);
    }
    
    protected View autenticacao(HttpServletResponse response)
    {
        String url = request.getRequestURI();
        
        if(request.getQueryString() != null)
        {
            url += "?" + request.getQueryString();
        }
        
        String path = "/autenticacao?" + AutenticacaoController.PARAMETER_URL + String.valueOf(Constants.CHAR_EQUALS) + encodeAutenticacaoUrl(fixAutenticacaoUrl(url));
        
        if(response == null)
        {
            return redirect(path);
        }
        else
        {
            try
            {
                response.sendRedirect(WebMvcConfig.getInstance().getServletContext().getContextPath() + path);
            }
            catch(Exception ioException)
            {
                
            }
            
            return null;
        }
    }
    
    public View handleHttpException(HttpException httpException)
    {
        return handleHttpException(httpException, null);
    }
    
    public View handleHttpException(HttpException httpException, HttpServletResponse response)
    {
        if(httpException.getHttpStatus() == HttpStatus.FORBIDDEN)
        {
            return autenticacao(response);
        }
        else
        {
            throw httpException;
        }
    }
    
    protected Estabelecimento getEstabelecimentoFromRequest()
    {
        return getEstabelecimentoFromRequest(false);
    }
    
    protected Estabelecimento getEstabelecimentoFromRequest(boolean trowable)
    {
        Estabelecimento estabelecimento = estabelecimentoService.getFromRequest(request);
        
        if(estabelecimento == null && trowable)
        {
            throw new HttpException(EXCEPTION_YOU_CAN_NOT_DO_THIS, HttpStatus.NOT_ACCEPTABLE);
        }
        
        return estabelecimento;
    }
    
    protected <T> RepositoryQuery<T> getRepositoryQuery(Class<T> type)
    {
        return RepositoryQuery.getFromRequest(type, request);
    }
    
    protected String loadEmailTemplate(String emailTemplateFileName) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(WebMvcConfig.getInstance().getServletContext().getRealPath("/WEB-INF/template/" + emailTemplateFileName)), Constants.TEXT_CHARSET_UTF_8));
        
        int character;
        
        while((character = bufferedReader.read()) != -1)
        {
            stringBuilder.append((char) character);
        }
        
        bufferedReader.close();
        
        return stringBuilder.toString();
    }
    
    protected String setVariablesToEmailTemplate(String emailTemplate, Map<String, String> variables)
    {
        for(Entry<String, String> variable : variables.entrySet())
        {
            emailTemplate = emailTemplate.replace(variable.getKey(), variable.getValue());
        }
        
        return emailTemplate;
    }
    
    protected String loadEmailTemplateWithVariables(String emailTemplateFileName, Map<String, String> variables) throws IOException
    {
        return setVariablesToEmailTemplate(loadEmailTemplate(emailTemplateFileName), variables);
    }
    
    @Transactional(readOnly = true)
    protected boolean canEdit(Long pessoaId, Usuario.Perfil... usuarioPerfis)
    {
        Usuario usuario = getUsuarioService().getFromRequest(getRequest());
        Estabelecimento estabelecimento = getEstabelecimentoFromRequest();
        Estabelecimento foundEstabelecimento = getEstabelecimentoService().findOne(pessoaId);
        
        if(foundEstabelecimento == null)
        {
            throw new HttpException(EstabelecimentoService.EXCEPTION_ESTABELECIMENTO_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        
        boolean hasPerfis = true;
        
        for(Usuario.Perfil usuarioPerfil : usuarioPerfis)
        {
            if(!UsuarioService.hasPerfil(usuario, usuarioPerfil))
            {
                hasPerfis = false;
                
                break;
            }
        }
        
        boolean canEdit = usuario != null ? hasPerfis : false;
        canEdit = canEdit ? estabelecimento != null ? foundEstabelecimento.getPessoaId().equals(estabelecimento.getPessoaId()) || (foundEstabelecimento.getPai() != null ? foundEstabelecimento.getPai().getPessoaId().equals(estabelecimento.getPessoaId()) : false) : UsuarioService.hasPerfil(usuario, Usuario.Perfil.ADMIN) : false;
        
        return canEdit;
    }
}
