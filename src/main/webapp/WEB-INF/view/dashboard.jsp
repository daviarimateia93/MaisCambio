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
		<h1>Hello, normal user.</h1>
	</c:otherwise>
</c:choose>