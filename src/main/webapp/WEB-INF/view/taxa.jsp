<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="json" uri="/json" %>
<%@ taglib prefix="s" uri="/string" %>
<%@ taglib prefix="u" uri="/usuario" %>

<div class="jumbotron">
	<fieldset>
		<legend>
			<i class="fa fa-money fa-fw"></i> Taxas de <span data-replace="taxa.finalidade.${fn:escapeXml((finalidade))}"></span>
		</legend>
		<div class="table-responsive">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th>Ativo</th>
						<th>Moeda</th>
						<th>Espécie</th>
						<th>Cartão</th>
						<th>Alterar</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${moedas}" var="moeda" varStatus="loop">
						<tr data-form data-form-action="${__contextPath__}/taxa/${fn:escapeXml((finalidade))}" data-form-method="post" class="${finalidade == 'VENDA' ? 'success' : finalidade == 'COMPRA' ? 'info' : ''}">
							<td>
								<input class="margin-left-10 margin-top-10" type="checkbox" />
							</td>
							<td>
								<input type="text" class="form-control" value="${fn:escapeXml((moeda))}" readonly />
							</td>
							<td>
								<input type="text" class="form-control" value="R$ 3,64" data-mask-money data-prefix="R$ " data-allow-zero="true" data-mask-money />
							</td>
							<td>
								<input type="text" class="form-control" value="R$ 3,74" data-mask-money data-prefix="R$ " data-allow-zero="true" data-mask-money />
							</td>
							<td>
								<a href="javascript: void(0);" data-form-submit class="margin-left-10 ${finalidade == 'VENDA' ? 'color-green' : finalidade == 'COMPRA' ? 'color-blue' : ''}">
									<i class="fa fa-check fa-2x"></i>
								</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</fieldset>
</div>
