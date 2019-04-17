package localhost;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
 
public class EncodeBase64 {

	public  String  encode (String fileLocat) {
 
		Base64 base64 = new Base64();
		
		//"C:\\Users\\Secarqint\\Desktop\\Kit-de-Integracion-CSharp-DLL\\Kit de Integracion CSharpV2.0\\DemoCSTimbraCFDI\\ArchivosPrueba\\comprobanteSinTimbrar.xml"
		File file = new File(fileLocat);
		byte[] fileArray = new byte[(int) file.length()];
					
		InputStream inputStream;
		String encodedFile = "";
		
		try {
			inputStream = new FileInputStream(file);
								
			inputStream.read(fileArray);
			
			encodedFile = new String(base64.encode(fileArray));
			
			
			} 
		catch (Exception e) {
			// Manejar Error
			System.out.println(e.toString());
		}
		return encodedFile;
		
		/*----------------CADENAS-----------------
		String cadena = "Cadena";
		String encodedString = "";
		encodedString = base64.encodeToString(cadena.getBytes());
		System.out.println(encodedString);*/
	}
	
	public String encodeByteArrayIS(InputStream inputStream) {
		Base64 base64 = new Base64();
		String encodedFile = "";
		try {
			if (inputStream == null) {
				return encodedFile;
			}
			byte[] byteArray = new byte[inputStream.available()];
			inputStream.read(byteArray);
			encodedFile = new String(base64.encode(byteArray));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encodedFile;
	}
	
	public String encodeFile(File f) {
		Base64 base64 = new Base64();
		byte[] fileArray = new byte[(int) f.length()];
		
		InputStream inputStream;
		String encodedFile = "";
		
		try {
			inputStream = new FileInputStream(f);
								
			inputStream.read(fileArray);
									
			encodedFile = new String(base64.encode(fileArray));
			
			
			} 
		catch (Exception e) {
			// Manejar Error
			System.out.println(e.toString());
		}
		return encodedFile;
	}
	
	public String encodeString(String cadena) {
		try {	
			Base64 base64 = new Base64();
			String encodedString = new String(base64.encode(cadena.getBytes("UTF-8")));
			return encodedString;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String encodeStringSelloCancelacion(String cadena) {
		try {	
			Base64 base64 = new Base64();
			String encodedString = new String(base64.encode(cadena.getBytes("ISO-8859-1")));//  ISO-8859-1
			return encodedString;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
//	private static String leerArchivo(String fileLocat){
//		
//		String theString="";
//		try {
//			File file = new File(fileLocat);
//			StringWriter writer =new StringWriter();
//			IOUtils.copy(new FileInputStream(file),writer,"UTF-8");
//			theString=writer.toString();
//		
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		return theString;
//		
//	}
 
}