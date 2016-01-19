package br.com.maiscambio.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "META_BAIRRO", schema = "public")
public class MetaBairro extends BaseEntity
{
	private static final long serialVersionUID = -2584859867233414260L;
	
	@Id
	@Column(name = "ID_BAIRRO")
	private Long bairroId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CIDADE", insertable = false, updatable = false)
	private Cidade cidade;
	
	@Column(name = "NOME")
	private String nome;
	
	public Long getBairroId()
	{
		return bairroId;
	}
	
	public void setBairroId(Long bairroId)
	{
		this.bairroId = bairroId;
	}
	
	public Cidade getCidade()
	{
		return cidade;
	}
	
	public void setCidade(Cidade cidade)
	{
		this.cidade = cidade;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
}
