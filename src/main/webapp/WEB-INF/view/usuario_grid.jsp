<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Green Header Model -->
<div class="modal fade" id="change-senha-modal" data-backdrop="static" data-keyboard="false" role="dialog" data-move="#modalExternalLink" data-move-insert data-move-before>
	<form id="change-senha-form" method="post" class="form-horizontal" role="form">
     	<div class="modal-dialog">
         	<div class="modal-content">
            	<div class="modal-header modal-header-green">
                	<h4 class="modal-title">Alterar senha - <span class="apelido"></span></h4>
            	</div>
             	<div class="modal-body">
             		<div class="form-groups" data-move="#frmEstabelecimento fieldset:first .form-groups" data-move-before>
             			<div class="form-group">
				            <label for="usuario-senha" class="col-sm-3 control-label">Nova Senha: </label>
				            <div class="col-sm-9">
				                <input type="password" class="form-control" name="newSenha" id="usuario-senha" ${readonly ? 'disabled' : ''}>
				            </div>
				        </div>
				        <div class="form-group">
				            <label for="usuario-senha-confirmation" class="col-sm-3 control-label">Confirmação: </label>
				            <div class="col-sm-9">
				                <input type="password" class="form-control" id="usuario-senha-confirmation" ${readonly ? 'disabled' : ''}>
				            </div>
				        </div>
             		</div>
             	</div>
             	<div class="modal-footer">
             		<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                 	<button type="submit" class="btn btn-green">Alterar</button>
             	</div>
         	</div>
     	</div>
    </form>
</div>
<!-- End Green Header Modal -->

<!-- Red Header Model -->
<div class="modal fade" id="remove-modal" data-backdrop="static" data-keyboard="false" role="dialog" data-move="#modalExternalLink" data-move-insert data-move-before>
	<form id="remove-form" method="post" class="form-horizontal" role="form">
     	<div class="modal-dialog">
         	<div class="modal-content">
            	<div class="modal-header modal-header-red">
                	<h4 class="modal-title">Excluir - <span class="apelido"></span></h4>
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
<script src="${__contextPath__}/assets/js/usuario_senha.js?v=${__appVersion__}"></script>
<script src="${__contextPath__}/assets/js/usuario_grid.js?v=${__appVersion__}"></script>
