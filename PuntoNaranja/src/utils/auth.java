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
        verificaCarpetas();
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
        return addCero(Integer.toString(cal.get(Calendar.MONTH)+1),2) + addCero(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)-1),2) + Integer.toString(cal.get(Calendar.YEAR));
    }

    private String getMes() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return addCero(Integer.toString(cal.get(Calendar.MONTH)+1),2);
    }
    private String getMesAntes() {
        Calendar cal = Calendar.getInstance();
        if("01".equals(getMes())){return "12";}
        return addCero(Integer.toString(cal.get(Calendar.MONTH)),2);
    }
     private String getHora() {
        Calendar cal = Calendar.getInstance();
        return addCero(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)),2) +":"+ addCero(Integer.toString(cal.get(Calendar.MINUTE)),2) +":"+ addCero(Integer.toString(cal.get(Calendar.SECOND)),2);
    }
    
public void escribeFicheroPrint(String monto,String num,String empresa,String nombre) throws IOException
    {
        verificaCarpetas();
        String fecha= leerArchivo("Bitacora\\archivoFecha.txt");        
        File Bitacora = new File("Files\\Bitacora");
        if(!fecha.equals(getMes())){
            int total=Bitacora.list().length;
            if(total>11){
                for(int i=0;i<total-11;i++){  
                    int j=i+1;
                    File removed = new File("Files\\Bitacora\\"+getMesAntes()+"-"+j+".txt");
                    removed.delete();
                }
            }
            fecha=getFecha();
            escribeFichero(getMes(), "Bitacora/archivoFecha.txt");
            escribeFichero("0", "Bitacora/archivoTransacciones.txt");
        }
        String trans= leerArchivo("Bitacora\\archivoTransacciones.txt");
            int cuantas=Integer.parseInt(trans)+1;
            escribeFichero(cuantas+"", "Bitacora/archivoTransacciones.txt");
            
        File fout = new File("Files/Bitacora/"+getMes()+"-"+cuantas+".txt");
      
	FileOutputStream fos = new FileOutputStream(fout);
 
	OutputStreamWriter osw = new OutputStreamWriter(fos);
            
                    osw.write("Punto de venta: "+new auth().leerArchivo("Sesion\\archivoUser.txt"));
                    osw.write(System.lineSeparator());
                    osw.write("Fecha: "+fecha);
                    osw.write(System.lineSeparator());
                    osw.write("Hora: "+getHora());
                    osw.write(System.lineSeparator());
	
            switch (nombre) {
                case "Recargas":
                    osw.write("Recarga automática (Tiempo Aire) Teléfono: "+num);
                    osw.write(System.lineSeparator());
                    osw.write("Transacción: "+empresa+", Monto: "+monto);
                    osw.write(System.lineSeparator());
                break;
                    
                case "Pines":
                    osw.write("Venta de Pin "+empresa+", Monto: "+monto);
                    osw.write(System.lineSeparator());
                break;
            }
                    osw.write("Gracias...");
 
	osw.close();
    }

public Boolean firstLogin()
    {
        File Sesion = new File("Files\\Sesion");
        File Bitacora = new File("Files\\Bitacora");
        int num=Bitacora.list().length;
        //File folderSesion = new File("Files\\Sesion");
        String ruta = "Files/Sesion/archivoPassword.txt";
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            Sesion.mkdirs();
            Bitacora.mkdirs();
            return false;
        }
        return true;
    }

public void verificaCarpetas(){ 
       // return file.exists();
        File Sesion = new File("Files\\Sesion");
        File Bitacora = new File("Files\\Bitacora");
        
        if (!Sesion.exists()) {
            Sesion.mkdirs();
        }
        if (!Bitacora.exists()) {
            Bitacora.mkdirs();
        }
}
public Boolean verificaFichero(File file){ 
        return file.exists();
}

private String addCero(String monto, int i) {
        return String.format("%0"+i+"d", Integer.parseInt(monto));
    };
}

