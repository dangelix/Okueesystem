package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.TipoEnvase;



public interface TipoEnvaseDao {

	void add(TipoEnvase t);
	
	List<TipoEnvase>getAll();
	
	void delete(TipoEnvase t);
	
	TipoEnvase getById(Long id);
}


