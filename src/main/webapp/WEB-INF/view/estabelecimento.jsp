<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="json" uri="/json" %>
<%@ taglib prefix="u" uri="/usuario" %>
<%@ taglib prefix="s" uri="/string" %>

<c:set var="editAction" value="/${estabelecimento != null ? estabelecimento.pessoaId : ''}" scope="request" />

<c:if test="${estabelecimento != null}">
	<script>var PESSOA_ID = ${json:toString(estabelecimento.pessoaId)};</script>
</c:if>

<c:choose>
	<c:when test="${success}">
		<div class="jumbotron">
			<h3 class="text-center">
				<i class="fa fa-check-circle color-green"></i> Cadastro realizado com sucesso!
			</h3>
			<h3 class="text-center">
				<small>Logo seu usuário será ativado.</small>
			</h3>
		</div>
	</c:when>
	<c:when test="${activateSuccess}">
		<div class="jumbotron">
			<h3 class="text-center">
				<i class="fa fa-check-circle color-green"></i> Parceiro ativado com sucesso!
			</h3>
		</div>
	</c:when>
</c:choose>

<div class="jumbotron">
	<form id="estabelecimento-form" method="post" action="${__contextPath__}/estabelecimento${fn:escapeXml((estabelecimento != null ? editAction : ''))}" class="form-horizontal" role="form">
		<fieldset>
			<legend><i class="fa fa-user fa-fw"></i> Novo cadastro</legend>
			<c:if test="${u:hasPerfilForRequest(pageContext.request, 'ADMIN')}">
				<div class="form-group">
		            <label for="estabelecimento-pai" class="col-md-2 control-label">Matriz: </label>
		            <div class="col-md-10">
		                <select class="form-control" name="pai.pessoaId" id="estabelecimento-pai" ${readonly ? 'disabled' : ''}>
		                	<option></option>
		                	<c:forEach items="${estabelecimentos}" var="currentEstabelecimento">
		                		<c:if test="${currentEstabelecimento.pessoaId != estabelecimento.pessoaId}">
		                			<option value="${fn:escapeXml((currentEstabelecimento.pessoaId))}" ${estabelecimento != null ? estabelecimento.pai.pessoaId == currentEstabelecimento.pessoaId ? 'selected' : '' : ''}>${fn:escapeXml((currentEstabelecimento.nomeFantasia))} (${currentEstabelecimento.cnpj != null ? s:format(fn:escapeXml((currentEstabelecimento.cnpj)), '##.###.###/####-##') : currentEstabelecimento.cpf != null ? s:format(fn:escapeXml((currentEstabelecimento.cpf)), '###.###.###-##') : fn:escapeXml((currentEstabelecimento.idEstrangeiro))})</option>
		                		</c:if>
		                	</c:forEach>
		                </select>
		            </div>
		        </div>
			</c:if>
			<div class="form-group ${estabelecimento.pai != null ? 'hide' : ''}">
		    	<label for="estabelecimento-nome-fantasia" class="col-md-2 control-label">Nome fantasia: </label>
		        <div class="col-md-10">
		            <input type="text" class="form-control" name="nomeFantasia" id="estabelecimento-nome-fantasia" autofocus value="${fn:escapeXml((estabelecimento.nomeFantasia))}" ${readonly ? 'disabled' : ''} />
		        </div>
			</div>
			<div class="form-group">
		    	<label for="estabelecimento-razao-social" class="col-md-2 control-label">Razão social: </label>
		        <div class="col-md-10">
		            <input type="text" class="form-control" name="razaoSocial" id="estabelecimento-razao-social" ${estabelecimento.pai != null ? 'autofocus' : ''} value="${fn:escapeXml((estabelecimento.razaoSocial))}" ${readonly ? 'disabled' : ''} />
		        </div>
			</div>
			<div class="form-group">
		        <label for="estabelecimento-cnpj" class="col-md-2 control-label">CNPJ: </label>
		        <div class="col-md-2">
		            <input type="text" class="form-control iptCnpj" name="cnpj" id="estabelecimento-cnpj" data-mask="99.999.999/9999-99" value="${fn:escapeXml((estabelecimento.cnpj))}" ${estabelecimento.cpf != null || estabelecimento.idEstrangeiro != null || readonly ? 'disabled': ''} />
		        </div>
		        <label for="estabelecimento-cpf" class="col-md-2 control-label">CPF: </label>
		        <div class="col-md-2">
		            <input type="text" class="form-control iptCpf" name="cpf" id="estabelecimento-cpf" data-mask="999.999.999-99" value="${fn:escapeXml((estabelecimento.cpf))}" ${estabelecimento.cnpj != null || estabelecimento.idEstrangeiro != null || readonly ? 'disabled': ''} />
		        </div>
		        <label for="estabelecimento-id-estrangeiro" class="col-md-2 control-label">ID Estrangeiro: </label>
		        <div class="col-md-2">
		            <input type="text" class="form-control iptIdEstrangeiro" name="idEstrangeiro" id="estabelecimento-id-estrangeiro" value="${fn:escapeXml((estabelecimento.idEstrangeiro))}" ${estabelecimento.cnpj != null || estabelecimento.cpf != null || readonly ? 'disabled': ''} />
		        </div>
		    </div>
		</fieldset>
		<fieldset>
			<legend><i class="fa fa-home fa-fw"></i> Endereço</legend>
			<div class="form-groups">
			    <div class="form-group">
			        <label for="estabelecimento-endereco-cep" class="col-md-2 control-label">CEP: </label>
			        <div class="col-md-10">
			            <input type="text" class="form-control iptCep" name="endereco.cep" id="estabelecimento-endereco-cep" data-mask="99999-999" value="${fn:escapeXml((estabelecimento.endereco.cep))}" ${readonly ? 'disabled' : ''} />
			        </div>
			    </div>
			    <div class="form-group">
			        <label for="estabelecimento-endereco-estado" class="col-md-2 control-label">Estado: </label>
			        <div class="col-md-2">
			            <select class="form-control sltEstado" name="endereco.cidade.estado.estadoId" id="estabelecimento-endereco-estado" ${readonly ? 'disabled' : ''} />
			            	<option></option>
			            	<c:forEach items="${estados}" var="estado">
							    <option value="${fn:escapeXml((estado.estadoId))}" ${estabelecimento.endereco.cidade.estado.estadoId == estado.estadoId ? 'selected' : ''}>${fn:escapeXml((estado.nome))}</option>
							</c:forEach>
			            </select>
			        </div>
			        <label for="estabelecimento-endereco-cidade" class="col-md-1 control-label">Cidade: </label>
			        <div class="col-md-3">
			      		<select class="form-control sltCidade" name="endereco.cidade.cidadeId" id="estabelecimento-endereco-cidade" ${readonly ? 'disabled' : ''}>
			            	<option></option>
			            	<c:forEach items="${cidades}" var="cidade">
			            		<option value="${fn:escapeXml((cidade.cidadeId))}" ${estabelecimento.endereco.cidade.cidadeId == cidade.cidadeId ? 'selected' : ''}>${fn:escapeXml((cidade.nome))}</option>
			            	</c:forEach>
			            </select>
			        </div>
			        <label for="estabelecimento-endereco-bairro" class="col-md-1 control-label">Bairro: </label>
			        <div class="col-md-3">
			            <input type="text" class="form-control iptBairro" name="endereco.bairro" id="estabelecimento-endereco-bairro" value="${fn:escapeXml((estabelecimento.endereco.bairro))}" ${readonly ? 'disabled' : ''} />
			        </div>
			    </div>
			    <div class="form-group">
			        <label for="estabelecimento-endereco-logradouro" class="col-md-2 control-label">Logradouro: </label>
			        <div class="col-md-3">
			            <input type="text" class="form-control iptLogradouro" name="endereco.logradouro" id="estabelecimento-endereco-logradouro" value="${fn:escapeXml((estabelecimento.endereco.logradouro))}" ${readonly ? 'disabled' : ''} />
			        </div>
			        <label for="estabelecimento-endereco-numero" class="col-md-1 control-label">Número: </label>
			        <div class="col-md-2">
			            <input type="text" class="form-control" name="endereco.numero" id="estabelecimento-endereco-numero" value="${fn:escapeXml((estabelecimento.endereco.numero))}" ${readonly ? 'disabled' : ''} />
			        </div>
			        <label for="estabelecimento-endereco-complemento" class="col-md-1 control-label">Compl.: </label>
			        <div class="col-md-3">
			            <input type="text" class="form-control" name="endereco.complemento" id="estabelecimento-endereco-complemento" value="${fn:escapeXml((estabelecimento.endereco.complemento))}" ${readonly ? 'disabled' : ''} />
			        </div>
			    </div>
		    </div>
		    <input class="iptPais" name="endereco.cidade.estado.pais.paisId" id="estabelecimento-endereco-pais" type="hidden" value="BR" />
		</fieldset>
		<fieldset>
			<legend><i class="fa fa-phone-square fa-fw"></i> Contato</legend>
			<div class="form-groups">
			    <div class="form-group">
			        <label for="estabelecimento-telefone-1" class="col-md-2 control-label">Telefone: </label>
			        <div class="col-md-4">
			            <input type="text" class="form-control" name="telefone1" id="estabelecimento-telefone-1" data-mask="+55 (99) 99999999?9" value="${fn:escapeXml((estabelecimento.telefone1))}" ${readonly ? 'disabled' : ''} />
			        </div>
			        <label for="estabelecimento-telefone-2" class="col-md-2 control-label">Telefone alternativo: </label>
			        <div class="col-md-4">
			            <input type="text" class="form-control" name="telefone2" id="estabelecimento-telefone-2" data-mask="+55 (99) 99999999?9" value="${fn:escapeXml((estabelecimento.telefone2))}" ${readonly ? 'disabled' : ''} />
			        </div>
			    </div>
			    <div class="form-group">
			        <label for="estabelecimento-email" class="col-md-2 control-label">Email: </label>
			        <div class="col-md-10">
			            <input type="text" class="form-control" name="email" id="estabelecimento-email" value="${fn:escapeXml((estabelecimento.email))}" ${readonly ? 'disabled' : ''} />
			        </div>
			    </div>
		    </div>
		</fieldset>
		<c:choose>
			<c:when test="${estabelecimento == null}">
				<fieldset>
					<legend><i class="fa fa-user fa-fw"></i> Usuário</legend>
					<div class="form-group">
				        <label for="estabelecimento-usuario-apelido" class="col-md-2 control-label">Usuário: </label>
				        <div class="col-md-4">
				            <input type="text" class="form-control" name="usuarios[0].apelido" id="estabelecimento-usuario-apelido" />
				        </div>
				        <label for="estabelecimento-usuario-senha" class="col-md-2 control-label">Senha: </label>
				        <div class="col-md-4">
				            <input type="password" class="form-control" name="usuarios[0].senha" id="estabelecimento-usuario-senha" />
				        </div>
				    </div>
				    <hr />
				    <div class="form-group">
				    	<div class="col-md-offset-2 col-md-10">
				    		<button type="submit" class="btn btn-green"><i class="fa fa-floppy-o"></i> Cadastrar</button>
				    	</div>
				    </div>
				</fieldset>
			</c:when>
			<c:otherwise>
				<c:if test="${u:hasPerfilForRequest(pageContext.request, 'ESTABELECIMENTO_ESCRITA')}">
					<hr />
					<div class="form-group">
						<div class="col-md-offset-2 col-md-10">
				    		<button type="submit" class="btn btn-green"><i class="fa fa-edit"></i> Editar</button>
				    		<c:if test="${u:hasPerfilForRequest(pageContext.request, 'ADMIN') && !estabelecimentoActivated}">
				    			<button type="button" id="estabelecimento-activate" class="btn btn-info"><i class="fa fa-check"></i> Ativar</button>
				    		</c:if>
				    	</div>
					</div>
				</c:if>
			</c:otherwise>
		</c:choose>
	</form>
</div>

<script src="${__contextPath__}/assets/js/endereco.js?v=${__appVersion__}"></script>
<script src="${__contextPath__}/assets/js/pessoa.js?v=${__appVersion__}"></script>
<script src="${__contextPath__}/assets/js/estabelecimento.js?v=${__appVersion__}"></script>
