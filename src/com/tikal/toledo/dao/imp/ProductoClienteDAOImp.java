package com.tikal.toledo.dao.imp;

import java.util.List;

import com.tikal.toledo.dao.ProductoClienteDAO;
import com.tikal.toledo.model.ProductoCliente;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class ProductoClienteDAOImp implements ProductoClienteDAO{

	@Override
	public void agregar(ProductoCliente p) {
		ofy().save().entity(p).now();
	}

	@Override
	public void eliminar(ProductoCliente p) {
		ofy().delete().entity(p).now();
	}

	@Override
	public List<ProductoCliente> consultar(Long idCliente) {
		return ofy().load().type(ProductoCliente.class).filter("idCliente", idCliente).list();
	}

}
