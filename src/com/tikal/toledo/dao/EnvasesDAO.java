package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.ClienteEnvases;
import com.tikal.toledo.model.Envase;

public interface EnvasesDAO {
	public ClienteEnvases getEnvases(Long id);
//	public List<Envase> getEnvases(Long id);
	public void guardar(ClienteEnvases e);
}
