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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        Calendar cal = Calendar.getInstance();
        return addCero(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)),2) +"/"+addCero(Integer.toString(cal.get(Calendar.MONTH)+1),2) +"/"+ Integer.toString(cal.get(Calendar.YEAR));
    }
    
    public String getDia() {
        Calendar cal = Calendar.getInstance();
        return addCero(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)),2);
    }

    public String getMes() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return addCero(Integer.toString(cal.get(Calendar.MONTH)+1),2);
    }
    private String getMesAntes() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        if("01".equals(getMes())){return "11";}
        if("02".equals(getMes())){return "12";}
        return addCero(Integer.toString(cal.get(Calendar.MONTH)),2);
    }
     private String getHora() {
        Calendar cal = Calendar.getInstance();
        return addCero(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)),2) +":"+ addCero(Integer.toString(cal.get(Calendar.MINUTE)),2) +":"+ addCero(Integer.toString(cal.get(Calendar.SECOND)),2);
    }
public String[] bitacora(){
    File Bitacora = new File("Files\\Bitacora");
    return Bitacora.list();
}    
public String returnRow(String fileName){
        String reporte="";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("Files\\Bitacora\\"+fileName));
        String line;
        int cuenta=0;
        try {
        while ((line = br.readLine()) != null) {
            reporte+=line+"&-&";
           cuenta++;
        }
        br.close();
        } catch (IOException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reporte;
    }
public void escribeFicheroPrint(String monto,String num,String empresa,String nombre) throws IOException
    {
        verificaCarpetas();
        String fecha= leerArchivo("Bitacora\\archivoFecha.txt");        
        File Bitacora = new File("Files\\Bitacora");
        if(!fecha.equals(getMes())){
            String[] listArchivos=Bitacora.list();
            int total=0;
            if(listArchivos.length>0){
                while(!listArchivos[total].split("-")[0].equals(getMesAntes())&&!fecha.equals(getMes())&&Bitacora.list().length>11){
                    Bitacora = new File("Files\\Bitacora");
                    
                    File removed = new File("Files\\Bitacora\\"+listArchivos[total]);
                    removed.delete();
                    total++;
                }
            }
           /* if(total>11){
                for(int i=0;i<total-11;i++){  
                    int j=i+1;
                    File removed = new File("Files\\Bitacora\\"+listArchivos[i]);
                    removed.delete();
                }
            }*/
            escribeFichero(getMes(), "Bitacora/archivoFecha.txt");
            escribeFichero("0", "Bitacora/archivoTransacciones.txt");
        }
        String trans= leerArchivo("Bitacora\\archivoTransacciones.txt");
            int cuantas=Integer.parseInt(trans)+1;
            escribeFichero(addCero(cuantas+"",2)+"", "Bitacora/archivoTransacciones.txt");
            
        File fout = new File("Files/Bitacora/"+getMes()+"-"+getDia()+"-"+addCero(cuantas+"",2)+".txt");
      
	FileOutputStream fos = new FileOutputStream(fout);
 
	OutputStreamWriter osw = new OutputStreamWriter(fos);
            
                    osw.write(new auth().leerArchivo("Sesion\\archivoUser.txt"));
                    osw.write(System.lineSeparator());
                    osw.write(getFecha());
                    osw.write(System.lineSeparator());
                    osw.write(getHora());
                    osw.write(System.lineSeparator());
                    osw.write(nombre);
                    osw.write(System.lineSeparator());
                    osw.write(num);
                    osw.write(System.lineSeparator());
                    osw.write(empresa);
                    osw.write(System.lineSeparator());
                    osw.write(monto);
 
	osw.close();
    }

public Boolean firstLogin()
    {
        File Sesion = new File("Files\\Sesion");
        File Bitacora = new File("Files\\Bitacora");
        //File folderSesion = new File("Files\\Sesion");
        String ruta = "Files/Sesion/archivoPassword.txt";
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            Sesion.mkdirs();
            Bitacora.mkdirs();
            try {
                escribeFichero("true","Sesion/archivoPidePassword.txt");
            } catch (IOException ex) {
                Logger.getLogger(auth.class.getName()).log(Level.SEVERE, null, ex);
            }
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

public String addCero(String monto, int i) {
        return String.format("%0"+i+"d", Integer.parseInt(monto));
    };
}

