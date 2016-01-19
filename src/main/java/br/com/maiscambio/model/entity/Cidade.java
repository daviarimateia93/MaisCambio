package br.com.maiscambio.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CIDADE", schema = "public")
public class Cidade extends BaseEntity
{
	private static final long serialVersionUID = -5294780538052256477L;
	
	@Id
	@Column(name = "ID_CIDADE")
	private Long cidadeId;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "ID_ESTADO"), @JoinColumn(name = "ID_PAIS") })
	private Estado estado;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "IBGE")
	private String ibge;
	
	@Column(name = "AREA")
	private Float area;
	
	public Long getCidadeId()
	{
		return cidadeId;
	}
	
	public void setCidadeId(Long cidadeId)
	{
		this.cidadeId = cidadeId;
	}
	
	public Estado getEstado()
	{
		return estado;
	}
	
	public void setEstado(Estado estado)
	{
		this.estado = estado;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	
	public String getIbge()
	{
		return ibge;
	}
	
	public void setIbge(String ibge)
	{
		this.ibge = ibge;
	}
	
	public Float getArea()
	{
		return area;
	}
	
	public void setArea(Float area)
	{
		this.area = area;
	}
}
