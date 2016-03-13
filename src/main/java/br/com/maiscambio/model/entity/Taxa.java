package br.com.maiscambio.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TAXA")
public class Taxa extends BaseEntity
{
	private static final long serialVersionUID = -5084058008269562708L;
	
	public static enum Moeda
	{
		USD, EUR, GBP, CAD, AUD, CLP, ARS, CHF, ZAR, MXN, UYU, JPY
	}
	
	public static enum Status
	{
		ATIVO, INATIVO
	}
	
	public static enum Finalidade
	{
		COMPRA, VENDA
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TAXA")
	private Long taxaId;
	private Estabelecimento estabelecimento;
	private BigDecimal valorEspecie;
	private BigDecimal valorCartao;
	private Moeda moeda;
	private Date data;
	private Status status;
	private Finalidade finalidade;
	
	public Long getTaxaId()
	{
		return taxaId;
	}
	
	public void setTaxaId(Long taxaId)
	{
		this.taxaId = taxaId;
	}
	
	public Estabelecimento getEstabelecimento()
	{
		return estabelecimento;
	}
	
	public void setEstabelecimento(Estabelecimento estabelecimento)
	{
		this.estabelecimento = estabelecimento;
	}
	
	public BigDecimal getValorEspecie()
	{
		return valorEspecie;
	}
	
	public void setValorEspecie(BigDecimal valorEspecie)
	{
		this.valorEspecie = valorEspecie;
	}
	
	public BigDecimal getValorCartao()
	{
		return valorCartao;
	}
	
	public void setValorCartao(BigDecimal valorCartao)
	{
		this.valorCartao = valorCartao;
	}
	
	public Moeda getMoeda()
	{
		return moeda;
	}
	
	public void setMoeda(Moeda moeda)
	{
		this.moeda = moeda;
	}
	
	public Date getData()
	{
		return data;
	}
	
	public void setData(Date data)
	{
		this.data = data;
	}
	
	public Status getStatus()
	{
		return status;
	}
	
	public void setStatus(Status status)
	{
		this.status = status;
	}
	
	public Finalidade getFinalidade()
	{
		return finalidade;
	}
	
	public void setFinalidade(Finalidade finalidade)
	{
		this.finalidade = finalidade;
	}
}
