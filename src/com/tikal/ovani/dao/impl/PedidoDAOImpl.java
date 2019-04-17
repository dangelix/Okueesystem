package com.tikal.ovani.dao.impl;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.tikal.ovani.dao.PedidoDAO;
import com.tikal.ovani.model.Pedido;


public class PedidoDAOImpl implements PedidoDAO {
	@Override
	public void guardar(Pedido p) {
		ofy().save().entity(p).now();
	}

	@Override
	public Pedido cargar(Long id) {
		return ofy().load().type(Pedido.class).id(id).now();
	}


	@Override
	public List<Pedido> todos(int page) {
		return ofy().load().type(Pedido.class).offset((page-1)*25).limit(25).list();
	}

	@Override
	public int pages() {
		int total= ofy().load().type(Pedido.class).count();
		return ((total-1)/25)+1;
	}

	@Override
	public List<Pedido> todos() {
		// TODO Auto-generated method stub
		return ofy().load().type(Pedido.class).list();
	}

	@Override
	public List<Pedido> getbyCliente(Long idCliente) {
		// TODO Auto-generated method stub
		return ofy().load().type(Pedido.class).filter("idCliente", idCliente).list();
	}

	@Override
	public void actualizar(Pedido p) {
		ofy().save().entity(p).now();
	}


}
