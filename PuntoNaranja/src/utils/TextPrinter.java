
package utils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.print.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
/**
* Utility class to print some lines of text to
* the default printer.  Uses some default
* font settings, and gets the page size from
* the PrinterJob object.
*
* Note: this little example class does not
* handle pagination.  All the text must fit
* on a single page.
* 
* This class can also be used as a standalone
* utility.  If the main method is invoked, it
* reads lines of text from System.in, and
* prints them to the default printer.
*/

public class TextPrinter implements Printable {

                 static PrinterJob pjob = PrinterJob.getPrinterJob();
    int cual=0;
    boolean selected=false;
    boolean caja=false;
    String[] lineas;
    public int print(Graphics g, PageFormat pf, int page) throws
                                                        PrinterException {
        return 1;
    }
    public int printFactura() throws
                                                        PrinterException {


        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
//        Graphics2D g2d = (Graphics2D)g;
//        g2d.translate(pf.getImageableX(), pf.getImageableY());
//        int place=50;
        
        if(!caja){
        BufferedReader br;
        File Bitacora;
       if(Static.isWindows()) Bitacora = new File(System.getProperty("user.home")+"\\Documents\\Puntos Naranja\\Files\\Bitacora");
       else Bitacora = new File(System.getProperty("user.home")+"/Documents/Puntos Naranja/Files/Bitacora");
        String[] lista=Bitacora.list();
        try {
            if(Static.isWindows()){
                if(selected){
                    br = new BufferedReader(new FileReader(System.getProperty("user.home")+"\\Documents\\Puntos Naranja\\Files\\Bitacora\\"+lista[cual]));
                }else{
                    br = new BufferedReader(new FileReader(System.getProperty("user.home")+"\\Documents\\Puntos Naranja\\Files\\Bitacora\\"+lista[Bitacora.list().length-3]));
                }
            }else{
                if(selected){
                    br = new BufferedReader(new FileReader(System.getProperty("user.home")+"/Documents/Puntos Naranja/Files/Bitacora/"+lista[cual]));
                }else{
                    br = new BufferedReader(new FileReader(System.getProperty("user.home")+"/Documents/Puntos Naranja/Files/Bitacora/"+lista[Bitacora.list().length-3]));
                }
            }
            String line;
            
        int cuenta=0;
        String tipo="";
        String cadena = "";
        try {
            cadena += "Puntos Naranja \n";
        if(tipo.equals("Recargas")){
            cadena += "Recarga automática (Tiempo Aire) \n";
        }
        if(tipo.equals("Servicios")){
            cadena += "Servicios Públicos \n";
            
        }
        while((line = br.readLine()) != null) {   
            if(cuenta==0)cadena += "Punto de venta: "+line + "\n"; 
            if(cuenta==1)cadena += "Fecha: "+line+ "\n";
            if(cuenta==2)cadena += "Hora: "+line + "\n";
            if(cuenta==3){tipo=line;}
            switch (tipo) {
                case "Recargas":
                    if(cuenta==4)cadena += "Teléfono: "+line + "\n";
                    if(cuenta==5)cadena += "Transacción: "+line + "\n";
                    if(cuenta==6)cadena += "Monto: "+line + "\n";
                break;
                    
                case "Pines":
                    if(cuenta==4)cadena += "Numero Pin: "+line + "\n";
                    if(cuenta==5)cadena += "Venta Pin: "+line + "\n";
                    if(cuenta==6)cadena += "Monto: "+line + "\n";
                break;
                    
                case "Servicios":
                    if(cuenta==4)cadena += "Identificador: "+line + "\n";
                    if(cuenta==5)cadena += "Servicio: "+line + "\n";
                    if(cuenta==6)cadena += "Monto: "+line + "\n";
                break;
            }              
           cuenta++;
        }
        cadena += "Gracias..." + "\n";
        br.close();
        //JOptionPane.showMessageDialog(null, cadena);
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        //Aqui selecciona tu impresora, el ejemplo tomará la impresora predeterminada.
                PrintService service = PrintServiceLookup.lookupDefaultPrintService();
                DocPrintJob pj = service.createPrintJob();
                byte[] bytes = cadena.getBytes();
                Doc doc = new SimpleDoc(bytes, flavor, null);
                try {
                    pj.print(doc, null);

                } catch (PrintException e) {
                    //System.out.println("cadena"+e.getMessage());
                    //JOptionPane.showMessageDialog(null, e.getMessage());
           
                }
        } catch (IOException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        }else{
//            for (String linea : lineas) {
//                g.drawString(linea, 0, place);
//                place+=20; 
//            }
        }
        
        return PAGE_EXISTS;
    }
    public void setLineas(String[] lineas){
        this.lineas=lineas;
    }
    private String getFecha() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return  addCero(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)-1),2) +"/"+ addCero(Integer.toString(cal.get(Calendar.MONTH)+1),2) +"/"+ Integer.toString(cal.get(Calendar.YEAR));
    }

    private String getMes() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        return addCero(Integer.toString(cal.get(Calendar.MONTH)+1),2);
    }
    
    private String addCero(String monto, int i) {
        return String.format("%0"+i+"d", Integer.parseInt(monto));
    };
    
    public String loadFileToArea(Boolean selected, int cual){
        String reporte="";
        BufferedReader br;
         File Bitacora;
       if(Static.isWindows()) Bitacora = new File(System.getProperty("user.home")+"\\Documents\\Puntos Naranja\\Files\\Bitacora");
       else Bitacora = new File(System.getProperty("user.home")+"/Documents/Puntos Naranja/Files/Bitacora");
        String[] lista=Bitacora.list();
        try {
            if(Static.isWindows()){
                if(selected){
                    br = new BufferedReader(new FileReader(System.getProperty("user.home")+"\\Documents\\Puntos Naranja\\Files\\Bitacora\\"+lista[cual]));
                }else{
                    br = new BufferedReader(new FileReader(System.getProperty("user.home")+"\\Documents\\Puntos Naranja\\Files\\Bitacora\\"+lista[Bitacora.list().length-3]));
                }
            }else{
                if(selected){
                    br = new BufferedReader(new FileReader(System.getProperty("user.home")+"/Documents/Puntos Naranja/Files/Bitacora/"+lista[cual]));
                }else{
                    br = new BufferedReader(new FileReader(System.getProperty("user.home")+"/Documents/Puntos Naranja/Files/Bitacora/"+lista[Bitacora.list().length-3]));
                }
            }
            String line;
        int cuenta=0;
        String tipo="";
        try {
            reporte+="Puntos Naranja \n";
        while((line = br.readLine()) != null) {   
            if(cuenta==0)reporte+="Punto de venta: "+line+"\n";
            if(cuenta==1)reporte+="Fecha: "+line+"\n";
            if(cuenta==2)reporte+="Hora: "+line+"\n";
            if(cuenta==3)tipo=line;
            switch (tipo) {
                case "Recargas":
                    if(cuenta==4)reporte+="Recarga automática (Tiempo Aire) \n Teléfono: "+line+"\n";
                    if(cuenta==5)reporte+="Transacción: "+line+", ";
                    if(cuenta==6)reporte+="Monto: "+line+"\n";
                break;
                    
                case "Pines":
                    if(cuenta==4)reporte+="Numero Pin: "+line+"\n";
                    if(cuenta==5)reporte+="Venta Pin: "+line+", ";
                    if(cuenta==6)reporte+="Monto: "+line+"\n";
                break;
                    
                case "Servicios":
                    if(cuenta==4)reporte+="Servicios Públicos \n Identificador: "+line+"\n";
                    if(cuenta==5)reporte+="Servicio: "+line+"\n";
                    if(cuenta==6)reporte+="Monto: "+line+"\n";
                break;
            }
                    
           cuenta++;
        }
        reporte+="Gracias...";
        br.close();
        } catch (IOException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reporte;
    }
    public static void test() throws PrinterException {
        DocPrintJob docPrintJob=null;
        String printerNameDesired;
        if(Static.isWindows())printerNameDesired = new auth().leerArchivo("Sesion\\defaultPrinter.txt");
        else printerNameDesired = new auth().leerArchivo("Sesion/defaultPrinter.txt");
        javax.print.PrintService[] service = PrinterJob.lookupPrintServices(); // list of printers

    int count = service.length;

    for (int i = 0; i < count; i++) {
        if (service[i].getName().equalsIgnoreCase(printerNameDesired )) {
            docPrintJob = service[i].createPrintJob();
            i = count;
        }
    }
//PrinterJob pjob = PrinterJob.getPrinterJob();
pjob.setPrintService(docPrintJob.getPrintService());
pjob.setJobName("job");
//pjob.print();
    }
    public void actionPerformed(ActionEvent e) {
         PrinterJob job = PrinterJob.getPrinterJob();
         job.setPrintable(this);
         boolean ok = job.printDialog();
         if (ok) {
             try {
                  job.print();
             } catch (PrinterException ex) {
              /* The job did not successfully complete */
             }
         }
    }

    public boolean setPrinter() throws IOException { 
                  //PrinterJob job = PrinterJob.getPrinterJob();
         pjob.setPrintable(this);
         boolean ok = pjob.printDialog();
         
         if (ok) {
            new auth().escribeFichero(pjob.getPrintService().getName(),"Sesion/defaultPrinter.txt");
            return true;
         }
         return false;
    }
    public void startPrinter(Boolean selected, int cual,Boolean caja) throws IOException{
 
                  //PrinterJob job = PrinterJob.getPrinterJob();
         pjob.setPrintable(this);
        /* boolean ok = job.printDialog();
         if (ok) {
             */
            this.selected=selected;
            this.cual=cual;
            this.caja=caja;
            try {
                  pjob.print();
             } catch (PrinterException ex) {
              JOptionPane.showMessageDialog(null, "Revise la conexión a la impresora");
             }
        // }
    }
    
}
    
