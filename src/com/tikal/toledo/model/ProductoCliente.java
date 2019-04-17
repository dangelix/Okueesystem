package com.tikal.toledo.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ProductoCliente {

	@Id
	private Long id;
	
	private String clave;
	
	private String descripcion;
	
	private double concentracion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getConcentracion() {
		return concentracion;
	}

	public void setConcentracion(double concentracion) {
		this.concentracion = concentracion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
	
}
