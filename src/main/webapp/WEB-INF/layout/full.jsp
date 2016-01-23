<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="u" uri="/usuario" %>
<%@ taglib prefix="json" uri="/json" %>

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
		
		<!-- Custom JS -->
		<script src="${__contextPath__}/assets/js/layout.js?v=${__appVersion__}"></script>
		
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
        </script>
	</head>

	<body>

		<c:if test="${__partialViewSimpleName__ != 'autenticacao'}">
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
						</ul>
						<ul class="nav navbar-nav navbar-right">
							<li class="${__partialViewSimpleName__ == 'autenticacao' ? 'active' : ''}">
								<a href="${__contextPath__}/autenticacao"><i class="fa fa-lock fa-fw"></i> Entrar</a>
							</li>
						</ul>
					</div><!--/.nav-collapse -->
				</div>
			</nav>
		</c:if>

		<div class="container">
			<c:import url="${__partialViewFullName__}"></c:import>
		</div> <!-- /container -->
	</body>
</html>
