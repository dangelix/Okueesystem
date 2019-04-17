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

import com.tikal.toledo.dao.AlmacenDAO;

import com.tikal.toledo.model.Almacen;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Util;

@Controller
@RequestMapping(value = { "/almacen" })
public class AlmacenController {
	@Autowired
	AlmacenDAO almacendao;
	
	@Autowired UsuarioDAO usuariodao;
	
	@Autowired PerfilDAO perfildao;
	
	@RequestMapping(value = {"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		//if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
		Almacen a= (Almacen) JsonConvertidor.fromJson(json, Almacen.class);
			almacendao.guardar(a);
			rs.getWriter().println(JsonConvertidor.toJson(a));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	@RequestMapping(value = {"/addPrueba" }, method = RequestMethod.GET, produces = "application/json")
	public void addP(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		//if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
	//	Almacen a= (Almacen) JsonConvertidor.fromJson(json, Almacen.class);
		Almacen a = new Almacen();
		a.setClave("alm1");
		a.setNombre("Almacen Uno");
			almacendao.guardar(a);
			rs.getWriter().println(JsonConvertidor.toJson(a));
//		}else{
//			rs.sendError(403);
//		}
	}
	@RequestMapping(value = {"/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void find(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
		
	//	if(Util.verificarPermiso(re, usuariodao, perfildao, 11)){
		Almacen a= almacendao.cargar(Long.parseLong(id));
		rs.getWriter().println(JsonConvertidor.toJson(a));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	@RequestMapping(value = {"/findAll" }, method = RequestMethod.GET, produces = "application/json")
	public void todos(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
	//	if(Util.verificarsesion(re)){
		List<Almacen> lista= almacendao.todos();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	
	@RequestMapping(value = {"/delete/{id}" }, method = RequestMethod.POST, produces = "application/json")
	public void delete(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
		
	//	if(Util.verificarPermiso(re, usuariodao, perfildao, 11)){
		Almacen a= almacendao.cargar(Long.parseLong(id));
		almacendao.eliminar(a);
		rs.getWriter().println(JsonConvertidor.toJson("ok"));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	
	
	
}
