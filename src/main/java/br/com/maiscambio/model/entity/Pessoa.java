package br.com.maiscambio.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PESSOA")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa extends BaseEntity
{
	private static final long serialVersionUID = -6313757603493280647L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PESSOA")
	private Long pessoaId;
	
	@Column(name = "CNPJ")
	private String cnpj;
	
	@Column(name = "CPF")
	private String cpf;
	
	@Column(name = "ID_ESTRANGEIRO")
	private String idEstrangeiro;
	
	@Column(name = "TELEFONE_1")
	private String telefone1;
	
	@Column(name = "TELEFONE_2")
	private String telefone2;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ENDERECO")
	private Endereco endereco;
	
	@Column(name = "EMAIL")
	private String email;
	
	@JsonIgnore
	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "pessoa", orphanRemoval = true)
	private List<Usuario> usuarios = new ArrayList<>();
	
	public Long getPessoaId()
	{
		return pessoaId;
	}
	
	public void setPessoaId(Long pessoaId)
	{
		this.pessoaId = pessoaId;
	}
	
	public String getCnpj()
	{
		return cnpj;
	}
	
	public void setCnpj(String cnpj)
	{
		this.cnpj = cnpj;
	}
	
	public String getCpf()
	{
		return cpf;
	}
	
	public void setCpf(String cpf)
	{
		this.cpf = cpf;
	}
	
	public String getIdEstrangeiro()
	{
		return idEstrangeiro;
	}
	
	public void setIdEstrangeiro(String idEstrangeiro)
	{
		this.idEstrangeiro = idEstrangeiro;
	}
	
	public String getTelefone1()
	{
		return telefone1;
	}
	
	public void setTelefone1(String telefone1)
	{
		this.telefone1 = telefone1;
	}
	
	public String getTelefone2()
	{
		return telefone2;
	}
	
	public void setTelefone2(String telefone2)
	{
		this.telefone2 = telefone2;
	}
	
	public Endereco getEndereco()
	{
		return endereco;
	}
	
	public void setEndereco(Endereco endereco)
	{
		this.endereco = endereco;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public List<Usuario> getUsuarios()
	{
		return usuarios;
	}
	
	public void setUsuarios(List<Usuario> usuarios)
	{
		setList("usuarios", usuarios);
	}
}
