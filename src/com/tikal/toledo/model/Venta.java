package com.tikal.toledo.model;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Venta {

	@Id
	private Long id;
	
	private String estatus;
	
	@Index
	private Date fecha;
	
	private String cliente;
	
	private Long idCliente;
	
	private String user;
	
	private List<Detalle> detalles;

	private String uuid;
	
	private float monto;
	
	private String formaDePago;
	
	private int folio;
	
	private String factura;
	
	private String version;
	
	private String metodoDePago;
	
	private List<Envase> envase;
	
	private Long idEmisor;
	
	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<Detalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}
	
	public void addDetalle(Detalle detalle){
		this.detalles.add(detalle);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFormaDePago() {
		return formaDePago;
	}

	public void setFormaDePago(String formaDePago) {
		this.formaDePago = formaDePago;
	}

	public void llenarRenglon(HSSFRow r){
		for(int i=0;i<8;i++){
			r.createCell(i);
		}
		
		r.getCell(0).setCellValue(this.getCliente());
		r.getCell(1).setCellValue(this.getEstatus());
		r.getCell(2).setCellValue(this.getFormaDePago());
		r.getCell(5).setCellValue(this.getMonto());
		r.getCell(3).setCellValue(this.getFolio());
		if(this.factura!=null){
		r.getCell(4).setCellValue(this.getFactura());
		}else{
			r.getCell(4).setCellValue("No facturado");
		}
	}
	
	
	
	

	public int getFolio() {
		return folio;
	}

	public void setFolio(int folio) {
		this.folio = folio;
	}

	public String getFactura() {
		return factura;
	}

	public void setFactura(String factura) {
		this.factura = factura;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMetodoDePago() {
		return metodoDePago;
	}

	public void setMetodoDePago(String metodoDePago) {
		this.metodoDePago = metodoDePago;
	}

	public List<Envase> getEnvase() {
		return envase;
	}

	public void setEnvase(List<Envase> envase) {
		this.envase = envase;
	}

	public Long getIdEmisor() {
		return idEmisor;
	}

	public void setIdEmisor(Long idEmisor) {
		this.idEmisor = idEmisor;
	}




	
}
