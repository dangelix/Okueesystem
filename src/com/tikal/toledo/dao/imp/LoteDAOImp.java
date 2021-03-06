package com.tikal.toledo.dao.imp;

import java.util.ArrayList;
import java.util.List;

import com.tikal.toledo.dao.LoteDAO;
import com.tikal.toledo.model.Lote;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class LoteDAOImp implements LoteDAO {

	@Override
	public void guardar(Lote l) {
		ofy().save().entity(l).now();
	}

	@Override
	public List<Lote> porProducto(Long idProducto) {
		List<Lote> result= ofy().load().type(Lote.class).filter("idProducto",idProducto).order("fecha").list();
		if (result.size()==0){
			return null;
		}else{
			return result;
		}
		
	}

	@Override
	public void guardarLotes(List<Lote> lotes) {
		ofy().save().entities(lotes).now();
	}

	@Override
	public List<Lote> todos() {
		return ofy().load().type(Lote.class).list();
	}

}
