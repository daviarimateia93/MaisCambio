<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="json" uri="/json" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" type="text/css" href="${__contextPath__}/assets/css/grid.css?v=${__appVersion__}" />

<script>
	var title = ${json:toString(param.title != null ? param.title : '')};
	var fixedFilters = [];
</script>

<div class="jumbotron">
	<div id="grid">
		<table cellpadding="0" cellspacing="0" border="0" class="grid" width="100%">
	    	<thead></thead>
	    	<tbody></tbody>
	    	<tfoot></tfoot>
	  	</table>
	  	<div id="grid-right-menu-mask">
			<ul id="grid-right-menu" class="dropdown-menu">
		  		
			</ul>
	  	</div>
	</div>
</div>

<c:if test="${paramValues.fixedFilter != null}">
	<c:forEach items="${paramValues.fixedFilter}" var="fixedFilter" varStatus="loop">
		<script>
			fixedFilters.push(${json:toString(fixedFilter)});
		</script>
	</c:forEach>
</c:if>

<script src="${__contextPath__}/assets/js/grid.js?v=${__appVersion__}"></script>
<script src="${__contextPath__}/assets/js/grid_layout.js?v=${__appVersion__}"></script>
