package br.com.maiscambio.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ENDERECO")
public class Endereco extends BaseEntity
{
	private static final long serialVersionUID = 6621702869420748115L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ENDERECO")
	private Long enderecoId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CIDADE")
	private Cidade cidade;
	
	@Column(name = "BAIRRO")
	private String bairro;
	
	@Column(name = "LOGRADOURO")
	private String logradouro;
	
	@Column(name = "COMPLEMENTO")
	private String complemento;
	
	@Column(name = "NUMERO")
	private String numero;
	
	@Column(name = "CEP")
	private String cep;
	
	public Long getEnderecoId()
	{
		return enderecoId;
	}
	
	public void setEnderecoId(Long enderecoId)
	{
		this.enderecoId = enderecoId;
	}
	
	public Cidade getCidade()
	{
		return cidade;
	}
	
	public void setCidade(Cidade cidade)
	{
		this.cidade = cidade;
	}
	
	public String getBairro()
	{
		return bairro;
	}
	
	public void setBairro(String bairro)
	{
		this.bairro = bairro;
	}
	
	public String getLogradouro()
	{
		return logradouro;
	}
	
	public void setLogradouro(String logradouro)
	{
		this.logradouro = logradouro;
	}
	
	public String getComplemento()
	{
		return complemento;
	}
	
	public void setComplemento(String complemento)
	{
		this.complemento = complemento;
	}
	
	public String getNumero()
	{
		return numero;
	}
	
	public void setNumero(String numero)
	{
		this.numero = numero;
	}
	
	public String getCep()
	{
		return cep;
	}
	
	public void setCep(String cep)
	{
		this.cep = cep;
	}
}
