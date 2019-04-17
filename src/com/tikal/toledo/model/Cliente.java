package com.tikal.toledo.model;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.tikal.toledo.sat.cfd.TUbicacion;

@Entity
public class Cliente {
	@Id
	private Long id;
	
	@Index
	private String nombre;
	
	private String rfc;
	
	private TUbicacion domicilio;
	
	private String telefono;
	
	private int credito;
	
	private int descuento;
	
	private String email;
	
	private List<Long> conceptos;
	
	private int diasCredito;
	
	private String estatus;
	
	private double saldo;
	
	private Date fechaPedido;
	
	private Date fechaPago;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getCredito() {
		return credito;
	}

	public void setCredito(int credito) {
		this.credito = credito;
	}

	public TUbicacion getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(TUbicacion domicilio) {
		this.domicilio = domicilio;
	}

	public int getDescuento() {
		return descuento;
	}

	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Long> getConceptos() {
		return conceptos;
	}

	public void setConceptos(List<Long> conceptos) {
		this.conceptos = conceptos;
	}

	public int getDiasCredito() {
		return diasCredito;
	}

	public void setDiasCredito(int diasCredito) {
		this.diasCredito = diasCredito;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public Date getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	

}
