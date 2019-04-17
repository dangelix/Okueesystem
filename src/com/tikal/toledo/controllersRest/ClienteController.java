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

import com.tikal.toledo.dao.ClienteDAO;
import com.tikal.toledo.dao.EnvasesDAO;
import com.tikal.toledo.dao.ProductoClienteDAO;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.ClienteEnvases;
import com.tikal.toledo.model.Envase;
import com.tikal.toledo.model.ProductoCliente;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Util;

@Controller
@RequestMapping(value = { "/clientes" })
public class ClienteController {

	@Autowired
	ClienteDAO clientesdao;
	
	@Autowired UsuarioDAO usuariodao;
	
	@Autowired PerfilDAO perfildao;
	
	@Autowired ProductoClienteDAO prodclientedao;
	
	@Autowired EnvasesDAO envasesdao;
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
			System.out.println("jfjfjf"+json);
			Cliente c= (Cliente) JsonConvertidor.fromJson(json, Cliente.class);
			clientesdao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(c));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/addProducto" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void addProducto(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
			ProductoCliente p= (ProductoCliente) JsonConvertidor.fromJson(json, ProductoCliente.class);
			prodclientedao.agregar(p);
			Cliente c= clientesdao.cargar(p.getId());
			c.getConceptos().add(p.getId());
			clientesdao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(p));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void find(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
		
		//if(Util.verificarPermiso(re, usuariodao, perfildao, 11)){
			
			Cliente c= clientesdao.cargar(Long.parseLong(id));
			rs.getWriter().println(JsonConvertidor.toJson(c));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	@RequestMapping(value = {
	"/search/{search}" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs, @PathVariable String search) throws IOException{
		if(Util.verificarsesion(re)){
		List<Cliente> lista= clientesdao.buscar(search);
		rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}
	

	@RequestMapping(value = {
	"/findAll/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs, @PathVariable int page) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		if(Util.verificarsesion(re)){
		List<Cliente> lista= clientesdao.todos(page);
		rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/findFull" }, method = RequestMethod.GET, produces = "application/json")
	public void todos(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		if(Util.verificarsesion(re)){
		List<Cliente> lista= clientesdao.todos();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/pages" }, method = RequestMethod.GET, produces = "application/json")
	public void pages(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		rs.getWriter().print(clientesdao.pages());
	}
	@RequestMapping(value = {
	"/envases/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void envases(HttpServletRequest re, HttpServletResponse rs, @PathVariable Long id) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		rs.getWriter().print(JsonConvertidor.toJson(envasesdao.getEnvases(id).getEnvases()));
	}
	
	@RequestMapping(value = {"/envases/devolucion/{id}/{tipo}/{cantidad}" }, method = RequestMethod.POST)
	public void devEnvases(HttpServletRequest re, HttpServletResponse rs, @PathVariable Long id,@PathVariable String tipo,@PathVariable int cantidad ) throws IOException{
		AsignadorDeCharset.asignar(re, rs);

		ClienteEnvases ce= envasesdao.getEnvases(id);
		ce.devolverEnvases(cantidad, tipo);
		envasesdao.guardar(ce);

	}
	
}
