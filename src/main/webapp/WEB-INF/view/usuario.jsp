<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="u" uri="/usuario" %>
<%@ taglib prefix="s" uri="/string" %>

<c:set var="editAction" value="/${usuario != null ? usuario.usuarioId : ''}" scope="request" />
<c:set var="readonly" value="${!u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_USUARIO_ESCRITA')}" scope="request" />

<script>var USUARIO_NEW = ${(usuario == null)};</script>
<script>var USUARIO_READONLY = ${readonly};</script>

<div class="jumbotron">
	<form id="usuario-form" class="form-horizontal" role="form" method="post" action="${__contextPath__}/usuario${fn:escapeXml((usuario != null ? editAction : ''))}">
		<fieldset>
			<legend><i class="fa fa-info-circle fa-fw"></i> Informações</legend>
			<c:if test="${usuario != null}">
		        <div class="form-group">
		            <label class="col-md-2 control-label">Código: </label>
		            <div class="col-md-10">
		                <div class="margin-top-7">${fn:escapeXml((usuario.usuarioId))}</div>
		            </div>
		        </div>
	        </c:if>
	        <div class="form-group">
	            <label for="usuario-apelido" class="col-md-2 control-label">Usuário: </label>
	            <div class="col-md-4">
	                <input type="text" class="form-control" name="apelido" id="usuario-apelido" autofocus value="${fn:escapeXml((usuario.apelido))}" ${readonly ? 'disabled' : ''}>
	            </div>
	            <label for="usuario-status" class="col-md-2 control-label">Status: </label>
	            <div class="col-md-4">
	                <select class="form-control" name="status" id="usuario-status" ${readonly ? 'disabled' : ''}>
	                	<option></option>
	                	<c:forEach items="${status}" var="usuarioStatus">
	                		<option value="${fn:escapeXml((usuarioStatus))}" ${usuario != null ? usuario.status == usuarioStatus ? 'selected' : '' : ''} data-replace="usuario.status.${fn:escapeXml((usuarioStatus))}" data-replace-text>${fn:escapeXml((usuarioStatus))}</option>
	                	</c:forEach>
	                </select>
	            </div>
	        </div>
	        <div class="form-group">
	            <label for="usuario-pessoa" class="col-md-2 control-label">Estabelecimento: </label>
	            <div class="col-md-10">
	                <select class="form-control" name="pessoa.pessoaId" id="usuario-pessoa" ${readonly ? 'disabled' : ''}>
	                	<option></option>
	                	<c:forEach items="${estabelecimentos}" var="estabelecimento">
	                		<option value="${fn:escapeXml((estabelecimento.pessoaId))}" ${usuario != null ? usuario.pessoa.pessoaId == estabelecimento.pessoaId ? 'selected' : '' : ''}>${fn:escapeXml((estabelecimento.nomeFantasia))} (${estabelecimento.cnpj != null ? s:format(fn:escapeXml((estabelecimento.cnpj)), '##.###.###/####-##') : s:format(fn:escapeXml((estabelecimento.cpf)), '###.###.###-##')})</option>
	                	</c:forEach>
	                </select>
	            </div>
	        </div>
	        <c:if test="${usuario == null && !readonly}">
	        	<div class="form-group">
		            <label for="usuario-senha" class="col-md-2 control-label">Senha: </label>
		            <div class="col-md-4">
		                <input type="password" class="form-control" name="senha" id="usuario-senha" ${readonly ? 'disabled' : ''}>
		            </div>
		            <label for="usuario-senha-confirmation" class="col-md-2 control-label">Confirmação: </label>
		            <div class="col-md-4">
		                <input type="password" class="form-control" id="usuario-senha-confirmation" ${readonly ? 'disabled' : ''}>
		            </div>
	        	</div>
	        	<br /><br />
	        	<div class="form-group">
	        		<div class="col-md-6 col-md-offset-3 alert alert-info text-center" role="alert"><strong><i class="fa fa-smile-o"></i> Fique tranquilo!</strong> Sua senha será criptografada em nossa Base</div>
	        	</div>
	        </c:if>
        </fieldset>
        <fieldset>
			<legend><i class="fa fa-lock fa-fw"></i> Permissões</legend>
			<div class="mg-left-15 checkbox">
				<label>
		    		<input type="checkbox" id="usuario-perfis-all" ${readonly ? 'disabled' : ''}> Todos
				</label>
			</div>
			<c:forEach items="${perfis}" var="usuarioPerfil">
				<div class="mg-left-15 checkbox">
					<label>
			    		<input type="checkbox" name="perfis" value="${fn:escapeXml((usuarioPerfil))}" ${usuario != null ? u:hasPerfilForUsuario(usuario, usuarioPerfil) ? 'checked' : '' : ''} ${readonly ? 'disabled' : ''}> <span data-replace="usuario.perfis.${fn:escapeXml((usuarioPerfil))}">${fn:escapeXml((usuarioPerfil))}</span>
					</label>
				</div>
			</c:forEach>
		</fieldset>
        <c:if test="${!readonly || usuario != null}">
	        <hr />
       		<button type="submit" class="btn btn-green">
       			<c:choose>
       				<c:when test="${usuario != null}">
       					<i class="glyphicon fa fa-pencil"></i> Editar
       				</c:when>
       				<c:otherwise>
       					<i class="glyphicon fa glyphicon-plus"></i> Cadastrar
       				</c:otherwise>
       			</c:choose>
       		</button>
        </c:if>
	</form>
</div>

<script src="${__contextPath__}/assets/js/usuario_senha.js?v=${__appVersion__}"></script>	
<script src="${__contextPath__}/assets/js/usuario.js?v=${__appVersion__}"></script>
