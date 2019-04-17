package com.tikal.toledo.dao.imp;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.tikal.toledo.dao.TipoEnvaseDao;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.TipoEnvase;

public class TipoEnvaseDaoImpl implements TipoEnvaseDao {

	@Override
	public void add(TipoEnvase t) {
		// TODO Auto-generated method stub
		ofy().save().entity(t).now();
		
	}

	@Override
	public List<TipoEnvase> getAll() {
		return ofy().load().type(TipoEnvase.class).list();
	}

	@Override
	public void delete(TipoEnvase t) {
		ofy().delete().entity(t).now();
	}

	@Override
	public TipoEnvase getById(Long id) {
		//List<TipoEnvase> lista=ofy().load().type(TipoEnvase.class).filter("id",id).list();
		return ofy().load().type(TipoEnvase.class).id(id).now();
//		if(lista.size()>0){
//			return lista.get(0);
//		}
//		return null;
	}
	
	

}
