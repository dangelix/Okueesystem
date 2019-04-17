package com.tikal.toledo.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Lote {

	@Id
	private Long id;
	
	@Index
	private Long idProducto;
	
	private float cantidad;
	
	@Index
	private Date fecha;
	
	private float costo;
	
	private Long proveedor;
	
	private int tipo;
	
	private String ubicacion;
	
	private String rack;
	
	private String cuadricula;
	
	private String nivel;
	
	private String posicion;
	
	@Index private Long idAlmacen;
	
	private String ubicacionFisica;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public float getCantidad() {
		return cantidad;
	}

	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public float getCosto() {
		return costo;
	}

	public void setCosto(float costo) {
		this.costo = costo;
	}

	public Long getProveedor() {
		return proveedor;
	}

	public void setProveedor(Long proveedor) {
		this.proveedor = proveedor;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getCuadricula() {
		return cuadricula;
	}

	public void setCuadricula(String cuadricula) {
		this.cuadricula = cuadricula;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public Long getIdAlmacen() {
		return idAlmacen;
	}

	public void setIdAlmacen(Long idAlmacen) {
		this.idAlmacen = idAlmacen;
	}

	public String getUbicacionFisica() {
		return ubicacionFisica;
	}

	public void setUbicacionFisica(String ubicacionFisica) {
		this.ubicacionFisica = ubicacionFisica;
	}
	
	
	
}
