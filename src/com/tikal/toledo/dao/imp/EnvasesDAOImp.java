package com.tikal.toledo.dao.imp;

import com.tikal.toledo.dao.EnvasesDAO;
import com.tikal.toledo.model.ClienteEnvases;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class EnvasesDAOImp implements EnvasesDAO{

	@Override
	public ClienteEnvases getEnvases(Long id) {
		ClienteEnvases e= ofy().load().type(ClienteEnvases.class).id(id).now();
		if(e == null) {
			e = new ClienteEnvases();
			e.setIdCliente(id);
			ofy().save().entity(e).now();
		}
		return e;
	}

	@Override
	public void guardar(ClienteEnvases e) {
		ofy().save().entity(e).now();
	}

}
