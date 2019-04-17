
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

import com.tikal.toledo.dao.TipoEnvaseDao;

import com.tikal.toledo.model.TipoEnvase;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;

@Controller
@RequestMapping(value={"/envase"})
public class TipoEnvaseController {
	@Autowired
	UsuarioDAO usuariodao;
	
	@Autowired
	PerfilDAO perfildao;

	@Autowired
	TipoEnvaseDao tipoEnvaseDao;
	
	@RequestMapping(value = {"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		//if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
		TipoEnvase t= (TipoEnvase) JsonConvertidor.fromJson(json, TipoEnvase.class);
		tipoEnvaseDao.add(t);
			rs.getWriter().println(JsonConvertidor.toJson(t));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	
	@RequestMapping(value = {"/getById/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void find(HttpServletRequest re, HttpServletResponse rs, @PathVariable Long id) throws IOException{
		
	//	if(Util.verificarPermiso(re, usuariodao, perfildao, 11)){
		TipoEnvase t= tipoEnvaseDao.getById(id);
		rs.getWriter().println(JsonConvertidor.toJson(t));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	@RequestMapping(value = {"/getAll" }, method = RequestMethod.GET, produces = "application/json")
	public void todos(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
	//	if(Util.verificarsesion(re)){
		List<TipoEnvase> lista= tipoEnvaseDao.getAll();
		rs.getWriter().println(JsonConvertidor.toJson(lista));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	
	@RequestMapping(value = {"/delete/{id}" }, method = RequestMethod.POST, produces = "application/json")
	public void delete(HttpServletRequest re, HttpServletResponse rs, @PathVariable Long id) throws IOException{
		
	//	if(Util.verificarPermiso(re, usuariodao, perfildao, 11)){
		TipoEnvase t= tipoEnvaseDao.getById(id);
		tipoEnvaseDao.delete(t);
		rs.getWriter().println(JsonConvertidor.toJson("ok"));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	
	
}



