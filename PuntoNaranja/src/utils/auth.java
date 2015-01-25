/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author Marlon
 */
public class auth {
    public String leerArchivo(String ruta) {
      File archivo = null;
      FileReader fr = null;
      BufferedReader br = null;
 
      try {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).
         archivo = new File ("Files\\"+ruta);
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);
 
         // Lectura del fichero
         String linea;
         while((linea=br.readLine())!=null)
            return linea;
      }
      catch(Exception e){
         e.printStackTrace();
      }finally{
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
      }
      return "0"; 
   }
    
public void escribeFichero(String linea,String nombre) throws IOException
    {
        String ruta = "Files/"+nombre;
        File archivo = new File(ruta);
        BufferedWriter bw;
        if(verificaFichero(archivo)) {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(linea);
        } else {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(linea);
        }
        bw.close();
    }

    private String getFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return Integer.toString(cal.get(Calendar.MONTH)) + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + Integer.toString(cal.get(Calendar.YEAR));
    }
    
public void escribeFicheroPrint(String monto,String num,String empresa,String nombre) throws IOException
    {
        String fecha= leerArchivo("archivoFecha.txt");
        if(!fecha.equals(getFecha())){
            fecha=getFecha();
            escribeFichero(fecha, "archivoFecha.txt");
            escribeFichero("0", "archivoTransacciones.txt");
        }
        String trans= leerArchivo("archivoTransacciones.txt");
            int cuantas=Integer.parseInt(trans)+1;
            escribeFichero(cuantas+"", "archivoTransacciones.txt");
            
        File fout = new File("Files/"+"Trans"+cuantas+".txt");
      
	FileOutputStream fos = new FileOutputStream(fout);
 
	OutputStreamWriter osw = new OutputStreamWriter(fos);
 
	
            switch (nombre) {
                case "Recargas":
                    osw.write(empresa);
                    osw.write(System.lineSeparator());
                    osw.write("Numero: "+num);
                    osw.write(System.lineSeparator());
                    osw.write("Monto: "+monto);
                    osw.write(System.lineSeparator());
                    osw.write("Gracias...");
                break;
                    
                case "Pines":
                    osw.write("Venta de Pin "+empresa);
                    osw.write(System.lineSeparator());
                    osw.write("Monto: "+monto);
                    osw.write(System.lineSeparator());
                    osw.write("Gracias...");
                break;
            }
 
	osw.close();
    }

public Boolean firstLogin()
    {
        String ruta = "Files/archivoPassword.txt";
        File archivo = new File(ruta);
        return verificaFichero(archivo);
    }

public Boolean verificaFichero(File file){ 
        return file.exists();
}
}

