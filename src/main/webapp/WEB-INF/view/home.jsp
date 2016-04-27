<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="${__contextPath__}/assets/css/home.css?v=${__appVersion__}" rel="stylesheet">

<div id="query" class="jumbotron col-md-3 col-sm-4">
	<form id="query-form" role="form">
		<fieldset>
			<legend>Eu quero</legend>
		</fieldset>
		<div class="radio">
	    	<label>
	    		<input type="radio" name="query-comprar-vender" value="comprar" checked> Comprar
	    	</label>
	  	</div>
	  	<div class="radio">
	    	<label>
	    		<input type="radio" name="query-comprar-vender" value="vender"> Vender
	    	</label>
	  	</div>
	  	<fieldset>
	  		<legend>Preciso de</legend>
	  	</fieldset>
	  	<div class="form-group">
	  		<select name="query-moeda" class="form-control">
				<option value="" disabled selected>Selecione...</option>
				<c:forEach items="${moedas}" var="moeda">
				    <option value="${fn:escapeXml((moeda))}" data-replace="taxa.moeda.${fn:escapeXml((moeda))}" data-replace-text>${fn:escapeXml((moeda))}</option>
				</c:forEach>
			</select>
	  	</div>
	  	<fieldset>
	  		<legend>Estou em</legend>
	  	</fieldset>
	  	<div class="form-group">
	  		<select name="query-estado" class="form-control">
				<option value="" disabled selected>Selecione...</option>
				<c:forEach items="${estados}" var="estado">
				    <option value="${fn:escapeXml((estado.estadoId))}">${fn:escapeXml((estado.nome))}</option>
				</c:forEach>
			</select>
	  	</div>
	</form>
</div>
<div id="content" class="jumbotron col-md-offset-1 col-sm-offset-1 col-md-8 col-sm-7">
	<h4 class="text-center">Encontre as melhores taxas de câmbio, negocie e fique por dentro das novidades do mercado!</h4>
</div>
