package br.com.maiscambio.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import me.gerenciar.util.BaseEntity;

@Entity
@Table(name = "PAIS", schema = "public")
public class Pais extends BaseEntity
{
	private static final long serialVersionUID = 4807573770278950328L;
	
	@Id
	@Column(name = "ID_PAIS")
	private String paisId;
	
	public String getPaisId()
	{
		return paisId;
	}
	
	public void setPaisId(String paisId)
	{
		this.paisId = paisId;
	}
}
