package com.tikal.toledo.controllersRest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.tikal.cacao.dao.ImagenDAO;
import com.tikal.cacao.factura.Estatus;
import com.tikal.cacao.model.Imagen;
import com.tikal.toledo.controllersRest.VO.DetalleVentasVO;
import com.tikal.toledo.controllersRest.VO.LoteVO;
import com.tikal.toledo.dao.AlertaDAO;
import com.tikal.toledo.dao.AlmacenDAO;
import com.tikal.toledo.dao.ClienteDAO;
import com.tikal.toledo.dao.ClienteProductoDao;
import com.tikal.toledo.dao.EmisorDAO;
import com.tikal.toledo.dao.EnvasesDAO;
import com.tikal.toledo.dao.FacturaDAO;
import com.tikal.toledo.dao.LoteDAO;
import com.tikal.toledo.dao.ProductoDAO;
import com.tikal.toledo.dao.SeriesDAO;
import com.tikal.toledo.dao.TornilloDAO;
import com.tikal.toledo.dao.VentaDAO;
import com.tikal.toledo.facturacion.ComprobanteVentaFactory;
import com.tikal.toledo.facturacion.ws.WSClient;
import com.tikal.toledo.model.AlertaInventario;
import com.tikal.toledo.model.Almacen;
import com.tikal.toledo.model.Cliente;
import com.tikal.toledo.model.ClienteEnvases;
import com.tikal.toledo.model.DatosEmisor;
import com.tikal.toledo.model.Detalle;
import com.tikal.toledo.model.Envase;
import com.tikal.toledo.model.Factura;
import com.tikal.toledo.model.Lote;
import com.tikal.toledo.model.Producto;
import com.tikal.toledo.model.Tornillo;
import com.tikal.toledo.model.Venta;
import com.tikal.toledo.sat.cfd.Comprobante;
import com.tikal.toledo.sat.timbrefiscaldigital.TimbreFiscalDigital;
import com.tikal.toledo.security.PerfilDAO;
import com.tikal.toledo.security.UsuarioDAO;
import com.tikal.toledo.util.AsignadorDeCharset;
import com.tikal.toledo.util.CorteDeCaja;
import com.tikal.toledo.util.DetalleVentas;
import com.tikal.toledo.util.EmailSender;
import com.tikal.toledo.util.JsonConvertidor;
import com.tikal.toledo.util.PDFFactura;
import com.tikal.toledo.util.ReporteLotes;
import com.tikal.toledo.util.Util;

import localhost.CancelaCFDIAckResponse;
import localhost.EncodeBase64;
import localhost.TimbraCFDIResponse;
import mx.gob.sat.cancelacfd.Acuse;

 
@Controller
@RequestMapping(value={"/ventas"})
public class VentaController {

	@Autowired
	EmisorDAO emisordao;
	
	@Autowired
	VentaDAO ventadao;
	
	@Autowired
	ProductoDAO productodao;
	
	@Autowired
	AlmacenDAO adao;
	
	@Autowired
	ClienteDAO clientedao;
	
	@Autowired
	TornilloDAO tornillodao;
	
	@Autowired
	LoteDAO lotedao;
	
	@Autowired
	ComprobanteVentaFactory cvFactory;
	
	@Autowired
	FacturaDAO facturadao;
	
	@Autowired
	WSClient client;
	
	@Autowired
	AlertaDAO alertadao;
	
	@Autowired
	SeriesDAO seriesdao;
	
	@Autowired
	UsuarioDAO usuariodao;
	
	@Autowired
	PerfilDAO perfildao;
	
	@Autowired
	ClienteProductoDao clienteProductoDao;
	
	@Autowired
	ImagenDAO imagendao;
	
	@Autowired 
	EnvasesDAO envasesdao;
	
	@PostConstruct
	public void init() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package with the WSDL java classes
		marshaller.setContextPath("localhost");

	
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
	}
	
	@RequestMapping(value = {"/add" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void add(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException, SQLException{
			AsignadorDeCharset.asignar(re, rs);
			System.out.println("si entra aqui"+json);
			Venta l= (Venta)JsonConvertidor.fromJson(json, Venta.class);
			Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
			cal.add(Calendar.HOUR_OF_DAY, -6);
			l.setFecha(cal.getTime());
			
			l.setCliente("Otro");
			if(l.getIdCliente()!=0){
				Cliente c= clientedao.cargar(l.getIdCliente());
				l.setCliente(c.getNombre());
				ClienteEnvases envases= envasesdao.getEnvases(c.getId());
				if (l.getEnvase().size()<=0){
					
				}else{
				
					
				
					for(Envase e:l.getEnvase()) {
						envases.agregaEnvases(e);
					}
			
				}
				envasesdao.guardar(envases);
			}
			
			float monto=0;
			for(Detalle d:l.getDetalles()){
//				double por= d.getPorcentaje();
//				System.out.println("jfkjf");
//				double total= d.getCantidad()*d.getPrecioUnitario()*por/100;
//				if(total != d.getTotal()){
//					d.setPrecioUnitario(d.getTotal()/d.getCantidad()*por);
//				}
				monto+= d.getTotal();
			}
			l.setMonto(monto);
			l.setFolio(seriesdao.getSerieVenta());		
			l.setVersion("3.3");
			l.setIdEmisor(emisordao.getActivo().getId());			
			ventadao.guardar(l);
			if(l.getEstatus().compareTo("GENERADO")!=0){
				actualizarInventario(l.getDetalles());
				seriesdao.incSerieVenta();
			}
			
			rs.getWriter().println(JsonConvertidor.toJson(l));
	}
	
	@RequestMapping(value = {
	"/find/{id}" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void find(HttpServletRequest re, HttpServletResponse rs, @PathVariable String id) throws IOException{
		AsignadorDeCharset.asignar(re, rs);
			Venta l= ventadao.cargar(Long.parseLong(id));
			rs.getWriter().println(JsonConvertidor.toJson(l));
	}
	
	@RequestMapping(value = {
	"/cancelarVenta" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void cancelar(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException, SQLException{
		
		AsignadorDeCharset.asignar(re, rs);
		Venta venta= (Venta) JsonConvertidor.fromJson(json, Venta.class);
		this.incrementarInventario(venta.getDetalles());
		venta.setEstatus("DEVOLUCION");
		ventadao.guardar(venta);
		
	}
	
	@RequestMapping(value = {
	"/update" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void update(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException{
		Venta venta= (Venta) JsonConvertidor.fromJson(json, Venta.class);
		AsignadorDeCharset.asignar(re, rs);
		ventadao.guardar(venta);
		rs.getWriter().println(JsonConvertidor.toJson(venta));
	}
	
	@RequestMapping(value = {
	"/facturar" }, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public void facturar(HttpServletRequest re, HttpServletResponse rs, @RequestBody String json) throws IOException, MessagingException, DocumentException{
		System.out.println("hkkdf");
		if(Util.verificarPermiso(re, usuariodao, perfildao, 3)){
		AsignadorDeCharset.asignar(re, rs);
			Venta venta= (Venta)JsonConvertidor.fromJson(json, Venta.class);
			DatosEmisor emisor= emisordao.getActivo();
			int folio = seriesdao.getSerieFactura();
			Cliente cliente= clientedao.cargar(venta.getIdCliente());
			Comprobante c=cvFactory.generarFactura(venta, cliente,emisor);
			//facturar
			c.setFolio(folio+"");
			c.setSerie("FS");
			TimbraCFDIResponse timbraCFDIResp = client.getTimbraCFDIResponse(Util.marshallComprobante(c));
			List<Object> listaResultado = timbraCFDIResp.getTimbraCFDIResult().getAnyType();
			int codigoError = (int) listaResultado.get(1);
			String[] respuesta= new String[2];
			if (codigoError == 0) {
				String cfdiXML = (String) listaResultado.get(3);
				Factura f= new Factura();
				f.setCfdiXML(cfdiXML);
				f.setCodigoQR((byte[])listaResultado.get(4));
				Comprobante cfdi= Util.unmarshallXML(cfdiXML);
				TimbreFiscalDigital timbreFD = (TimbreFiscalDigital) cfdi.getComplemento().getAny().get(0);
				Date fechaCertificacion = timbreFD.getFechaTimbrado().toGregorianCalendar().getTime();
				
				f.setFechaCertificacion(fechaCertificacion);
				f.setSelloDigital((String)listaResultado.get(5));
				f.setUuid(timbreFD.getUUID());
				f.setEstatus(Estatus.TIMBRADO);
//				venta.setXml(cfdiXML);
				venta.setEstatus("FACTURADO");
				venta.setUuid(f.getUuid());
				venta.setFactura("FS"+folio);
				seriesdao.incSerieFactura();
				ventadao.guardar(venta);
				facturadao.guardar(f);
				respuesta[0]="0";
				EmailSender mailero = new EmailSender();
				if(cliente.getEmail()!=null){
					mailero.enviaFactura(cliente.getEmail(), f, "", cfdi.getComplemento().getAny().get(0).toString());
				}
			}else{
				respuesta[0]="1";
				String mensaje=(String)listaResultado.get(2);
				respuesta[1]="Mensaje de respuesta: " +mensaje ;
			}
			
			rs.getWriter().println(JsonConvertidor.toJson(respuesta));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = "/cancelarAck", method = RequestMethod.POST)
	public void cancelarAck(HttpServletRequest req, HttpServletResponse res, @RequestBody String json) throws IOException {
		if(Util.verificarPermiso(req, usuariodao, perfildao, 3)){
		Venta v= ventadao.cargar(Long.parseLong(json));
		Factura factura = facturadao.consultar(v.getUuid());
		Comprobante com= Util.unmarshallXML(factura.getCfdiXML());
		String rfc= com.getEmisor().getRfc();
		String[] resp= new String[2];
		CancelaCFDIAckResponse cancelaCFDIAckResponse = client.getCancelaCFDIAckResponse(v.getUuid(),rfc);
		
		try {
			PrintWriter writer = res.getWriter();
			List<Object> respuesta = cancelaCFDIAckResponse.getCancelaCFDIAckResult().getAnyType();
			int codigoError = (int) respuesta.get(1);

			if (codigoError == 0) {
				String acuseXML = (String) respuesta.get(3);
				StringBuilder stringBuilder = new StringBuilder(acuseXML);
				stringBuilder.insert(99, " xmlns=\"http://cancelacfd.sat.gob.mx\" ");
				String acuseXML2 = stringBuilder.toString();
				factura.setAcuseCancelacionXML(acuseXML2);
				Acuse acuse = Util.unmarshallAcuseXML(acuseXML2);
				if (acuse != null) {
					EncodeBase64 encodeBase64 = new EncodeBase64();
					String sello = new String(acuse.getSignature().getSignatureValue(), "ISO-8859-1");
					String selloBase64 = encodeBase64.encodeStringSelloCancelacion(sello);
					factura.setFechaCancelacion(acuse.getFecha().toGregorianCalendar().getTime());
					factura.setSelloCancelacion(selloBase64);
				} else {
					stringBuilder = new StringBuilder(acuseXML);
					stringBuilder.insert(100, " xmlns=\"http://cancelacfd.sat.gob.mx\" ");
					acuseXML2 = stringBuilder.toString();
					factura.setAcuseCancelacionXML(acuseXML2);
					acuse = Util.unmarshallAcuseXML(acuseXML2);
					if (acuse != null) {
						EncodeBase64 encodeBase64 = new EncodeBase64();
						String sello = new String(acuse.getSignature().getSignatureValue(), "ISO-8859-1");
						String selloBase64 = encodeBase64.encodeStringSelloCancelacion(sello);
						factura.setFechaCancelacion(acuse.getFecha().toGregorianCalendar().getTime());
						factura.setSelloCancelacion(selloBase64);
					}
				}
				factura.setEstatus(Estatus.CANCELADO);
				v.setEstatus(Estatus.CANCELADO.toString());
				ventadao.guardar(v);
				facturadao.guardar(factura);
				String evento = "Se cancel� la factura guardada con el id:"+factura.getUuid();
//				RegistroBitacora registroBitacora = Util.crearRegistroBitacora(req.getSession(), "Operacional", evento);
//				bitacoradao.addReg(registroBitacora);
				resp[0]="0"; 
			}else{
				resp[0]="1";
				String mensaje=(String)respuesta.get(2);
				resp[1]="Mensaje de respuesta: " +mensaje ;
			}
			writer.println(JsonConvertidor.toJson(resp));
			// escribirRespuesta(respuesta, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		}else{
			res.sendError(403);
		}
	}
	
	@RequestMapping(value = "/buscar", method = RequestMethod.GET, produces = "application/json")
	public void buscar(HttpServletRequest req, HttpServletResponse res) throws ParseException, IOException {
		if(Util.verificarPermiso(req, usuariodao, perfildao, 1)){
			AsignadorDeCharset.asignar(req, res);
			String fi = (String) req.getParameter("fi");
			String ff = (String) req.getParameter("ff");
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			Date datei = formatter.parse(fi);
			Date datef = formatter.parse(ff);
			Calendar c = Calendar.getInstance();
			c.setTime(datef);
			c.add(Calendar.DATE, 1);
			datef = c.getTime();

			// agregar serie en el @RequestMapping
			List<Venta> listaR = ventadao.buscar(datei, datef);

			res.getWriter().println(JsonConvertidor.toJson(listaR));
		}else{
			res.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/findAll/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void search(HttpServletRequest re, HttpServletResponse rs, @PathVariable int page) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 1,3)){
		AsignadorDeCharset.asignar(re, rs);
		List<Venta> lista= ventadao.todos(page);
		rs.getWriter().println(JsonConvertidor.toJson(lista));
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/numPages" }, method = RequestMethod.GET, produces = "application/json")
	public void pages(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		rs.getWriter().print(ventadao.pages());
	}
	
	@RequestMapping(value = {
	"/numPagesProductos" }, method = RequestMethod.GET, produces = "application/json")
	public void pagesProductos(HttpServletRequest re, HttpServletResponse rs) throws IOException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 1)){
		int totalp= productodao.total();
		int totalt = tornillodao.total();
		int pages= ((totalp+totalt-1)/50)+1;
		rs.getWriter().print(ventadao.pages());
		}else{
			rs.sendError(403);
		}
	}
	
	@RequestMapping(value = {"/descargaNota/{id}" }, method = RequestMethod.GET)
	public void pdfNota(HttpServletRequest re, HttpServletResponse res, @PathVariable Long id) throws IOException{
		
		if(Util.verificarPermiso(re, usuariodao, perfildao, 1,3)){
		AsignadorDeCharset.asignar(re, res);
		res.setContentType("Application/PDF");
		Venta venta= ventadao.cargar(id);
		Cliente c= null;
		ClienteEnvases envsAcumulados= new ClienteEnvases();
		if(venta.getIdCliente()!=0){
			c= clientedao.cargar(venta.getIdCliente());
			envsAcumulados= envasesdao.getEnvases(c.getId());
			
		}
	
//		if (venta.getCliente().equals("Otro")){
//			System.out.println("no hay envases acumulados");
//			//ClienteEnvases envsAcumulados= new ClienteEnvases();
//		}else{
//			
//		}
		DatosEmisor de= emisordao.getById(venta.getIdEmisor());
	//	Comprobante cfdi=cvFactory.generarNota(venta, c,emisordao.getActivo());
		Comprobante cfdi=cvFactory.generarNota(venta, c,de);
		cfdi.setFolio("Folio: "+venta.getFolio());
		
	
		try {
//			TimbreFiscalDigital timbre= (TimbreFiscalDigital)cfdi.getComplemento().getAny().get(0);
//			String uuid= timbre.getUUID();
			PDFFactura pdfFactura = new PDFFactura();
			PdfWriter writer = PdfWriter.getInstance(pdfFactura.getDocument(), res.getOutputStream());
			pdfFactura.getDocument().open();
			pdfFactura.getPieDePagina().setUuid("Nota");
		//	DatosEmisor de= emisordao.getById(venta.getIdEmisor());
			System.out.println("emisordao:"+de);
			imagendao.addImagen("FQO150408K53","images/okue.jpg");
			Imagen imagen = imagendao.get("FQO150408K53");
			if (de.getRfc().equals("OIN980511H24")){
				imagendao.addImagen("OIN980511H24","images/logoOvani.jpg");
				 imagen = imagendao.get("OIN980511H24");
			}     //  imagen.scaleAbsolute(150, 70);
		
//			}else if(de.getRfc().equals("FQO150408K53")){
//				Image imagen = Image.getInstance("images/okue.jpg");
//			}
			
//			if (factura.getEstatus().equals(Estatus.CANCELADO)) {
//				pdfFactura.getPieDePagina().setFechaCancel(factura.getFechaCancelacion());
//				pdfFactura.getPieDePagina().setSelloCancel(factura.getSelloCancelacion());
//				pdfFactura.construirPdfCancelado(cfdi, factura.getSelloDigital(), factura.getCodigoQR(),factura.getSelloCancelacion(),factura.getFechaCancelacion());
//				pdfFactura.crearMarcaDeAgua("CANCELADO", writer);
//			}else{
			
			
				pdfFactura.construirPdf(cfdi, "", null, com.tikal.toledo.factura.Estatus.valueOf(venta.getEstatus()), 0, imagen, venta.getEnvase(), envsAcumulados, de);
//			}
			pdfFactura.getDocument().close();
			res.getOutputStream().flush();
			res.getOutputStream().close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		}else{
			res.sendError(403);
		}
	}
	
	
	@RequestMapping(value = {"/sendmail" }, method = RequestMethod.POST,consumes= "application/json")
	public void mail(HttpServletRequest re, HttpServletResponse res, @RequestBody String json) throws IOException, MessagingException, DocumentException{
		if(Util.verificarPermiso(re, usuariodao, perfildao, 1,3)){
		Venta venta= (Venta)JsonConvertidor.fromJson(json, Venta.class);
		Cliente c= clientedao.cargar(venta.getIdCliente());
		Factura f= facturadao.consultar(venta.getUuid());
		EmailSender mailero = new EmailSender();
		if(f!=null){
			if(c.getEmail()!=null){
				Comprobante cfdi= Util.unmarshallXML(f.getCfdiXML());
				mailero.enviaFactura(c.getEmail(), f, "", cfdi.getComplemento().getAny().get(0).toString());
				res.getWriter().print("Se envi�");
			}
		}
		}else{
			res.sendError(403);
		}
	}
	
	@RequestMapping(value = "/xmlDescarga/{uuid}", method = RequestMethod.GET, produces = "text/xml")
	public void obtenerXML(HttpServletRequest req, HttpServletResponse res, @PathVariable String uuid) throws IOException {
		if(Util.verificarPermiso(req, usuariodao, perfildao, 1,3)){
			AsignadorDeCharset.asignar(req, res);
			Factura factura = facturadao.consultar(uuid);
			PrintWriter writer = res.getWriter();
			if (factura != null) {
				res.setContentType("text/xml");
				writer.println(factura.getCfdiXML());
			} else {
				writer.println("La factuca con el folio fiscal (uuid) ".concat(uuid).concat(" no existe"));
			}
		}else{
			res.sendError(403);
		}
	}
	
	@RequestMapping(value = {
	"/productos/{page}" }, method = RequestMethod.GET, produces = "application/json")
	public void productos(HttpServletRequest re, HttpServletResponse rs,@PathVariable int page) throws IOException, SQLException{
		AsignadorDeCharset.asignar(re, rs);
		int totalp = productodao.total();
		int nump = totalp / 50;
		int offset = totalp % 50;
		nump++;
		int rest = nump - page;
		List<List> listaf= new ArrayList<List>();
		List<Producto> listap = productodao.todos(page);
//		if (rest < 1) {
//			List<Tornillo> lista = tornillodao.page(Math.abs(rest)+1);
//			if(lista.size()>0 && (totalp+lista.size() >50)){
//			  	lista= lista.subList(0, lista.size()-offset);
//				if(rest<0){
//					lista.addAll(tornillodao.page(Math.abs(rest)).subList(offset, 49));
//				}
//			}
//			listaf.add(lista);
//		}else{
//			listaf.add(new ArrayList<Tornillo>());
//		}
		
		listaf.add(listap);
		rs.getWriter().println(JsonConvertidor.toJson(listaf));
	}
	
	@RequestMapping(value = "/crearFolios", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public void crearFolios(HttpServletRequest req, HttpServletResponse res) throws IOException {
		seriesdao.crear();
	}
	
	@RequestMapping(value = "/corteDeCaja", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public void corte(HttpServletRequest req, HttpServletResponse res) throws IOException {
		System.out.println("-----------------***------------------------");
		if(Util.verificarPermiso(req, usuariodao, perfildao, 1,3)){
			AsignadorDeCharset.asignar(req, res);
			Date datef;
			Date datei;
			Calendar c = Calendar.getInstance();
			datei=c.getTime();
			c.add(Calendar.DATE, 1);
			datef = c.getTime();
			datei.setHours(0);
			datei.setMinutes(1);
			int dia1=datei.getDate();
			
			int dia2=datef.getDate();
//			List<Factura> lista = facturaDAO.buscar(datei, datef, rfc);
//			Reporte rep = new Reporte(lista);
		//	List<Venta> lista=ventadao.buscar(datei, datef);
			List<Venta> lista=ventadao.todos_();
			CorteDeCaja corte= new CorteDeCaja();
			corte.setVentas(lista);
			HSSFWorkbook reporte=corte.getReporte();
			reporte.write(res.getOutputStream());
		}
	}
	
	@RequestMapping(value = "/DetalleVentas", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public void detalle(HttpServletRequest req, HttpServletResponse res) throws IOException {
		System.out.println("-----------------***------------------------");
		if(Util.verificarPermiso(req, usuariodao, perfildao, 1,3)){
			AsignadorDeCharset.asignar(req, res);
			Date datef;
			Date datei;
			Calendar c = Calendar.getInstance();
			datei=c.getTime();
			c.add(Calendar.DATE, 1);
			datef = c.getTime();
			datei.setHours(0);
			datei.setMinutes(1);
			int dia1=datei.getDate();
			
			int dia2=datef.getDate();
//			List<Factura> lista = facturaDAO.buscar(datei, datef, rfc);
//			Reporte rep = new Reporte(lista);
		//	List<Venta> lista=ventadao.buscar(datei, datef);
			List<Venta> lista=ventadao.todos_();
			List<DetalleVentasVO> ld= new ArrayList<DetalleVentasVO>();
			for(Venta v:lista){
				System.out.println("venta::"+v.getFolio());
				for (Detalle det:v.getDetalles()){
					DetalleVentasVO d= new DetalleVentasVO();
					d.setFecha(v.getFecha().toLocaleString());
					d.setCliente(v.getCliente());
					d.setFolio(v.getFolio());
					d.setConcepto(det.getDescripcion());
					d.setUnidad(det.getUnidad());
					d.setCantidad(det.getCantidad());
					d.setPrecio(det.getPrecioUnitario());
					d.setImporte(det.getTotal());
					d.setFormaPago(v.getFormaDePago());
					d.setFacturado(v.getFactura());
					ld.add(d);
				}
			}
			
			
			System.out.println("listaaaaa:"+lista);
			DetalleVentas d= new DetalleVentas();
			d.setVentas(ld);
			HSSFWorkbook reporte=d.getReporte();
			reporte.write(res.getOutputStream());
		}
	}
	
	
	@RequestMapping(value = "/ReporteLotes", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public void lotes(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		if(Util.verificarPermiso(req, usuariodao, perfildao, 1,3)){
			AsignadorDeCharset.asignar(req, res);
			Date datef;
			Date datei;
			Calendar c = Calendar.getInstance();
			datei=c.getTime();
			c.add(Calendar.DATE, 1);
			datef = c.getTime();
			datei.setHours(0);
			datei.setMinutes(1);
			int dia1=datei.getDate();
			
			int dia2=datef.getDate();
//			List<Factura> lista = facturaDAO.buscar(datei, datef, rfc);
//			Reporte rep = new Reporte(lista);
			List<LoteVO> lista= new ArrayList<LoteVO>();
			List<Lote> lotes= lotedao.todos();
			System.out.println("lotesssssssso:"+lotes);
			for(Lote l:lotes){
				System.out.println("lote:"+l.getId());
				System.out.println("loteIdpro:"+l.getIdProducto());
			//	System.out.println("loteIdAlma:"+l.getIdAlmacen());
				Producto p=productodao.cargar(l.getIdProducto());
				if (p!=null){
					//System.out.println("p.getNombre:"+p.getNombre());
					String noma="-";
					if (l.getIdAlmacen()!=null){
						Almacen alma= adao.cargar(l.getIdAlmacen());
						noma=alma.getNombre();
					}
					String des=p.getNombre();
					if (des==null){
						des="--";
					}else{
						System.out.println("p.getNombre:"+p.getNombre());
					}
					String uni="-";
					if (p.getUnidadSat()!=null){
						uni=p.getUnidadSat();
					}else{
						uni="-";
					}
					System.out.println("Producto:"+p);
					System.out.println("almacen:"+noma);
					System.out.println("l:"+l);
					System.out.println("pnombre:"+des);
					System.out.println("punidad:"+p.getUnidadSat());
					System.out.println("cantidadlote:"+l.getCantidad());
					LoteVO lov= new LoteVO(l,des, noma,uni, l.getCantidad());
					System.out.println("loteVO:"+lov);
					lista.add(lov);
					System.out.println("---lista:"+lista);
				}else{
					Tornillo t= tornillodao.cargar(l.getIdProducto());
					if (t!=null){
						//System.out.println("p.getNombre:"+p.getNombre());
						String noma="-";
						if (l.getIdAlmacen()!=null){
							Almacen alma= adao.cargar(l.getIdAlmacen());
							noma=alma.getNombre();
						}
						String des=t.getNombre();
						if (des==null){
							des="--";
						}else{
							System.out.println("t.getNombre:"+t.getNombre());
						}
						String uni="-";
						if (t.getUnidadSat()!=null){
							uni=t.getUnidadSat();
						}else{
							uni="-";
						}
						System.out.println("Tornillo:"+t);
						System.out.println("almacen:"+noma);
						System.out.println("l:"+l);
						System.out.println("tnombre:"+des);
						System.out.println("tunidad:"+t.getUnidadSat());
						System.out.println("almapexis:"+l.getCantidad());
						LoteVO lov= new LoteVO(l,des, noma,uni, l.getCantidad());
						System.out.println("loteVO:"+lov);
						lista.add(lov);
						System.out.println("---lista:"+lista);
					}
					else{
						System.out.println("ya no existe el producto...");
					}
				}
			}
		//	List<LoteVO> lista=ventadao.buscar(datei, datef);
			ReporteLotes rep= new ReporteLotes();
			rep.setLotes(lista);
			HSSFWorkbook reporte=rep.getReporte();
			reporte.write(res.getOutputStream());
		}
	}
	
	
	private float incrementarInventario(List<Detalle> detalles) throws SQLException{
		float monto=0;
		for(Detalle d:detalles){
			if(d.getTipo()==0){
				Producto p= productodao.cargar(d.getIdProducto());
				double restante= p.getExistencia()+d.getCantidad();
				p.setExistencia((float)restante);
				List<Lote> lista= lotedao.porProducto(p.getId());
				double aux= d.getCantidad();
				
				for(Lote l : lista){
						l.setCantidad((float)l.getCantidad()+(float)aux);
						break;
				}
				lotedao.guardarLotes(lista);
				productodao.guardar(p);
				if(p.getMinimo()!=0){
					AlertaInventario c=alertadao.consultar(p.getId());
					if(c==null){
					if(p.getExistencia()< p.getMinimo()){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre();
						a.alerta="Inventario por debajo del m�nimo";
						alertadao.add(a);
					}else{if(p.getExistencia() < (p.getMinimo()*1.10)){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre();
						a.alerta="Inventario a punto de llegar al m�nimo";
						alertadao.add(a);
					}}
					}
				}
			}else{
				Tornillo p= tornillodao.cargar(d.getIdProducto());
				double restante= p.getExistencia()+d.getCantidad();
				p.setExistencia((float)restante);
				List<Lote> lista= lotedao.porProducto(p.getId());
				double aux= d.getCantidad();
				
				for(Lote l : lista){
						l.setCantidad(l.getCantidad()+ (float)aux);
						break;
				}
				lotedao.guardarLotes(lista);
				tornillodao.guardar(p);
				if(p.getMinimo()!=0){
					AlertaInventario c=alertadao.consultar(p.getId());
					if(c==null){
					if(p.getExistencia()< p.getMinimo()){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre();
						a.alerta="Inventario por debajo del m�nimo";
						alertadao.add(a);
					}else{if(p.getExistencia() < (p.getMinimo()*1.10)){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre()+" "+p.getMedidas();
						a.alerta="Inventario a punto de llegar al m�nimo";
						alertadao.add(a);
					}}
					}
				}
			}
			
			
			
			monto+= d.getPrecioUnitario()* d.getCantidad();
		}
		return monto;
	}
	
	private float actualizarInventario(List<Detalle> detalles) throws SQLException{
		float monto=0;
		for(Detalle d:detalles){
			if(d.getTipo()==0){
				Producto p= productodao.cargar(d.getIdProducto());
				double cantNeta= (d.getCantidad()*d.getPorcentaje())/100;
				double restante= p.getExistencia()-cantNeta;
				p.setExistencia((float)restante);
				List<Lote> lista= lotedao.porProducto(p.getId());
				double aux= d.getCantidad();
				
				for(Lote l : lista){
					if(aux< l.getCantidad()){
						l.setCantidad((float)l.getCantidad()-(float)aux);
						break;
					}else{
						aux= aux-l.getCantidad();
						l.setCantidad(0);
					}
				}
				lotedao.guardarLotes(lista);
				productodao.guardar(p);
				if(p.getMinimo()!=0){
					AlertaInventario c=alertadao.consultar(p.getId());
					if(c==null){
					if(p.getExistencia()< p.getMinimo()){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre();
						a.alerta="Inventario por debajo del m�nimo";
						alertadao.add(a);
					}else{if(p.getExistencia() < (p.getMinimo()*1.10)){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre();
						a.alerta="Inventario a punto de llegar al m�nimo";
						alertadao.add(a);
					}}
					}
				}
			}else{
				Tornillo p= tornillodao.cargar(d.getIdProducto());
				double restante= p.getExistencia()-d.getCantidad();
				p.setExistencia((float)restante);
				List<Lote> lista= lotedao.porProducto(p.getId());
				double aux= d.getCantidad();
				
				for(Lote l : lista){
					if(aux< l.getCantidad()){
						l.setCantidad(l.getCantidad()- (float)aux);
						break;
					}else{
						aux= aux-l.getCantidad();
						l.setCantidad(0);
					}
				}
				lotedao.guardarLotes(lista);
				tornillodao.guardar(p);
				if(p.getMinimo()!=0){
					AlertaInventario c=alertadao.consultar(p.getId());
					if(c==null){
					if(p.getExistencia()< p.getMinimo()){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre();
						a.alerta="Inventario por debajo del m�nimo";
						alertadao.add(a);
					}else{if(p.getExistencia() < (p.getMinimo()*1.10)){
						AlertaInventario a= new AlertaInventario();
						a.idproducto=p.getId();
						a.nombre=p.getNombre()+" "+p.getMedidas();
						a.alerta="Inventario a punto de llegar al m�nimo";
						alertadao.add(a);
					}}
					}
				}
			}
			
			
			
			monto+= d.getPrecioUnitario()* d.getCantidad();
		}
		return monto;
	}
	
	@RequestMapping(value = "/upCostos", method = RequestMethod.GET)
	public void up(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
//		List<Producto> lp= productodao.todos();
//		for (Producto p:lp){
//			p.setPrecioCredito(1);
//			p.setPrecioMayoreo(1);
//			p.setPrecioReferencia(1);
//			p.setPrecioMostrador(1);
//			productodao.guardar(p);
//			
//		}
		List<Lote> ll= lotedao.todos();
		for(Lote l:ll){
			l.setCosto(1);
		    lotedao.guardar(l);
		}
		
	
	}
		
}
	