<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="json" uri="/json" %>

<link rel="stylesheet" type="text/css" href="${__contextPath__}/assets/css/autenticacao.css?v=${__appVersion__}" />

<table cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
	<tr>
		<td valign="middle">
			<div class="row">  
			
				<div class="col-sm-6 col-sm-offset-3 col-lg-4 col-lg-offset-4">
				
				    <div class="form-container">
				        <div class="top-wrapper text-center mg-btm-20">
				        	<h3><i class="fa fa-lock fa-fw"></i> Autentique-se</h3>
				        </div>
				        <div class="bottom-wrapper">
				            <form id="frmAutenticacao" class="login-form" role="form" action="${__contextPath__}/autenticacao/login?url=${fn:escapeXml((param['url']))}" method="post">
				            	<input class="hidden" aria-hidden="true">
								<input type="password" class="hidden" aria-hidden="true">
				                <div class="form-group has-feedback">
				                    <input type="text" name="apelido" class="form-control" placeholder="Usuário" autocomplete="off">
				                    <span class="fa fa-user form-control-feedback"></span>
				                </div>
				                <div class="form-group has-feedback mg-btm-30">
				                    <input type="password" name="senha" class="form-control" placeholder="Senha" autocomplete="off">
				                    <span class="fa fa-unlock-alt form-control-feedback"></span>
				                </div>
				                <div class="form-group checkbox text-center">
				                	<label>
				                		<input type="checkbox" name="remember"> <i>Lembrar usuário?</i>
				                	</label>
				                </div>
				                <div class="form-group mg-btm-30">
				                    <button type="submit" class="btn btn-green btn-block">ENTRAR</button>
				                </div>
				            </form>
				        </div>
				    </div>
				    
				</div>
			
			</div>
		</td>
	</tr>
</table>

<script src="${__contextPath__}/assets/js/autenticacao.js?v=${__appVersion__}"></script>
