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
	</c:otherwise>
</c:choose>