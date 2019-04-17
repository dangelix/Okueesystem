package com.tikal.toledo.dao.imp;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tikal.toledo.dao.AlmacenDAO;
import com.tikal.toledo.model.Almacen;


public class AlmacenDAOImpl implements AlmacenDAO{
	@Override
	public void guardar(Almacen a) {
		ofy().save().entity(a).now();
	}

	@Override
	public Almacen cargar(Long id) {
		return ofy().load().type(Almacen.class).id(id).now();
	}
	
	@Override
	public void eliminar(Almacen a) {
		ofy().delete().entity(a);
	}

	@Override
	public List<Almacen> buscar(String search) {
		search= search.toLowerCase();
		List<Almacen> lista= ofy().load().type(Almacen.class).list();
		List<Almacen> result= new ArrayList<Almacen>();
		for(Almacen a:lista){
			if(a.getNombre().toLowerCase().contains(search)){
				result.add(a);
			}
		}
		return result;
	}

	@Override
	public List<Almacen> todos(int page) {
		return ofy().load().type(Almacen.class).offset((page-1)*25).limit(25).list();
	}

	@Override
	public int pages() {
		int total= ofy().load().type(Almacen.class).count();
		return ((total-1)/25)+1;
	}

	@Override
	public List<Almacen> todos() {
		// TODO Auto-generated method stub
		return ofy().load().type(Almacen.class).list();
	}


}
