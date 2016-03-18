<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="json" uri="/json" %>
<%@ taglib prefix="s" uri="/string" %>
<%@ taglib prefix="u" uri="/usuario" %>
<%@ taglib prefix="d" uri="/date" %>

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
													<a href="${__contextPath__}/estabelecimento/${fn:escapeXml((estabelecimento.pessoaId))}">${fn:escapeXml((estabelecimento.nomeFantasia))}</a>
												</td>
												<td>
													<a href="${__contextPath__}/estabelecimento/${fn:escapeXml((estabelecimento.pessoaId))}">${fn:escapeXml((estabelecimento.cnpj != null ? s:format(estabelecimento.cnpj, '##.###.###/####-##') : estabelecimento.cpf != null ? s:format(estabelecimento.cpf, '###.###.###-##') : estabelecimento.idEstrangeiro))}</a>
												</td>
												<td>
													<a href="${__contextPath__}/estabelecimento/${fn:escapeXml((estabelecimento.pessoaId))}/activate">Ativar</a>
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
		<div class="jumbotron">
			<fieldset>
				<legend>
					<div class="pull-left">Bem vindo, <strong>${fn:escapeXml((sessionUsuario.apelido))}</strong>.</div>
					<div class="pull-right"><span data-format="date" data-format-value="${fn:escapeXml((d:formatWithPattern(currentDate, 'yyyy-MM-dd\'T\'HH:mm:ss.SSSZ')))}" data-date-mask="DD/MM/YY HH:mm"></span></div>
					<div class="clearfix"></div>
				</legend>
			</fieldset>
		</div>
		<c:if test="${u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_TAXA_LEITURA')}">
			<c:set var="taxaReadonly" value="${u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_TAXA_ESCRITA')}" />
			<div class="jumbotron">
				<div>
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active"><a href="#taxa-venda" aria-controls="taxa-venda" role="tab" data-toggle="tab">Venda</a></li>
						<li role="presentation"><a href="#taxa-compra" aria-controls="taxa-compra" role="tab" data-toggle="tab">Compra</a></li>
					</ul>
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active padding-top-15" id="taxa-venda">
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
											<tr data-form data-form-action="${__contextPath__}/taxa/VENDA" data-form-method="post" class="success">
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
													<a href="javascript: void(0);" data-form-submit class="margin-left-10 color-green">
														<i class="fa fa-check fa-2x"></i>
													</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane padding-top-15" id="taxa-compra">
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
											<tr data-form data-form-action="${__contextPath__}/taxa/COMPRA" data-form-method="post" class="info">
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
													<a href="javascript: void(0);" data-form-submit class="margin-left-10 color-blue">
														<i class="fa fa-check fa-2x"></i>
													</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:if>
	</c:otherwise>
</c:choose>