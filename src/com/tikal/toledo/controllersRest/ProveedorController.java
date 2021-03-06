package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.dao.ProveedorDAO;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.Proveedor;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Util;

@Controller
@RequestMapping(value={"/proveedores"})
public class ProveedorController {
	
	@Autowired
	ProveedorDAO proveedordao;
	
	@Autowired
	UsuarioDAO usuariodao;
	
	@Autowired
	PerfilDAO perfildao;
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 8,9)){
			Proveedor c= (Proveedor) JsonConvertidor.fromJson(json, Proveedor.class);
			proveedordao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(c));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {"/addFinkok" }, method = RequestMethod.GET, produces = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs) throws IOException{
	//	if(Util.verificarPermiso(re, usuariodao, perfildao, 8,9)){
			Proveedor c= new Proveedor();
			c.setDireccion("Domicilio Conocido");
			c.setNombre("finkok");
			c.setTelefono("0180078878");
			proveedordao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(c));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	@RequestMapping(value = {
	"/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void find(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 8,9)){
			rs.getWriter().println(JsonConvertidor.toJson(proveedordao.cargar(Long.parseLong(id))));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/search/{search}" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs, @PathVariable String search) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 8,9,1)){
			rs.getWriter().println(JsonConvertidor.toJson(proveedordao.buscar(search)));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/findAll" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 8,9)){
		List<Proveedor> lista= proveedordao.todos();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}
}
