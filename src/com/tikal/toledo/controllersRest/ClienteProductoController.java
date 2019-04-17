package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tikal.toledo.dao.ClienteProductoDao;
import com.tikal.toledo.dao.ProductoClienteDAO;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.ClienteProducto;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.Util;

@Controller
@RequestMapping(value = { "/clienteProducto" })
public class ClienteProductoController {
	
	@Autowired UsuarioDAO usuariodao;
	
	@Autowired PerfilDAO perfildao;
	
	@Autowired ClienteProductoDao clienteProductoDao;

	
	@RequestMapping(value = {"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		//if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
		System.out.println("json:"+json);
			ClienteProducto c= (ClienteProducto) JsonConvertidor.fromJson(json, ClienteProducto.class);
			clienteProductoDao.guardar(c);
			rs.getWriter().println(JsonConvertidor.toJson(c));
//		}else{
//			rs.sendError(403);
//		}
	}
	
	@RequestMapping(value = {"/eliminar" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void delete(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException, SQLException{
	//	if(Util.verificarPermiso(re, usuariodao, perfildao, 0)){
		AsignadorDeCharset.asignar(re, rs);	
		ClienteProducto p= (ClienteProducto) JsonConvertidor.fromJson(json, ClienteProducto.class);
		clienteProductoDao.eliminar(p);
//		}else{
//			rs.sendError(403);
//		}
	}
	
	@RequestMapping(value = {"/getByCliente/{idCliente}" }, method = RequestMethod.GET, produces = "application/json")
	public void delete(HttpServletRequest re, HttpServletResponse rs, @PathVariable Long idCliente) throws IOException, SQLException{
	//	if(Util.verificarPermiso(re, usuariodao, perfildao, 0)){
		AsignadorDeCharset.asignar(re, rs);	
		List<ClienteProducto> lista= clienteProductoDao.getByCliente(idCliente);
		rs.getWriter().println(JsonConvertidor.toJson(lista));
//		}else{
//			rs.sendError(403);
//		}
	}
}
