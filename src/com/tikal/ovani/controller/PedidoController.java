package com.tikal.ovani.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.tikal.cacao.model.Imagen;
import com.tikal.ovani.dao.PedidoDAO;
import com.tikal.ovani.model.DetPedido;
import com.tikal.ovani.model.Pedido;
import com.tikal.toledo.dao.ClienteDAO;
import com.tikal.toledo.dao.EnvasesDAO;
import com.tikal.toledo.dao.LoteDAO;
import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.SeriesDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.ClienteEnvases;
import com.tikal.toledo.model.DatosEmisor;
import com.tikal.toledo.model.Detalle;
import com.tikal.toledo.model.Lote;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.ProductoCliente;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.model.Venta;
import com.tikal.toledo.sat.cfd.Comprobante;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.PDFFactura;
import com.tikal.toledo.util.Util;

@Controller
@RequestMapping(value = { "/pedido" })
public class PedidoController {
	@Autowired PedidoDAO pedidoDao;
	@Autowired ProductoDAO proDao;
	@Autowired TornilloDAO torDao;
	@Autowired ClienteDAO clienteDao;
	@Autowired LoteDAO loteDao;
	@Autowired UsuarioDAO usuariodao;
	@Autowired PerfilDAO perfildao;
	@Autowired
	SeriesDAO seriesdao;
	
	
	
	@RequestMapping(value = {
	"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		//if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
			System.out.println("pedido"+json);
			Pedido p= (Pedido) JsonConvertidor.fromJson(json, Pedido.class);
			pedidoDao.guardar(p);
			seriesdao.incSeriePedido();
			/// validar cliente
			
			Cliente c = clienteDao.cargar(p.getIdCliente());
			c.setFechaPedido(p.getFecha());
			c.setEstatus("NO VALIDADO");
			validaCliente(p.getIdCliente());
			rs.getWriter().println(JsonConvertidor.toJson(p));
						
			
			
//		}else{
//			rs.sendError(403);
//		}
	}
	
	@RequestMapping(value = {"/update" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void upd(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		//if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
			System.out.println("pedido"+json);
			Pedido p= (Pedido) JsonConvertidor.fromJson(json, Pedido.class);
			pedidoDao.actualizar(p);
			
			
	}
	
	
	@RequestMapping(value = {"/add_" }, method = RequestMethod.GET, produces = "application/json")
	public void add_(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		//if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
			Date fecha= new Date();
			Calendar calx=Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
			calx.add(Calendar.DAY_OF_MONTH, -10);
			Pedido p= new Pedido();
			
			p.setFolio(seriesdao.getSeriePedido());
			p.setId(Long.parseLong("123456789101112"));
			p.setIdCliente(Long.parseLong("5466840532779008"));
			List<DetPedido> items= new ArrayList<DetPedido>();
			
			DetPedido d= new DetPedido();
			d.setCantidad(Double.valueOf(5));
			
		//	d.setDescripcion("martillo__");
			d.setIdItem(Long.parseLong("5910974510923776"));
			items.add(d);
			p.setItems(items);
//			d.setTipo( );
//			d.s
//			pro.add(Long.parseLong("5910974310923776"));
//			pro.add(Long.parseLong("5348024557502464"));
//			p.setProductos(pro); 
			
			
			pedidoDao.guardar(p);
			seriesdao.incSeriePedido();
			Cliente c = clienteDao.cargar(p.getIdCliente());
			c.setFechaPago(fecha);
			c.setFechaPedido(fecha);
			c.setEstatus("NO VALIDADO");
			c.setSaldo(500);			c.setFechaPedido(fecha);
			clienteDao.guardar(c);
			
			validaCliente(p.getIdCliente());
			
			rs.getWriter().println(JsonConvertidor.toJson(p));
			
			
//		}else{
//			rs.sendError(403);
//		}
	}
//	@RequestMapping(value = {
//	"/addProducto" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
//	public void addProducto(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
//		if(Util.verificarPermiso(re, usuariodao, perfildao, 10, 11)){
//			ProductoCliente p= (ProductoCliente) JsonConvertidor.fromJson(json, ProductoCliente.class);
//			prodclientedao.agregar(p);
//			Cliente c= clientesdao.cargar(p.getId());
//			c.getConceptos().add(p.getId());
//			clientesdao.guardar(c);
//			rs.getWriter().println(JsonConvertidor.toJson(p));
//		}else{
//			rs.sendError(403);
//		}
//	}
//	
	@RequestMapping(value = {"/find/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public void find(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
		
		//if(Util.verificarPermiso(re, usuariodao, perfildao, 11)){
			
			Pedido p= pedidoDao.cargar(Long.parseLong(id));
			rs.getWriter().println(JsonConvertidor.toJson(p));
//		}else{
//			rs.sendError(403);
//		}
	}
	
//	@RequestMapping(value = {//	"/search/{search}" }, method = RequestMethod.GET, produces = "application/json")
//	public void search(HttpServletRequest re, HttpServletResponse rs, @PathVariable String search) throws IOException{
//		if(Util.verificarsesion(re)){
//		List<Cliente> lista= clientesdao.buscar(search);
//		rs.getWriter().println(JsonConvertidor.toJson(lista));
//		}else{
//			rs.sendError(403);
//		}
//	}
//	
//
	
	@RequestMapping(value = {"/numPages" }, method = RequestMethod.GET, produces = "application/json")
	public void pages(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		rs.getWriter().print(pedidoDao.pages());
	}
	
	@RequestMapping(value = {"/findAll/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs, @PathVariable int page) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		if(Util.verificarsesion(re)){
			List<Pedido> lista= pedidoDao.todos(page);
			rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {"/findAll" }, method = RequestMethod.GET, produces = "application/json")
	public void todos(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		if(Util.verificarsesion(re)){
			List<Pedido> lista= pedidoDao.todos();
			rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {"/getByCliente/{idCliente}" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs, @PathVariable Long idCliente) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
		if(Util.verificarsesion(re)){
			List<Pedido> lista= pedidoDao.getbyCliente(idCliente);
			rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}

	
	@RequestMapping(value = {"/Pdf/{id}" }, method = RequestMethod.GET)
	public void pdfaPedido(HttpServletRequest re, HttpServletResponse res, @PathVariable Long id) throws IOException{
		
//		if(Util.verificarPermiso(re, usuariodao, perfildao, 1,3)){
//			AsignadorDeCharset.asignar(re, res);
//			res.setContentType("Application/PDF");
//			Pedido p= pedidoDao.cargar(id);
//			PDFPedido = new PDFPedido();
//			PdfWriter writer = PdfWriter.getInstance(pdfPedido.getDocument(), res.getOutputStream());
//			PDFPedido.getDocument().open();
//			PDFPedido.getPieDePagina().setUuid("Nota");
//		//	DatosEmisor de= emisordao.getById(venta.getIdEmisor());
//			System.out.println("emisordao:"+de);
//			imagendao.addImagen("FQO150408K53","images/okue.jpg");
//			Imagen imagen = imagendao.get("FQO150408K53");
//			factura.crearMarcaDeAgua("CANCELADO", writer);
//
//				pdfPedido.construirPdf(p);
////			}
//			pdfPedido.getDocument().close();
//			res.getOutputStream().flush();
//			res.getOutputStream().close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//		}else{
//			res.sendError(403);
//		}
	}

	
	
	public void validaCliente(Long idCliente){
		Date fecha= new Date();
		Cliente c= clienteDao.cargar(idCliente);

		System.out.println("fechPed:"+c.getFechaPedido());
		System.out.println("saldo:"+c.getSaldo());
		//c.setFechaPago(fecha);
		String aux= "NO VALIDADO";
		//Date fecha= new Date();
	
		
		int diaspedido=(int) ((fecha.getTime()-c.getFechaPedido().getTime())/86400000);
		int diaspago=(int) ((fecha.getTime()-c.getFechaPago().getTime())/86400000);
		if ((c.getSaldo()<1000) && (c.getCredito()>50000) && (diaspago<30) && (diaspedido<30)){
			aux="VALIDADO";
		}

		c.setEstatus(aux);
		clienteDao.guardar(c);
		//return aux;
	}
	
	
	public void ajustarPedidoLong (Long idPedido){
		Pedido ped= pedidoDao.cargar(idPedido);
		List<DetPedido> items=ped.getItems();
		for(DetPedido i:items){
			Producto pr= proDao.cargar(i.getIdItem());
			if (pr==null){
				Tornillo t=torDao.cargar(i.getIdItem());
				List<Lote> lotes= loteDao.porProducto(i.getIdItem());
				double cantidad= i.getCantidad();
				for (Lote l:lotes){
					
					if(l.getCantidad()<cantidad){
						  
					}
				}
			}
			
			
		}
	}
}
