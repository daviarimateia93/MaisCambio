package br.com.maiscambio.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.maiscambio.model.entity.primarykey.EstadoPrimaryKey;

@Entity
@IdClass(EstadoPrimaryKey.class)
@Table(name = "ESTADO", schema = "public")
public class Estado extends BaseEntity
{
	private static final long serialVersionUID = -7725856257917944716L;
	
	@Id
	@Column(name = "ID_ESTADO")
	private String estadoId;
	
	@Id
	@Column(name = "ID_PAIS")
	private String paisId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PAIS", insertable = false, updatable = false)
	private Pais pais;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "IBGE")
	private String ibge;
	
	public String getEstadoId()
	{
		return estadoId;
	}
	
	public void setEstadoId(String estadoId)
	{
		this.estadoId = estadoId;
	}
	
	public String getPaisId()
	{
		return paisId;
	}
	
	public void setPaisId(String paisId)
	{
		this.paisId = paisId;
	}
	
	public Pais getPais()
	{
		return pais;
	}
	
	public void setPais(Pais pais)
	{
		this.pais = pais;
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
}
