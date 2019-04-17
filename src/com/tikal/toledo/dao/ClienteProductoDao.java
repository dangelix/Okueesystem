package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.ClienteProducto;



public interface ClienteProductoDao {
	
public void guardar(ClienteProducto c);

public void eliminar(ClienteProducto c);
	
	public ClienteProducto cargar(Long id);

	//public List<ClienteProducto> buscar(String search);
	
	public List<ClienteProducto> todos(int page);
	
	public List<ClienteProducto> todos();
	
	public int pages();
	
	public List <ClienteProducto> getByCliente(Long idCliente);
	
	public ClienteProducto getByCyP(Long Cliente, Long Producto);

}
