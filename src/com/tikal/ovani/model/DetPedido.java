package com.tikal.ovani.model;

import com.googlecode.objectify.annotation.Index;

public class DetPedido {

	
	//private String descripcion;
	@Index private Long idItem;
	private double cantidad;
//	public String getDescripcion() {
//		return descripcion;
//	}
//	public void setDescripcion(String descripcion) {
//		this.descripcion = descripcion;
//	}
	public Long getIdItem() {
		return idItem;
	}
	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	
	
}
