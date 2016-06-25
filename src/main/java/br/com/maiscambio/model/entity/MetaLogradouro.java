package br.com.maiscambio.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "META_LOGRADOURO", schema = "public")
public class MetaLogradouro extends BaseEntity
{
	private static final long serialVersionUID = -3812746097187902870L;
	
	@Id
	@Column(name = "CEP")
	private String cep;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_BAIRRO", insertable = false, updatable = false)
	private MetaBairro metaBairro;
	
	@Column(name = "LOGRADOURO")
	private String logradouro;
	
	@Column(name = "TIPO_LOGRADOURO")
	private String tipoLogradouro;
	
	@Column(name = "COMPLEMENTO")
	private String complemento;
	
	@Column(name = "LOCAL")
	private String local;
	
	public String getCep()
	{
		return cep;
	}
	
	public void setCep(String cep)
	{
		this.cep = cep;
	}
	
	public MetaBairro getMetaBairro()
	{
		return metaBairro;
	}
	
	public void setMetaBairro(MetaBairro metaBairro)
	{
		this.metaBairro = metaBairro;
	}
	
	public String getLogradouro()
	{
		return logradouro;
	}
	
	public void setLogradouro(String logradouro)
	{
		this.logradouro = logradouro;
	}
	
	public String getTipoLogradouro()
	{
		return tipoLogradouro;
	}
	
	public void setTipoLogradouro(String tipoLogradouro)
	{
		this.tipoLogradouro = tipoLogradouro;
	}
	
	public String getComplemento()
	{
		return complemento;
	}
	
	public void setComplemento(String complemento)
	{
		this.complemento = complemento;
	}
	
	public String getLocal()
	{
		return local;
	}
	
	public void setLocal(String local)
	{
		this.local = local;
	}
}
