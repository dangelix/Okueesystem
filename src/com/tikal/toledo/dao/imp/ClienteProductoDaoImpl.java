package com.tikal.toledo.dao.imp;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.tikal.toledo.dao.ClienteProductoDao;
import com.tikal.toledo.model.ClienteProducto;


public class ClienteProductoDaoImpl implements ClienteProductoDao {
	
	@Override
	public void guardar(ClienteProducto c) {
		ofy().save().entity(c).now();
	}
	
	@Override
	public void eliminar(ClienteProducto c) {
		ofy().delete().entity(c).now();
	}

	@Override
	public ClienteProducto cargar(Long id) {
		return ofy().load().type(ClienteProducto.class).id(id).now();
	}

//	@Override
//	public List<ClienteProducto> buscar(String search) {
//		search= search.toLowerCase();
//		List<ClienteProducto> lista= ofy().load().type(ClienteProducto.class).list();
//		List<ClienteProducto> result= new ArrayList<ClienteProducto>();
//		for(ClienteProducto c:lista){
//			if(c.getNombre().toLowerCase().contains(search)|| c.getRfc().toLowerCase().contains(search)){
//				result.add(c);
//			}
//		}
//		return result;
//	}

	@Override
	public List<ClienteProducto> todos(int page) {
		return ofy().load().type(ClienteProducto.class).offset((page-1)*25).limit(25).list();
	}

	@Override
	public int pages() {
		int total= ofy().load().type(ClienteProducto.class).count();
		return ((total-1)/25)+1;
	}

	@Override
	public List<ClienteProducto> todos() {
		// TODO Auto-generated method stub
		System.out.println("jfkjf");
		return ofy().load().type(ClienteProducto.class).list();
	}


	@Override
	public List <ClienteProducto> getByCliente(Long idCliente){
		
		return ofy().load().type(ClienteProducto.class).filter("idCliente", idCliente).list();
	}

	@Override
	public ClienteProducto getByCyP(Long Cliente, Long Producto) {
		return ofy().load().type(ClienteProducto.class).filter("idCliente", Cliente).filter("idProducto", Producto).list().get(0);
		
	}
	
	
}
