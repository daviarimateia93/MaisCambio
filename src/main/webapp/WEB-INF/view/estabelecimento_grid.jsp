<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Red Header Model -->
<div class="modal fade" id="remove-modal" data-backdrop="static" data-keyboard="false" role="dialog" data-move="#modalExternalLink" data-move-insert data-move-before>
	<form id="remove-form" method="post" class="form-horizontal" role="form">
     	<div class="modal-dialog">
         	<div class="modal-content">
            	<div class="modal-header modal-header-red">
                	<h4 class="modal-title">Excluir - <span class="nome-fantasia"></span></h4>
            	</div>
             	<div class="modal-body">
             		<p>
             			<strong>Tem certeza que deseja continuar?</strong>
             		</p>
             	</div>
             	<div class="modal-footer">
             		<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                 	<button type="submit" class="btn btn-red">Excluir</button>
             	</div>
         	</div>
     	</div>
    </form>
</div>
<!-- End Red Header Modal -->

<c:import url="/WEB-INF/view/grid.jsp"></c:import>
<script src="${__contextPath__}/assets/js/estabelecimento_grid.js?v=${__appVersion__}"></script>
