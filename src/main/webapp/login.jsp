<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="context" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html>
<head>
	<title>Sistema de Ouvidoria</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="description" content="" />
	<meta name="author" content="" />
	<meta http-equiv="refresh" content="3600;url=/ouvidoria" />
	<link rel="stylesheet" href="${context}/resources/css/modal.css" />
	<link rel="stylesheet" href="${context}/resources/css/estilo.css" />
	<link rel="stylesheet" href="${context}/resources/css/estiloGoogle.css" />
	<script src="${context}/resources/js/jquery.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$.get("/ouvidoria/aviso", function(data) {
				if(data.titulo) {
					$("#avisoTitulo").append(data.titulo);
					$("#avisoConteudo").append(data.conteudo);
					$(".modal").show();
				}
			}, "json" );
		})
	</script>
</head>

<body>
	<div class="global">	
		<div class="bgSistemaOuvidoria">
			<!-- conteúdo Login -->
			<form method="post" action="${context}/login">
				<div class="conteudo">
                    <div class="logo-externa">
                        <img src="${context}/img/logo-ouvidoria.png" />
                    </div>
					<div class="combo-login">
						<div class="topoLoginBox"></div>
						<div class="conteudoLogin">
							<div class="mensagem">
								<ul>
									<c:forEach var="error" items="${errors}">
										<li>
											<c:out value="${error}" />
										</li>
									</c:forEach>
									<c:choose>
										<c:when test="${param.logout == 'expired'}">
											<li>Sessão Expirada</li>
										</c:when>
									</c:choose>
								</ul>
							</div>
							<div class="panelLogin">
								<div class="panelLoginConteudo">
									<div class="alingBox">
										<div class="loginBox">
											<label for="username" style="font-family: 'Open Sans'; font-size: 14px">Login:</label>&nbsp;&nbsp;
											<br />
											<input type="text" name="username" style="font-family: sans-serif; font-size: 12px" size="24" />
										</div>
										<div class="loginBoxPw">
											<label for="password" style="font-family: 'Open Sans'; font-size: 14px">Senha:</label>&nbsp;&nbsp;
											<br />
											<input type="password" name="password" style="font-family: sans-serif; font-size: 12px" size="24" />
										</div>
									</div>
	
									<div class="alingButton">
										<div class="rSenha">
											<a href="${context}/recuperarsenha.xhtml">Recuperar Senha</a>
										</div>
										<div class="dadosCadastro1">
											<button type="submit" style="border: none;"
													class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left">
												<span class="ui-button-text">ACESSAR</span>
											</button>
										</div>
									</div>
									<div class="cadastroUsuariosOuvidoria">
										Não é cadastrado?
										<a href="${context}/pages/externo/cadastrarManifestante.xhtml">Cadastre-se</a>
									</div>
								</div>
							</div>
						</div>

						<div class="dadosCadastro2">
							<div class="notch"></div>
							<div class="botaoCadastro">
								<img src="${context}/img/addManifestacao.png" />
								<a href="${context}/pages/manifestacao/cadastrar.xhtml">Cadastrar Manifestação</a>
							</div>
							<br />
							<p>
								À Ouvidoria compete receber, examinar e encaminhar as
								reclamações, sugestões, elogios, denúncias e solicitação de
								informações, especialmente aquelas que demandam maior urgência.
							</p>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	
	<div class="modal" id="avisoModal">
		<div class="modal-content">
			<div class="modal-header">
		        <button type="button" class="close" onclick="$('.modal').hide();"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="avisoTitulo"></h4>
			</div>
			<div class="modal-body"><div id="avisoConteudo"></div></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" onclick="$('.modal').hide();">Fechar</button>
			</div>
		</div>
	</div>
	
</body>
</html>
