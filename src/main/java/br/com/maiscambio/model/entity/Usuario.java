package br.com.maiscambio.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.gerenciar.util.BaseEntity;

@Entity
@Table(name = "USUARIO")
public class Usuario extends BaseEntity
{
	public static enum Status
	{
		ATIVO, INATIVO
	}
	
	public static enum Perfil
	{
		ADMIN, ESTABELECIMENTO_ESCRITA, ESTABELECIMENTO_LEITURA, ESTABELECIMENTO_DASHBOARD_LEITURA, ESTABELECIMENTO_TAXA_ESCRITA, ESTABELECIMENTO_TAXA_LEITURA, ESTABELECIMENTO_USUARIO_ESCRITA, ESTABELECIMENTO_USUARIO_LEITURA
	}
	
	private static final long serialVersionUID = 5785151691588649542L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_USUARIO")
	private Long usuarioId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PESSOA")
	private Pessoa pessoa;
	
	@Column(name = "APELIDO")
	private String apelido;
	
	@Column(name = "SENHA")
	private String senha;
	
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ElementCollection(targetClass = Perfil.class, fetch = FetchType.EAGER)
	@JoinTable(name = "USUARIO_USUARIO_PERFIL", joinColumns = @JoinColumn(name = "ID_USUARIO"))
	@Enumerated(EnumType.STRING)
	@Column(name = "PERFIL")
	private List<Perfil> perfis = new ArrayList<>();
	
	public Long getUsuarioId()
	{
		return usuarioId;
	}
	
	public void setUsuarioId(Long usuarioId)
	{
		this.usuarioId = usuarioId;
	}
	
	public Pessoa getPessoa()
	{
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa)
	{
		this.pessoa = pessoa;
	}
	
	public String getApelido()
	{
		return apelido;
	}
	
	public void setApelido(String apelido)
	{
		this.apelido = apelido;
	}
	
	@JsonIgnore
	public String getSenha()
	{
		return senha;
	}
	
	@JsonProperty
	public void setSenha(String senha)
	{
		this.senha = senha;
	}
	
	public Status getStatus()
	{
		return status;
	}
	
	public void setStatus(Status status)
	{
		this.status = status;
	}
	
	public List<Perfil> getPerfis()
	{
		return perfis;
	}
	
	public void setPerfis(List<Perfil> perfis)
	{
		this.perfis = perfis;
	}
}
