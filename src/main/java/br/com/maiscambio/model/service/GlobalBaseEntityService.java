package br.com.maiscambio.model.service;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import br.com.maiscambio.model.entity.BaseEntity;

@Service
public interface GlobalBaseEntityService<ENTITY extends BaseEntity, ID extends Serializable> extends BaseService
{
	public ENTITY findOne(ID id);
}
