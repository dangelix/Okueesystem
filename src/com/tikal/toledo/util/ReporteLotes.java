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

import com.tikal.toledo.controllersRest.VO.LoteVO;

	public class ReporteLotes {	
		private List<LoteVO> lotes;

		public List<LoteVO> getLotes() {
			return lotes;
		}

		public void setLotes(List<LoteVO> lotes) {
			this.lotes = lotes;
		}
		
		public HSSFWorkbook getReporte() {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			workbook.setSheetName(0, "Hoja excel");

			String[] headers = new String[] { "Lote", "Producto", "Unidad ","Fecha Lote", "Almacen","Costo","Cantidad","Rack","Nivel","Posición","Cuadricula"};
			Integer[] wd =                   {256*22,   256*70  ,  256*15, 256*25, 256*25,  256*20, 256*15,	   256*15,   256*10, 256*10, 256*10,     256*10  };   
			
			 Row fila = sheet.createRow(4);
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			headerStyle.setFont(font);

	        HSSFRow headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; ++i) {
	            String header = headers[i];
	            HSSFCell cell = headerRow.createCell(i);
	            cell.setCellStyle(headerStyle);
	            cell.setCellValue(header);
	            sheet.setColumnWidth(i,wd[i]);
	        }
	        float totalLotes=0;
	        for(int i=0; i<this.lotes.size();i++){
	        	HSSFRow dataRow = sheet.createRow(i + 1);
	        	LoteVO l = lotes.get(i);
	        	l.llenarRenglonLotes(dataRow);
//	        	if(l.getFormaDePago()!=null){
//	        		if(v.getFormaDePago().compareToIgnoreCase("Efectivo")==0){
//	        			totalCaja+=v.getMonto();
//	        		}
//	        	}
	        	
	        }
	        HSSFRow dataRow = sheet.createRow(lotes.size()+3);
	        dataRow.createCell(0).setCellValue("Total en Caja");
	        dataRow.createCell(1).setCellValue(totalLotes);
	    //    sheet.setColumnWidth(0, 13*256);
//	        sheet.setColumnWidth(1, 35*256);
//	        sheet.setColumnWidth(2, 20*256);
//	        sheet.setColumnWidth(3, 15*256);
//	        sheet.setColumnWidth(4, 25*256);
//	        sheet.setColumnWidth(5, 20*256);
//	        sheet.setColumnWidth(6, 25*256);
//	        sheet.setColumnWidth(7, 40*256);
//	        sheet.setColumnWidth(8, 13*256);
//	        sheet.setColumnWidth(9, 13*256);
//	        sheet.setColumnWidth(10, 13*256);
//	        sheet.setColumnWidth(11, 20*256);
			return workbook;
		}
		
	}
