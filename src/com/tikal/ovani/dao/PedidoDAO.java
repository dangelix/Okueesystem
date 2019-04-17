package com.tikal.ovani.dao;

import java.util.List;

import com.tikal.ovani.model.Pedido;


public interface PedidoDAO {
	
	public void guardar(Pedido p);
	public void actualizar(Pedido p);
	
	public Pedido cargar(Long id);
	
	public List<Pedido> todos(int page);
	
	public List<Pedido> todos();
	
	public int pages();
	
	public List<Pedido> getbyCliente(Long idCliente);

}
