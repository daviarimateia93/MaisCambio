<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="${__contextPath__}/assets/css/home.css?v=${__appVersion__}" rel="stylesheet">

<div class="jumbotron">
	<h3 class="text-center">Encontre as melhores taxas de câmbio, negocie e fique por dentro das novidades do mercado!</h3>
	<hr />
	<form class="form-inline col-xs-12">
	  	<div class="form-group col-sm-6">
	  		<div class="input-group col-xs-12">
		    	<div class="input-group-addon width-30">
		    		<label class="margin-bottom-0" for="moeda-chooser">
		    			<i class="fa fa-usd fa-lg"></i>
		    		</label>
		    	</div>
		    	<select id="moeda-chooser" class="form-control">
		    		<option value="" disabled selected>Preciso de...</option>
		    		<option value="DOLAR">Dólar</option>
		    		<option value="EURO">Euro</option>
		    		<option value="LIBRA">Libra</option>
		    	</select>
		    </div>
	  	</div>
	  	<div class="form-group col-sm-6">
	  		<div class="input-group col-xs-12">
		  		<div class="input-group-addon width-30">
		    		<label class="margin-bottom-0" for="estado-chooser">
		    			<i class="fa fa-map-marker fa-lg"></i>
		    		</label>
		    	</div>
		    	<select id="estado-chooser" class="form-control">
		    		<option value="" disabled selected>Estou em...</option>
		    		<option value="SP">São Paulo</option>
		    		<option value="RJ">Rio de Janeiro</option>
		    		<option value="MG">Minas Gerais</option>
		    	</select>
	    	</div>
	  	</div>
	  	<div class="form-group col-xs-12 text-center margin-top-40">
	  		<button type="submit" class="btn btn-lg btn-green">
	  			<i class="fa fa-search"></i> Pesquisar
	  		</button>
	  	</div>
	</form>
	<div class="clearfix"></div>
</div>

<div id="taxas-boxes-container">
	<div class="taxa-box-container col-xs-6 col-sm-4 col-md-3 col-lg-3">
		<a href="javascript: void(0);">
			<div class="taxa-box">
				<div class="icon text-center">
					<img src="${__contextPath__}/assets/img/icon_user_metro.png" alt="nome empresa" title="nome empresa" />
				</div>
				<div class="valor">
					<hr />
					<div class="row">
						<div class="col-xs-6 text-center">
							<i class="fa fa-money fa-2x"></i>
						</div>
						<div class="col-xs-6 text-center">
							<i class="fa fa-credit-card fa-2x"></i>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 text-center especie">R$ 4,13</div>
						<div class="col-xs-6 text-center cartao">R$ 4,16</div>
					</div>
				</div>
				<div class="cover"></div>
			</div>
		</a>
	</div>
	
	<div class="taxa-box-container col-xs-6 col-sm-4 col-md-3 col-lg-3">
		<a href="javascript: void(0);">
			<div class="taxa-box">
				<div class="icon text-center">
					<img src="${__contextPath__}/assets/img/icon_user_metro.png" alt="nome empresa" title="nome empresa" />
				</div>
				<div class="valor">
					<hr />
					<div class="row">
						<div class="col-xs-6 text-center">
							<i class="fa fa-money fa-2x"></i>
						</div>
						<div class="col-xs-6 text-center">
							<i class="fa fa-credit-card fa-2x"></i>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 text-center especie">R$ 4,13</div>
						<div class="col-xs-6 text-center cartao">R$ 4,16</div>
					</div>
				</div>
				<div class="cover"></div>
			</div>
		</a>
	</div>
	
	<div class="taxa-box-container col-xs-6 col-sm-4 col-md-3 col-lg-3">
		<a href="javascript: void(0);">
			<div class="taxa-box">
				<div class="icon text-center">
					<img src="${__contextPath__}/assets/img/icon_user_metro.png" alt="nome empresa" title="nome empresa" />
				</div>
				<div class="valor">
					<hr />
					<div class="row">
						<div class="col-xs-6 text-center">
							<i class="fa fa-money fa-2x"></i>
						</div>
						<div class="col-xs-6 text-center">
							<i class="fa fa-credit-card fa-2x"></i>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 text-center especie">R$ 4,13</div>
						<div class="col-xs-6 text-center cartao">R$ 4,16</div>
					</div>
				</div>
				<div class="cover"></div>
			</div>
		</a>
	</div>
	
	<div class="taxa-box-container col-xs-6 col-sm-4 col-md-3 col-lg-3">
		<a href="javascript: void(0);">
			<div class="taxa-box">
				<div class="icon text-center">
					<img src="${__contextPath__}/assets/img/icon_user_metro.png" alt="nome empresa" title="nome empresa" />
				</div>
				<div class="valor">
					<hr />
					<div class="row">
						<div class="col-xs-6 text-center">
							<i class="fa fa-money fa-2x"></i>
						</div>
						<div class="col-xs-6 text-center">
							<i class="fa fa-credit-card fa-2x"></i>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 text-center especie">R$ 4,13</div>
						<div class="col-xs-6 text-center cartao">R$ 4,16</div>
					</div>
				</div>
				<div class="cover"></div>
			</div>
		</a>
	</div>
	
	<div class="taxa-box-container col-xs-6 col-sm-4 col-md-3 col-lg-3">
		<a href="javascript: void(0);">
			<div class="taxa-box">
				<div class="icon text-center">
					<img src="${__contextPath__}/assets/img/icon_user_metro.png" alt="nome empresa" title="nome empresa" />
				</div>
				<div class="valor">
					<hr />
					<div class="row">
						<div class="col-xs-6 text-center">
							<i class="fa fa-money fa-2x"></i>
						</div>
						<div class="col-xs-6 text-center">
							<i class="fa fa-credit-card fa-2x"></i>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 text-center especie">R$ 4,13</div>
						<div class="col-xs-6 text-center cartao">R$ 4,16</div>
					</div>
				</div>
				<div class="cover"></div>
			</div>
		</a>
	</div>
	
	<div class="taxa-box-container col-xs-6 col-sm-4 col-md-3 col-lg-3">
		<a href="javascript: void(0);">
			<div class="taxa-box">
				<div class="icon text-center">
					<img src="${__contextPath__}/assets/img/icon_user_metro.png" alt="nome empresa" title="nome empresa" />
				</div>
				<div class="valor">
					<hr />
					<div class="row">
						<div class="col-xs-6 text-center">
							<i class="fa fa-money fa-2x"></i>
						</div>
						<div class="col-xs-6 text-center">
							<i class="fa fa-credit-card fa-2x"></i>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-6 text-center especie">R$ 4,13</div>
						<div class="col-xs-6 text-center cartao">R$ 4,16</div>
					</div>
				</div>
				<div class="cover"></div>
			</div>
		</a>
	</div>
</div>

<script src="${__contextPath__}/assets/js/home.js?v=${__appVersion__}"></script>
