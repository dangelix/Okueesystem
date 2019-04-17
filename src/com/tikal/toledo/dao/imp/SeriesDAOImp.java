package com.tikal.toledo.dao.imp;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.tikal.toledo.dao.SeriesDAO;
import com.tikal.toledo.model.SerieFactura;
import com.tikal.toledo.model.SeriePedido;
import com.tikal.toledo.model.SerieVenta;

public class SeriesDAOImp implements SeriesDAO{

	@Override
	public int getSerieVenta() {
		SerieVenta sv= ofy().load().type(SerieVenta.class).list().get(0);
		return sv.folio;
	}

	@Override
	public int getSerieFactura() {
		SerieFactura sv= ofy().load().type(SerieFactura.class).list().get(0);
		return sv.folio;
	}
	
	@Override
	public int getSeriePedido() {
		SeriePedido sp= ofy().load().type(SeriePedido.class).list().get(0);
		return sp.folio;
	}

	@Override
	public void incSerieVenta() {
		SerieVenta sv= ofy().load().type(SerieVenta.class).list().get(0);
		
			sv.folio= sv.folio+1;
			ofy().save().entity(sv).now();
	
		
	}

	@Override
	public void incSerieFactura() {
		SerieFactura sv= ofy().load().type(SerieFactura.class).list().get(0);
		sv.folio= sv.folio+1;
		ofy().save().entity(sv).now();		
	}
	
	@Override
	public void incSeriePedido() {
		SeriePedido sp= ofy().load().type(SeriePedido.class).list().get(0);
		sp.folio= sp.folio+1;
		ofy().save().entity(sp).now();		
	}

	@Override
	public void crear() {
		SerieFactura sf= new SerieFactura();
		sf.folio=1;
		SeriePedido sp= new SeriePedido();
		sp.folio=1;
		SerieVenta sv= new SerieVenta();
		sv.folio=1;
		ofy().save().entity(sv).now();
		ofy().save().entity(sf).now();
		ofy().save().entity(sp).now();
	}

}
