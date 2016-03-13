<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="u" uri="/usuario" %>
<%@ taglib prefix="json" uri="/json" %>

<c:set var="sessionUsuario" value="${sessionScope.USUARIO}" scope="request" />
<c:set var="menuEstabelecimentoHref" value="${u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_LEITURA') ? '/estabelecimento/list' : u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_ESCRITA') ? '/estabelecimento' : ''}" scope="request" />
<c:set var="menuUsuarioHref" value="${u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_USUARIO_LEITURA') ? '/usuario/list' : u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_USUARIO_ESCRITA') ? '/usuario' : ''}" scope="request" />

<!DOCTYPE html>
<html lang="pt-BR">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<meta name="description" content="Encontre as melhores taxas de câmbio, negocie e fique por dentro das novidades do mercado!" />
        <meta name="keywords" content="casas de câmbio, cotação, cambio, câmbio turismo, corretoras de câmbio, melhor taxa de câmbio, menor taxa de câmbio, melhor taxa, menor taxa" />
        <meta name="author" content="MaisCambio.com.br" />
        <link rel="shortcut icon" title="${__appName__}" href="${__contextPath__}/assets/img/favicon.png" />
        <link rel="apple-touch-icon" title="${__appName__}" href="${__contextPath__}/assets/img/favicon.png" />

		<title>MaisCâmbio - Compare melhor/menor taxa de câmbio</title>

		<!-- CSS Libs -->
		<link href="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/css/bootstrap.min.css?v=${__appVersion__}" rel="stylesheet" />
		<link href="${__contextPath__}/assets/libs/font-awesome-4.5.0-dist/css/font-awesome.min.css?v=${__appVersion__}" rel="stylesheet" />
		<link href="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/assets/ie10-viewport-bug-workaround.css?v=${__appVersion__}" rel="stylesheet" />
		<link href="${__contextPath__}/assets/libs/toastr/toastr.min.css?v=${__appVersion__}" rel="stylesheet" />
		<link href="${__contextPath__}/assets/libs/bootstrap-datepicker/bootstrap-datepicker.css?v=${__appVersion__}" rel="stylesheet" />
        <link href="${__contextPath__}/assets/libs/jquery-filer/jquery.filer.css?v=${__appVersion__}" rel="stylesheet" />
        <link href="${__contextPath__}/assets/libs/jquery-filer/themes/jquery.filer-dragdropbox-theme.css?v=${__appVersion__}" rel="stylesheet" />

		<!-- Custom CSS -->
		<link href="${__contextPath__}/assets/css/layout.css?v=${__appVersion__}" rel="stylesheet" />
		
		<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/assets/html5shiv.min.js?v=${__appVersion__}"></script>
			<script src="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/assets/respond.min.js?v=${__appVersion__}"></script>
		<![endif]-->
		
		<!-- JS Libs -->
		<script src="${__contextPath__}/assets/libs/jquery-1.11.3-dist/jquery.min.js?v=${__appVersion__}"></script>
		<script src="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/js/bootstrap.min.js?v=${__appVersion__}"></script>
		<script src="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/assets/ie10-viewport-bug-workaround.js?v=${__appVersion__}"></script>
		<script src="${__contextPath__}/assets/libs/json2/json2.js?v=${__appVersion__}"></script>
		<script src="${__contextPath__}/assets/libs/jquery-jstorage/jstorage.js?v=${__appVersion__}"></script>
		<script src="${__contextPath__}/assets/libs/toastr/toastr.custom.js?v=${__appVersion__}"></script>
		<script src="${__contextPath__}/assets/libs/jquery-hotkeys/jquery.hotkeys.js?v=${__appVersion__}"></script>
        <script src="${__contextPath__}/assets/libs/moment/moment.min.js?v=${__appVersion__}"></script>
        <script src="${__contextPath__}/assets/libs/bootstrap-datepicker/bootstrap-datepicker.js?v=${__appVersion__}"></script>
        <script src="${__contextPath__}/assets/libs/jquery-maskedinput/jquery.maskedinput.js?v=${__appVersion__}"></script>
        <script src="${__contextPath__}/assets/libs/jquery-maskmoney/jquery.maskMoney.custom.js?v=${__appVersion__}"></script>
        <script src="${__contextPath__}/assets/libs/jquery-filer/jquery.filer.js?v=${__appVersion__}"></script>
        <script src="${__contextPath__}/assets/libs/bigdecimal/BigDecimal-all-last.min.js?v=${__appVersion__}"></script>
        <script src="${__contextPath__}/assets/libs/datatables/jquery.dataTables.custom.js?v=${__appVersion__}"></script>
        <script src="${__contextPath__}/assets/libs/datatables/fnReloadAjax.js?v=${__appVersion__}"></script>
		
		<!-- Custom JS -->
		<script src="${__contextPath__}/assets/js/layout.js?v=${__appVersion__}"></script>
		
		<!-- Replace Bundle JS -->
		<script src="${__contextPath__}/assets/libs/replace-bundle/bundle.js?v=${__appVersion__}"></script>
		
		<!-- Templates -->
		<script id="filerBoxTemplate" type="text/template">
			<c:import url="/WEB-INF/template/filer_box.jsp"></c:import>
		</script>
		
		<script id="filerItemTemplate" type="text/template">
			<c:import url="/WEB-INF/template/filer_item.jsp"></c:import>
		</script>
		
		<script id="filerItemAppendTemplate" type="text/template">
			<c:import url="/WEB-INF/template/filer_item_append.jsp"></c:import>
		</script>
		
		<script id="filerChangeInputTemplate" type="text/template">
			<c:import url="/WEB-INF/template/filer_change_input.jsp"></c:import>
		</script>

		<script id="filerProgressBarTemplate" type="text/template">
			<c:import url="/WEB-INF/template/filer_progress_bar.jsp"></c:import>
		</script>
		
		<script id="filerSuccessTemplate" type="text/template">
			<c:import url="/WEB-INF/template/filer_success.jsp"></c:import>
		</script>
		
		<script id="filerErrorTemplate" type="text/template">
			<c:import url="/WEB-INF/template/filer_error.jsp"></c:import>
		</script>
		
		<script>
        	var APP_NAME = ${json:toString(__appName__)};
        	var APP_VERSION = ${json:toString(__appVersion__)};
        
        	var ROOT = ${json:toString(__contextPath__)};
        	
			var SID = ${json:toString(sid)};
        	
        	var ESTABELECIMENTO_PESSOA_ID = ${json:toString(sessionEstabelecimentoPessoaId)};
        	
        	var PARTIAL_VIEW_SIMPLE_NAME = ${json:toString(__partialViewSimpleName__)};
        	
        	var USUARIO_ID = ${json:toString(sessionUsuario.usuarioId)};
        	
        	var USUARIO_APELIDO = ${json:toString(sessionUsuario.apelido)};
        	
        	var USUARIO_PERFIS = [
				<c:if test="${sessionUsuario != null}">
					<c:forEach items="${sessionUsuario.perfis}" var="perfil" varStatus="loop">
						${json:toString(perfil)}<c:if test="${!loop.last}">,</c:if>
					</c:forEach>
				</c:if>
            ];
        </script>
	</head>

	<body>

		<!-- Fixed navbar -->
		<nav class="navbar navbar-default navbar-fixed-top">
			<div class="container">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
						<span class="sr-only">Navegação</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="${__contextPath__}/">
						<img src="${__contextPath__}/assets/img/logo.png" width="155" height="50" title="MaisCâmbio" alt="MaisCâmbio" />
					</a>
				</div>
				<div id="navbar" class="navbar-collapse collapse">
					<ul class="nav navbar-nav">
						<li class="${__partialViewSimpleName__ == 'home' ? 'active' : ''}">
							<a href="${__contextPath__}"><i class="fa fa-home fa-fw"></i> Home</a>
						</li>
						<c:choose>
							<c:when test="${sessionUsuario == null}"></c:when>
							<c:otherwise>
								<c:if test="${u:hasPerfilForRequest(pageContext.request, 'ADMIN') ? u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_LEITURA') && u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_ESCRITA') : u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_LEITURA')}">
									<li class="${__partialViewSimpleName__ == 'dashboard' ? 'active' : ''}">
										<a href="${__contextPath__}/dashboard"><i class="fa fa-desktop fa-fw"></i> Dashboard</a>
									</li>
								</c:if>
								<c:if test="${fn:length(menuEstabelecimentoHref) > 0}">
									<li class="${__partialViewSimpleName__ == 'estabelecimento_grid' || __partialViewSimpleName__ == 'estabelecimento' ? 'active' : ''}">
										<a href="${__contextPath__}${menuEstabelecimentoHref}">
											<c:choose>
												<c:when test="${u:hasPerfilForRequest(pageContext.request, 'ADMIN')}">
													<i class="fa fa-hand-o-right fa-fw"></i> Parceiro
												</c:when>
												<c:otherwise>
													<i class="fa fa-info fa-fw"></i> Informações
												</c:otherwise>
											</c:choose>
										</a>
									</li>
								</c:if>
								<c:if test="${fn:length(menuUsuarioHref) > 0}">
									<li class="${__partialViewSimpleName__ == 'usuario_grid' || __partialViewSimpleName__ == 'usuario' ? 'active' : ''}">
										<a href="${__contextPath__}${menuUsuarioHref}"><i class="fa fa-user fa-fw"></i> Usuário</a>
									</li>
								</c:if>
							</c:otherwise>
						</c:choose>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<c:choose>
							<c:when test="${sessionUsuario == null}">
								<li class="${__partialViewSimpleName__ == 'estabelecimento' ? 'active' : ''}">
									<a href="${__contextPath__}/estabelecimento"><i class="fa fa-hand-o-right fa-fw"></i> Seja nosso parceiro</a>
								</li>
								<li class="${__partialViewSimpleName__ == 'autenticacao' ? 'active' : ''}">
									<a href="${__contextPath__}/autenticacao"><i class="fa fa-lock fa-fw"></i> Entrar</a>
								</li>
							</c:when>
							<c:otherwise>
								<li>
									<a href="${__contextPath__}/autenticacao/logout"><i class="fa fa-power-off fa-fw"></i> Sair</a>
								</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div><!--/.nav-collapse -->
			</div>
		</nav>

		<div class="container">
			<c:import url="${__partialViewFullName__}"></c:import>
		</div> <!-- /container -->
		
		<c:if test="${sessionUsuario != null}">
			<!-- Light Blue Header Model -->
	        <div class="modal fade" id="modalExternalLink" data-backdrop="static" data-keyboard="false" role="dialog">
	            <div class="modal-dialog">
	                <div class="modal-content">
	                    <div class="modal-header modal-header-light-blue">
	                        <h4 class="modal-title">Ops! Link externo</h4>
	                    </div>
	                    <div class="modal-body">
	                    	<p>Algum link externo está tentando ser aberto, caso deseje, clique em continuar.</p>
	                    	<p>
	                    		<small>
	                    			<i class="link"></i>
	                    		</small>
	                    	</p>
	                    </div>
	                    <div class="modal-footer">
	                    	<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
	                        <a href="javascript: void(0);" target="_blank" class="continue btn btn-blue">Continuar</a>
	                    </div>
	                </div>
	            </div>
	        </div>
	        <!-- End Light Blue Header Modal -->
	       	
	       	<!-- Light Blue Header Model -->
	        <div class="modal fade" id="modalAutenticacao" data-backdrop="static" data-keyboard="false" role="dialog">
	        	<form method="post" action="${__contextPath__}/autenticacao/login?doNotRedirect=true" class="form-horizontal" role="form">
		            <div class="modal-dialog">
		                <div class="modal-content">
		                    <div class="modal-header modal-header-light-blue">
		                        <h4 class="modal-title">Ops! Sua sessão expirou</h4>
		                    </div>
		                    <div class="modal-body">
		                    	<div class="form-group">
					                <label for="iptUsuarioPaiPessoaId" class="col-sm-2 control-label">Matriz: </label>
					                <div class="col-sm-10">
					                    <input type="text" id="iptUsuarioPaiPessoaId" class="form-control" name="matriz" placeholder="Matriz" value="${fn:escapeXml((sessionMatriz))}" readonly>
					                </div>
					            </div>
					            <div class="form-group">
					                <label for="iptUsuarioApelido" class="col-sm-2 control-label">Usuário: </label>
					                <div class="col-sm-10">
					                    <input type="text" id="iptUsuarioApelido" class="form-control" name="apelido" placeholder="Usuário" value="${fn:escapeXml((sessionUsuario.apelido))}" readonly>
					                </div>
					            </div>
					            <div class="form-group">
					                <label for="iptUsuarioSenha" class="col-sm-2 control-label">Senha: </label>
					                <div class="col-sm-10">
					                    <input type="password" id="iptUsuarioSenha" class="form-control" name="senha" placeholder="Senha">
					                </div>
					            </div>
		                    </div>
		                    <div class="modal-footer">
		                    	<input type="hidden" class="form-control" name="estabelecimentoPessoaId" value="${fn:escapeXml((sessionEstabelecimentoPessoaId))}">
		                        <button type="submit" class="btn btn-blue">Entrar</button>
		                    </div>
		                </div>
		            </div>
	            </form>
	        </div>
	        <!-- End Light Blue Header Modal -->
		</c:if>
	</body>
</html>
