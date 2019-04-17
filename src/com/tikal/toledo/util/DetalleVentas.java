package com.tikal.toledo.util;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import com.tikal.toledo.controllersRest.VO.DetalleVentasVO;
import com.tikal.toledo.controllersRest.VO.LoteVO;
import com.tikal.toledo.controllersRest.VO.VentaVO;
import com.tikal.toledo.model.Detalle;
import com.tikal.toledo.model.Venta;

public class DetalleVentas {	
	private List<DetalleVentasVO> ventas;

	public List<DetalleVentasVO> getVentas() {
		return ventas;
	}

	public void setVentas(List<DetalleVentasVO> ventas) {
		this.ventas = ventas;
	}
	
	public HSSFWorkbook getReporte() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "Hoja excel");

		
		System.out.println("listaaaaa:"+ventas);
		String[] headers = new String[] { "Fecha","Cliente", "Folio","Concepto","Unidad","Cantidad","Precio","Importe", "Forma de Pago", "Facturado"};
		Integer[] wd =                   {256*25,   256*20  ,  256*8, 256*60, 256*8,  256*10,       256*15,    256*20, 256*15, 256*15 };   
		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);

        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i <10; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
            sheet.setColumnWidth(i,wd[i]);
        }
        double totalCaja=0;
        for(int i=0; i<this.ventas.size();i++){
        	HSSFRow dataRow = sheet.createRow(i + 1);
        	DetalleVentasVO v= ventas.get(i);
//        	List<Detalle> ds= v.getDetalles();
//        	if(i==3){
//	        	for(Detalle d:ds){
//	        		v.llenarRenglon(dataRow);
//	        	}
//        	}
//         	
        	v.llenarRenglon(dataRow);
//        	if(v.getFacturado())!=null){
//        		if(v.getFacturado()).compareToIgnoreCase("Efectivo")==0){
//        			//totalCaja+=v.getImporte());
//        		}
//        	}
        	totalCaja=totalCaja+v.getImporte();
        	
        }
        HSSFRow dataRow = sheet.createRow(ventas.size()+3);
        HSSFCell cellT = dataRow.createCell(6);
        cellT.setCellValue("Total en Caja");
        cellT.setCellStyle(headerStyle);
       // dataRow.createCell(1).setCellValue(totalCaja);
        HSSFCell cell = dataRow.createCell(7);
        cell.setCellValue(totalCaja);
        cell.setCellStyle(headerStyle);
		return workbook;
	}
	
}
