package br.com.maiscambio.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.maiscambio.Autenticacao;
import br.com.maiscambio.Perfil;
import br.com.maiscambio.model.entity.Estabelecimento;
import br.com.maiscambio.model.entity.Iof;
import br.com.maiscambio.model.entity.Taxa;
import br.com.maiscambio.model.entity.Usuario;
import br.com.maiscambio.model.service.UsuarioService;
import br.com.maiscambio.util.View;

@Controller
@RequestMapping("/dashboard")
public class DashboardController extends BaseController
{
	@Transactional(readOnly = true)
	@RequestMapping(method = RequestMethod.GET)
	@Autenticacao(@Perfil(Usuario.Perfil.ESTABELECIMENTO_DASHBOARD_LEITURA))
	public View index(@RequestParam(required = false) String selectedFormattedDate)
	{
		Usuario usuario = getUsuarioService().getFromRequest(getRequest());
		Estabelecimento estabelecimento = getEstabelecimentoFromRequest();
		
		View view = view("full", "dashboard", "Dashboard");
		
		if(UsuarioService.hasPerfil(usuario, Usuario.Perfil.ADMIN))
		{
			view.addObject("estabelecimentos", getEstabelecimentoService().findByUsuarioStatusWherePaiIsNullAndUsuariosSizeIsOne(Usuario.Status.INATIVO));
			view.addObject("iofCompra", getIofService().findLastByFinalidade(Iof.Finalidade.COMPRA));
			view.addObject("iofVenda", getIofService().findLastByFinalidade(Iof.Finalidade.VENDA));
		}
		
		if(UsuarioService.hasPerfil(usuario, Usuario.Perfil.ESTABELECIMENTO_TAXA_LEITURA) && estabelecimento != null)
		{
			List<String> moedas = getTaxaService().getMoedasAsStringList();
			
			Map<String, Taxa> taxasVenda = new HashMap<>();
			Map<String, Taxa> taxasCompra = new HashMap<>();
			
			for(String moeda : moedas)
			{
				taxasVenda.put(moeda, getTaxaService().findLastByMoedaAndFinalidade(estabelecimento.getPessoaId(), Taxa.Moeda.valueOf(moeda), Taxa.Finalidade.VENDA));
				taxasCompra.put(moeda, getTaxaService().findLastByMoedaAndFinalidade(estabelecimento.getPessoaId(), Taxa.Moeda.valueOf(moeda), Taxa.Finalidade.COMPRA));
			}
			
			view.addObject("moedas", moedas);
			view.addObject("taxasVenda", taxasVenda);
			view.addObject("taxasCompra", taxasCompra);
		}
		
		return view;
	}
}
