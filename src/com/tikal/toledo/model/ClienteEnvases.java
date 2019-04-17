package com.tikal.toledo.model;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ClienteEnvases {

	@Id
	private Long idCliente;
	
	private List<Envase> envases;

	public ClienteEnvases() {
		this.envases = new ArrayList<Envase>();
	}
	
	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public List<Envase> getEnvases() {
		return envases;
	}

	public void setEnvases(List<Envase> envases) {
		this.envases = envases;
	}
	
	public void agregaEnvases(Envase e) {
		boolean encontrado =false;
		for(Envase a: this.envases) {
			if(a.getTipoEnvase().compareTo(e.getTipoEnvase())==0) {
				encontrado= true;
				a.setCantidadEnvase(a.getCantidadEnvase() + e.getCantidadEnvase());
				break;
			}
		}
		if(!encontrado) {
			this.envases.add(e);
		}
	}
	
	public void devolverEnvases(int cantidad, String tipo) {
		boolean encontrado =false;
		for(Envase a: this.envases) {
			if(a.getTipoEnvase().compareTo(tipo)==0) {
				encontrado= true;
				a.setCantidadEnvase(a.getCantidadEnvase() - cantidad);
				break;
			}
		}

	}
}
