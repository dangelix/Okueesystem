package com.tikal.toledo.controllersRest.VO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.tikal.toledo.model.Detalle;
import com.tikal.toledo.model.Lote;
import com.tikal.toledo.model.Venta;

public class DetalleVentasVO {


	private String fecha;
	private int folio;
	private String cliente;
	private String concepto;
	private double cantidad;
	private String unidad;	
	private double precio;
	private double importe;
	private String formaPago;
	private String facturado;
	
	
	
	public String getFecha() {
		return fecha;
	}



	public void setFecha(String fecha) {
		this.fecha = fecha;
	}



	public int getFolio() {
		return folio;
	}



	public void setFolio(int folio) {
		this.folio = folio;
	}



	public String getConcepto() {
		return concepto;
	}



	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}



	public double getCantidad() {
		return cantidad;
	}



	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}



	public String getUnidad() {
		return unidad;
	}



	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}



	public double getPrecio() {
		return precio;
	}



	public void setPrecio(double precio) {
		this.precio = precio;
	}



	public double getImporte() {
		return importe;
	}



	public void setImporte(double importe) {
		this.importe = importe;
	}



	public String getCliente() {
		return cliente;
	}



	public void setCliente(String cliente) {
		this.cliente = cliente;
	}



	public String getFormaPago() {
		return formaPago;
	}



	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}



	public String getFacturado() {
		return facturado;
	}



	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}



	public DetalleVentasVO(){}
	
	
	public void llenarRenglon(HSSFRow r){
		for(int i=0;i<10;i++){
			r.createCell(i);
		}
		
		r.getCell(0).setCellValue(this.getFecha());
		r.getCell(1).setCellValue(this.getCliente());
		r.getCell(2).setCellValue(this.getFolio());
		r.getCell(3).setCellValue(this.getConcepto());
		r.getCell(4).setCellValue(this.getUnidad());
		r.getCell(5).setCellValue(this.getCantidad());		
		r.getCell(6).setCellValue(this.getPrecio());
		r.getCell(7).setCellValue(this.getImporte());
		r.getCell(8).setCellValue(this.getFormaPago());
	//	r.getCell(9).setCellValue(this.getFacturado());
		if(this.facturado!=null){
			r.getCell(9).setCellValue(this.getFacturado());
		}else{
				r.getCell(9).setCellValue("No facturado");
		}
		
	
	}
	
}
	

