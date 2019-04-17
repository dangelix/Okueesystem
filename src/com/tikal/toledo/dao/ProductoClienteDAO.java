package com.tikal.toledo.dao;

import java.util.List;

import com.tikal.toledo.model.ProductoCliente;

public interface ProductoClienteDAO {

	public void agregar(ProductoCliente p);
	public void eliminar(ProductoCliente p);
	public List<ProductoCliente> consultar(Long idCliente);
}
