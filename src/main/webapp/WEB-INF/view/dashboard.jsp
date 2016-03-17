<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="json" uri="/json" %>
<%@ taglib prefix="s" uri="/string" %>
<%@ taglib prefix="u" uri="/usuario" %>

<c:choose>
	<c:when test="${u:hasPerfilForRequest(pageContext.request, 'ADMIN')}">
		<div class="jumbotron">
			<fieldset>
				<legend><i class="fa fa-clock-o fa-fw"></i> Pendências</legend>
					<c:choose>
						<c:when test="${fn:length(estabelecimentos) > 0}">
							<div class="table-responsive">
								<table class="table table-striped table-hover">
									<thead>
										<th>Nome fantasia</th>
										<th>CNPJ/CPF/ID</th>
										<th>Ação</th>
									</thead>
									<tbody>
										<c:forEach items="${estabelecimentos}" var="estabelecimento" varStatus="loop">
											<tr class="warning">
												<td>
													<a href="${__contextPath__}/estabelecimento/${estabelecimento.pessoaId}">${estabelecimento.nomeFantasia}</a>
												</td>
												<td>
													<a href="${__contextPath__}/estabelecimento/${estabelecimento.pessoaId}">${estabelecimento.cnpj != null ? s:format(estabelecimento.cnpj, '##.###.###/####-##') : estabelecimento.cpf != null ? s:format(estabelecimento.cpf, '###.###.###-##') : estabelecimento.idEstrangeiro}</a>
												</td>
												<td>
													<a href="${__contextPath__}/estabelecimento/${estabelecimento.pessoaId}/activate">Ativar</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</c:when>
						<c:otherwise>
							<p><i>Não há nenhuma pendência</i></p>
						</c:otherwise>
					</c:choose>
			</fieldset>
		</div>
	</c:when>
	<c:otherwise>
		<c:if test="${u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_TAXA_LEITURA')}">
			<div class="jumbotron">
				<fieldset>
					<legend>
						<div class="pull-left"><i class="fa fa-money fa-fw"></i> Taxas</div>
						<div class="pull-left position-relative top-3-negative">
							<div class="margin-left-10 margin-top-11 padding-7 margin-right-7 bg-green pull-left"></div><div class="pull-left"> <small class="font-size-70-percent">(venda)</small></div>
							<div class="margin-left-10 margin-top-11 padding-7 margin-right-7 bg-blue pull-left"></div><div class="pull-left"> <small class="font-size-70-percent">(compra)</small></div>
							<div class="clearfix"></div>
						</div>
						<div class="clearfix"></div>
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
									<tr data-form data-form-action="${__contextPath__}/taxa/venda" data-form-method="post" class="success">
										<td>
											<input class="margin-left-10 margin-top-10" type="checkbox" />
										</td>
										<td>
											<input type="text" class="form-control" value="${moeda}" readonly />
										</td>
										<td>
											<input type="text" class="form-control" value="R$ 3,64" data-mask-money data-prefix="R$ " data-allow-zero="true" data-mask-money />
										</td>
										<td>
											<input type="text" class="form-control" value="R$ 3,74" data-mask-money data-prefix="R$ " data-allow-zero="true" data-mask-money />
										</td>
										<td>
											<a href="javascript: void(0);" data-form-submit class="margin-left-10 color-green">
												<i class="fa fa-check fa-2x"></i>
											</a>
										</td>
									</tr>
								</c:forEach>
								<c:forEach items="${moedas}" var="moeda" varStatus="loop">
									<tr data-form data-form-action="${__contextPath__}/taxa/venda" data-form-method="post" class="info">
										<td>
											<input class="margin-left-10 margin-top-10" type="checkbox" />
										</td>
										<td>
											<input type="text" class="form-control" value="${moeda}" readonly />
										</td>
										<td>
											<input type="text" class="form-control" value="R$ 3,64" data-mask-money data-prefix="R$ " data-allow-zero="true" data-mask-money />
										</td>
										<td>
											<input type="text" class="form-control" value="R$ 3,74" data-mask-money data-prefix="R$ " data-allow-zero="true" data-mask-money />
										</td>
										<td>
											<a href="javascript: void(0);" data-form-submit class="margin-left-10 color-blue">
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
		</c:if>
	</c:otherwise>
</c:choose>