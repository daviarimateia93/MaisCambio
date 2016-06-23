package br.com.maiscambio.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import me.gerenciar.util.BaseEntity;
import me.gerenciar.util.DateHelper;

@Entity
@Table(name = "IOF")
public class Iof extends BaseEntity
{
	private static final long serialVersionUID = -1791108994056442734L;
	
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
	@Column(name = "ID_IOF")
	private Long iofId;
	
	@Column(name = "VALOR_ESPECIE")
	private BigDecimal valorEspecie;
	
	@Column(name = "VALOR_CARTAO")
	private BigDecimal valorCartao;
	
	@DateTimeFormat(pattern = DateHelper.DATE_TIME_FORMAT_PATTERN)
	@Column(name = "DATA")
	private Date data;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private Status status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FINALIDADE")
	private Finalidade finalidade;
	
	public Long getIofId()
	{
		return iofId;
	}
	
	public void setIofId(Long iofId)
	{
		this.iofId = iofId;
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
