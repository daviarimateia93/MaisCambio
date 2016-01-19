package br.com.maiscambio.model.entity.primarykey;

import br.com.maiscambio.model.entity.BaseEntity;

public class EstadoPrimaryKey extends BaseEntity
{
	private static final long serialVersionUID = 2726548718007087820L;
	
	private String estadoId;
	private String paisId;
	
	public EstadoPrimaryKey()
	{
		
	}
	
	public EstadoPrimaryKey(String estadoId, String paisId)
	{
		this.estadoId = estadoId;
		this.paisId = paisId;
	}
	
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
}
