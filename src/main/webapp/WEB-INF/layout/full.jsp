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
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
		<meta name="description" content="Encontre as melhores taxas de câmbio, negocie e fique por dentro das novidades do mercado!" />
        <meta name="keywords" content="casas de câmbio, cotação, cambio, câmbio turismo, corretoras de câmbio, melhor taxa de câmbio, menor taxa de câmbio, melhor taxa, menor taxa" />
        <meta name="author" content="MaisCambio.com.br" />
        <link rel="shortcut icon" title="${__appName__}" href="${__contextPath__}/assets/img/favicon.png">
        <link rel="apple-touch-icon" title="${__appName__}" href="${__contextPath__}/assets/img/favicon.png">

		<title>MaisCâmbio - Compare melhor/menor taxa de câmbio</title>

		<!-- Bootstrap core CSS -->
		<link href="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/css/bootstrap.min.css?v=${__appVersion__}" rel="stylesheet">
		
		<!-- Font Awesome -->
		<link href="${__contextPath__}/assets/libs/font-awesome-4.5.0-dist/css/font-awesome.min.css?v=${__appVersion__}" rel="stylesheet">

		<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
		<link href="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/assets/ie10-viewport-bug-workaround.css?v=${__appVersion__}" rel="stylesheet">

		<!-- Custom styles for this template -->
		<link href="${__contextPath__}/assets/css/layout.css?v=${__appVersion__}" rel="stylesheet">

		<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/assets/html5shiv.min.js?v=${__appVersion__}"></script>
			<script src="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/assets/respond.min.js?v=${__appVersion__}"></script>
		<![endif]-->
		
		<script src="${__contextPath__}/assets/libs/jquery-1.11.3-dist/jquery.min.js?v=${__appVersion__}"></script>
		<script src="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/js/bootstrap.min.js?v=${__appVersion__}"></script>
		
		<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
		<script src="${__contextPath__}/assets/libs/bootstrap-3.3.6-dist/assets/ie10-viewport-bug-workaround.js?v=${__appVersion__}"></script>
		
		<script>
        	var APP_NAME = ${json:toString(__appName__)};
        	var APP_VERSION = ${json:toString(__appVersion__)};
        
        	var ROOT = ${json:toString(__contextPath__)};
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
						<li class="active"><a href="#">Home</a></li>
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="#">Action</a></li>
								<li><a href="#">Another action</a></li>
								<li><a href="#">Something else here</a></li>
								<li role="separator" class="divider"></li>
								<li class="dropdown-header">Nav header</li>
								<li><a href="#">Separated link</a></li>
								<li><a href="#">One more separated link</a></li>
							</ul>
						</li>
					</ul>
					<ul class="nav navbar-nav navbar-right">
						<li><a href="../navbar/">Default</a></li>
						<li><a href="../navbar-static-top/">Static top</a></li>
						<li class="active"><a href="./">Fixed top <span class="sr-only">(current)</span></a></li>
					</ul>
				</div><!--/.nav-collapse -->
			</div>
		</nav>

		<div class="container">
			<c:import url="${__partialViewFullName__}"></c:import>
		</div> <!-- /container -->
	</body>
</html>
