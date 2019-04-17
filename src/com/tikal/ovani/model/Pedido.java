package com.tikal.ovani.model;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class Pedido {

	@Id private Long idPedido;	
	@Index private int folio;
	@Index private Date fecha;
	@Index private  Long idCliente;
	private List<DetPedido> items;
	
	public Long getId() {
		return idPedido;
	}
	public void setId(Long id) {
		this.idPedido = id;
	}
	
	
	public int getFolio() {
		return folio;
	}
	public void setFolio(int folio) {
		this.folio = folio;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Long getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	public List<DetPedido> getItems() {
		return items;
	}
	public void setItems(List<DetPedido> items) {
		this.items = items;
	}
	
	
	
	
	
}
