package br.com.maiscambio.model.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.maiscambio.UsuarioSessionManager;
import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Pessoa;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.entity.Usuario.Status;
import br.com.maiscambio.util.Constants;
import br.com.maiscambio.util.ControllerHelper;
import br.com.maiscambio.util.HttpException;

@Service
public class AutenticacaoService implements BaseService
{
    public static final String SESSION_ATTRIBUTE_NAME_USUARIO = "USUARIO";
    public static final String SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_PESSOA_ID = "ESTABELECIMENTO_PESSOA_ID";
    public static final String SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_NOME_FANTASIA = "ESTABELECIMENTO_NOME_FANTASIA";
    public static final String SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_CNPJ = "ESTABELECIMENTO_CNPJ";
    public static final String SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_CPF = "ESTABELECIMENTO_CPF";
    
    public static final String EXCEPTION_USER_CREDENTIALS_NOT_FOUND = "USER_CREDENTIALS_NOT_FOUND";
    public static final String EXCEPTION_LICENCA_IS_EXPIRED = "LICENCA_IS_EXPIRED";
    public static final String EXCEPTION_INVALID_USER_CREDENTIALS_FORMAT = "INVALID_USER_CREDENTIALS_FORMAT";
    public static final String EXCEPTION_UNAUTHORIZED_USUARIO = "UNAUTHORIZED_USUARIO";
    public static final String EXCEPTION_YOU_DO_NOT_HAVE_PERMISSION = "YOU_DO_NOT_HAVE_PERMISSION";
    public static final String EXCEPTION_USUARIO_IS_NOT_LOGGED_IN = "USUARIO_IS_NOT_LOGGED_IN";
    public static final String EXCEPTION_USUARIO_NOT_FOUND = "USUARIO_NOT_FOUND";
    public static final String EXCEPTION_USUARIO_SENHA_DOES_NOT_MATCH = "USUARIO_SENHA_DOES_NOT_MATCH";
    public static final String EXCEPTION_USUARIO_STATUS_INATIVO = "USUARIO_STATUS_INATIVO";
    public static final String EXCEPTION_ESTABELECIMENTO_NOT_FOUND = "ESTABELECIMENTO_NOT_FOUND";
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EstabelecimentoService estabelecimentoService;
    
    @Autowired
    private PessoaService pessoaService;
    
    public static boolean isLoggedIn(HttpServletRequest request)
    {
        return (request.getSession().getAttribute(SESSION_ATTRIBUTE_NAME_USUARIO) != null);
    }
    
    @Transactional(readOnly = true)
    public void login(HttpServletRequest request, String usuarioApelido, String usuarioSenha, boolean updateUsuarioSessionManager, boolean updateSessionEstabelecimentoPessoaId)
    {
        Usuario usuario = test(usuarioApelido, usuarioSenha);
        
        setupLoginSession(request, usuario, updateUsuarioSessionManager, updateSessionEstabelecimentoPessoaId);
        
        authenticate(request);
    }
    
    @Transactional(readOnly = true)
    public void loginEncrypted(HttpServletRequest request, String usuarioApelido, String usuarioSenhaEncrypted, boolean updateUsuarioSessionManager, boolean updateSessionEstabelecimentoPessoaId)
    {
        Usuario usuario = testEncrypted(usuarioApelido, usuarioSenhaEncrypted);
        
        setupLoginSession(request, usuario, updateUsuarioSessionManager, updateSessionEstabelecimentoPessoaId);
        
        authenticate(request);
    }
    
    @Transactional(readOnly = true)
    private void setupLoginSession(HttpServletRequest request, Usuario usuario, boolean updateUsuarioSessionManager, boolean updateSessionEstabelecimentoPessoaId)
    {
        request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME_USUARIO, usuario);
        
        if(updateSessionEstabelecimentoPessoaId)
        {
            Pessoa pessoa = pessoaService.getFromRequest(request);
            
            Estabelecimento estabelecimento = pessoa != null ? estabelecimentoService.findOne(pessoa.getPessoaId()) : null;
            
            if(estabelecimento != null)
            {
                request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_PESSOA_ID, estabelecimento.getPessoaId());
                request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_NOME_FANTASIA, estabelecimento.getNomeFantasia());
                request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_CNPJ, estabelecimento.getCnpj());
                request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_CPF, estabelecimento.getCpf());
            }
            else
            {
                request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_PESSOA_ID, null);
                request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_NOME_FANTASIA, null);
                request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_CNPJ, null);
                request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_CPF, null);
            }
        }
        
        if(updateUsuarioSessionManager)
        {
            new UsuarioSessionManager().put(request);
        }
    }
    
    public void logout(HttpServletRequest request)
    {
        request.getSession().invalidate();
        
        new UsuarioSessionManager().remove(request.getSession());
    }
    
    @Transactional(readOnly = true)
    private void authenticateForSession(HttpServletRequest request, Usuario.Perfil... usuarioPerfis)
    {
        if(isLoggedIn(request))
        {
            Usuario usuario = (Usuario) request.getSession().getAttribute(SESSION_ATTRIBUTE_NAME_USUARIO);
            
            usuarioIsInPerfis(usuario, usuarioPerfis);
        }
        else
        {
            throw new HttpException(EXCEPTION_USUARIO_IS_NOT_LOGGED_IN, HttpStatus.FORBIDDEN);
        }
    }
    
    @Transactional(readOnly = true)
    private void authenticateForRequest(HttpServletRequest request, Usuario.Perfil... usuarioPerfis)
    {
        String[] userCredentials = getUserCredentialsFromRequest(request);
        
        authenticate(userCredentials[1], userCredentials[2], usuarioPerfis);
    }
    
    public static String[] getUserCredentials()
    {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        
        if(requestAttributes != null)
        {
            if(requestAttributes instanceof ServletRequestAttributes)
            {
                String[] userCredentials = null;
                
                try
                {
                    userCredentials = getUserCredentialsFromRequest(((ServletRequestAttributes) requestAttributes).getRequest());
                }
                catch(HttpException httpException)
                {
                    
                }
                
                return userCredentials;
            }
        }
        
        return null;
    }
    
    public static String[] getUserCredentialsFromRequest(HttpServletRequest request)
    {
        Usuario loggedInUsuario = UsuarioService.getLoggedIn(request);
        
        if(loggedInUsuario != null)
        {
            return new String[] { loggedInUsuario.getApelido(), loggedInUsuario.getSenha(), String.valueOf(request.getSession().getAttribute(SESSION_ATTRIBUTE_NAME_ESTABELECIMENTO_PESSOA_ID)) };
        }
        else if(ControllerHelper.getParameter("apelido", request) != null && ControllerHelper.getParameter("senha", request) != null)
        {
            return new String[] { ControllerHelper.getParameter("apelido", request), ControllerHelper.getParameter("senha", request) };
        }
        else
        {
            // User-Credentials must follow this format:
            // usuarioApelido@sha512(usuarioSenha)[@estabelecimentoPessoaId]
            
            String userCredentials = request.getHeader(Constants.TEXT_HEADER_USER_CREDENTIALS);
            
            if(StringUtils.isNotEmpty(userCredentials))
            {
                if(userCredentials.contains("@"))
                {
                    String[] credentials = userCredentials.split("\\@");
                    
                    return credentials;
                }
                else
                {
                    throw new HttpException(EXCEPTION_INVALID_USER_CREDENTIALS_FORMAT, HttpStatus.FORBIDDEN);
                }
            }
            else
            {
                throw new HttpException(EXCEPTION_USER_CREDENTIALS_NOT_FOUND, HttpStatus.FORBIDDEN);
            }
        }
    }
    
    @Transactional(readOnly = true)
    public void authenticate(HttpServletRequest request, Usuario.Perfil... usuarioPerfis)
    {
        if(isLoggedIn(request))
        {
            authenticateForSession(request, usuarioPerfis);
        }
        else
        {
            authenticateForRequest(request, usuarioPerfis);
        }
        
        Estabelecimento estabelecimento = estabelecimentoService.getFromRequest(request);
        
        if(estabelecimento != null)
        {
            if(estabelecimento.getData() != null)
            {
                if(estabelecimento.getData().before(new Date()))
                {
                    logout(request);
                    
                    throw new HttpException(EXCEPTION_LICENCA_IS_EXPIRED, HttpStatus.FORBIDDEN);
                }
            }
        }
    }
    
    @Transactional(readOnly = true)
    public void authenticate(Usuario usuario, Usuario.Perfil... usuarioPerfis)
    {
        authenticate(usuario.getApelido(), usuario.getSenha(), usuarioPerfis);
    }
    
    @Transactional(readOnly = true)
    public void authenticate(HttpServletRequest request, Usuario usuario)
    {
        authenticate(request);
        
        Usuario usuarioFromRequest = usuarioService.getFromRequest(request);
        
        if(!usuarioFromRequest.getUsuarioId().equals(usuario.getUsuarioId()))
        {
            throw new HttpException(EXCEPTION_YOU_DO_NOT_HAVE_PERMISSION, HttpStatus.UNAUTHORIZED);
        }
    }
    
    @Transactional(readOnly = true)
    public void authenticate(HttpServletRequest request, Estabelecimento estabelecimento)
    {
        authenticate(request);
        
        Estabelecimento estabelecimentoFromRequest = estabelecimentoService.getFromRequest(request);
        
        if(!estabelecimentoFromRequest.getPessoaId().equals(estabelecimento.getPessoaId()))
        {
            throw new HttpException(EXCEPTION_YOU_DO_NOT_HAVE_PERMISSION, HttpStatus.UNAUTHORIZED);
        }
    }
    
    @Transactional(readOnly = true)
    private Usuario authenticate(String usuarioApelido, String usuarioSenhaEncrypted, Usuario.Perfil... usuarioPerfis)
    {
        Usuario usuario = testEncrypted(usuarioApelido, usuarioSenhaEncrypted);
        
        usuarioIsInPerfis(usuario, usuarioPerfis);
        
        // At this point we are authenticated :-)
        
        return usuario;
    }
    
    private void usuarioIsInPerfis(Usuario usuario, Usuario.Perfil... usuarioPerfis)
    {
        if(usuarioPerfis != null)
        {
            if(usuarioPerfis.length > 0)
            {
                if(!usuarioService.isInPerfis(usuario, usuarioPerfis))
                {
                    throw new HttpException(EXCEPTION_UNAUTHORIZED_USUARIO, HttpStatus.UNAUTHORIZED);
                }
            }
        }
    }
    
    @Transactional(readOnly = true)
    public Usuario test(String usuarioApelido, String usuarioSenha)
    {
        return testEncrypted(usuarioApelido, usuarioService.encryptSenha(usuarioSenha));
    }
    
    @Transactional(readOnly = true)
    public Usuario testEncrypted(String usuarioApelido, String usuarioSenhaEncrypted)
    {
        Usuario usuario = usuarioService.findByApelido(usuarioApelido);
        
        if(usuario == null)
        {
            throw new HttpException(EXCEPTION_USUARIO_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
        else
        {
            if(!usuario.getSenha().equals(usuarioSenhaEncrypted))
            {
                throw new HttpException(EXCEPTION_USUARIO_SENHA_DOES_NOT_MATCH, HttpStatus.FORBIDDEN);
            }
            
            // Usuario matches :-)
            
            if(usuario.getStatus() == Status.INATIVO)
            {
                throw new HttpException(EXCEPTION_USUARIO_STATUS_INATIVO, HttpStatus.UNAUTHORIZED);
            }
            
            // Usuario Status is allowed to continue :-)
            
            return usuario;
        }
    }
}
