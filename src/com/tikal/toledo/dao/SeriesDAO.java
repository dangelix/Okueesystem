package com.tikal.toledo.dao;

public interface SeriesDAO {
	int getSerieVenta();
	int getSerieFactura();
	int getSeriePedido();
	void incSerieVenta();
	void incSerieFactura();
	void incSeriePedido();
	void crear();
}
