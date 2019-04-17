package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.Almacen;
import com.tikal.toledo.model.Tornillo;


public interface AlmacenDAO {
	public void guardar(Almacen a);
	
	public Almacen cargar(Long id);
	
	public void eliminar(Almacen a);

	public List<Almacen> buscar(String search);
	
	public List<Almacen> todos(int page);
	
	public List<Almacen> todos();
	
	public int pages();

}
