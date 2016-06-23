package br.com.maiscambio.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import me.gerenciar.util.DateHelper;

@Entity
@Table(name = "ESTABELECIMENTO")
@PrimaryKeyJoinColumn(name = "ID_PESSOA")
public class Estabelecimento extends Pessoa
{
	private static final long serialVersionUID = -5586424328321297612L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PESSOA_PAI")
	private Estabelecimento pai;
	
	@JsonIgnore
	@OneToMany(mappedBy = "pai", fetch = FetchType.EAGER)
	private List<Estabelecimento> filhos = new ArrayList<>();
	
	@Column(name = "RAZAO_SOCIAL")
	private String razaoSocial;
	
	@Column(name = "NOME_FANTASIA")
	private String nomeFantasia;
	
	@DateTimeFormat(pattern = DateHelper.DATE_TIME_FORMAT_PATTERN)
	@Column(name = "DATA")
	private Date data;
	
	@Column(name = "TIME_ZONE")
	private String timeZone;
	
	@Fetch(FetchMode.SELECT)
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "ESTABELECIMENTO_LICENCA", joinColumns = @JoinColumn(name = "ID_PESSOA"))
	@Column(name = "LICENCA")
	private List<String> licencas = new ArrayList<>();
	
	public Estabelecimento getPai()
	{
		return pai;
	}
	
	public void setPai(Estabelecimento pai)
	{
		this.pai = pai;
	}
	
	public List<Estabelecimento> getFilhos()
	{
		return filhos;
	}
	
	public String getRazaoSocial()
	{
		return razaoSocial;
	}
	
	public void setRazaoSocial(String razaoSocial)
	{
		this.razaoSocial = razaoSocial;
	}
	
	public String getNomeFantasia()
	{
		return nomeFantasia;
	}
	
	public void setNomeFantasia(String nomeFantasia)
	{
		this.nomeFantasia = nomeFantasia;
	}
	
	public Date getData()
	{
		return data;
	}
	
	public void setData(Date data)
	{
		this.data = data;
	}
	
	public String getTimeZone()
	{
		return timeZone;
	}
	
	public void setTimeZone(String timeZone)
	{
		this.timeZone = timeZone;
	}
	
	public List<String> getLicencas()
	{
		return licencas;
	}
	
	public void setLicencas(List<String> licencas)
	{
		this.licencas = licencas;
	}
}
