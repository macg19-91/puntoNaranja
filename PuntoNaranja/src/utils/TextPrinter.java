
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
import javax.jnlp.PrintService;
import javax.print.DocPrintJob;
import javax.print.PrintServiceLookup;
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
    public int print(Graphics g, PageFormat pf, int page) throws
                                                        PrinterException {

        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        int place=30;
        /* Now we perform our rendering */
        BufferedReader br;
        File Bitacora = new File("Files\\Bitacora");
        String[] lista=Bitacora.list();
        try {
            if(selected){
                br = new BufferedReader(new FileReader("Files\\Bitacora\\"+lista[cual]));
            }else{
                br = new BufferedReader(new FileReader("Files\\Bitacora\\"+lista[Bitacora.list().length-3]));
            }
            String line;
        /*int cuenta=0;
        try {
        while ((line = br.readLine()) != null) {      
                g.drawString(line, 0, place); 
                place+=20;
           cuenta++;
        }*/
        int cuenta=0;
        String tipo="";
        try {
        while((line = br.readLine()) != null) {   
            if(cuenta==0)g.drawString("Punto de venta: "+line, 0, place); 
            if(cuenta==1)g.drawString("Fecha: "+line, 0, place); 
            if(cuenta==2)g.drawString("Hora: "+line, 0, place); 
            if(cuenta==3){tipo=line;place-=20;}
            switch (tipo) {
                case "Recargas":
                    if(cuenta==4)g.drawString("Recarga automática (Tiempo Aire) Teléfono: "+line, 0, place); 
                    if(cuenta==5)g.drawString("Transacción: "+line, 0, place); 
                    if(cuenta==6)g.drawString("Monto: "+line, 0, place); 
                break;
                    
                case "Pines":
                    if(cuenta==4)g.drawString("Numero Pin: "+line, 0, place); 
                    if(cuenta==5)g.drawString("Venta Pin: "+line, 0, place); 
                    if(cuenta==6)g.drawString("Monto: "+line, 0, place); 
                break;
            }              
           place+=20;      
           cuenta++;
        }
        g.drawString("Gracias...", 0, place); 
        br.close();
        } catch (IOException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
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
        File Bitacora = new File("Files\\Bitacora");
        String[] lista=Bitacora.list();
        try {
            if(selected){
                br = new BufferedReader(new FileReader("Files\\Bitacora\\"+lista[cual]));
            }else{
                br = new BufferedReader(new FileReader("Files\\Bitacora\\"+lista[Bitacora.list().length-3]));
            }
            String line;
        int cuenta=0;
        String tipo="";
        try {
        while((line = br.readLine()) != null) {   
            if(cuenta==0)reporte+="Punto de venta: "+line+"\n";
            if(cuenta==1)reporte+="Fecha: "+line+"\n";
            if(cuenta==2)reporte+="Hora: "+line+"\n";
            if(cuenta==3)tipo=line;
            switch (tipo) {
                case "Recargas":
                    if(cuenta==4)reporte+="Recarga automática (Tiempo Aire) Teléfono: "+line+"\n";
                    if(cuenta==5)reporte+="Transacción: "+line+", ";
                    if(cuenta==6)reporte+="Monto: "+line+"\n";
                break;
                    
                case "Pines":
                    if(cuenta==4)reporte+="Numero Pin: "+line+"\n";
                    if(cuenta==5)reporte+="Venta Pin: "+line+", ";
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
        String printerNameDesired = new auth().leerArchivo("Sesion\\defaultPrinter.txt");

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

    public void setPrinter() throws IOException { 
                  //PrinterJob job = PrinterJob.getPrinterJob();
         pjob.setPrintable(this);
         boolean ok = pjob.printDialog();
         if (ok) {
            new auth().escribeFichero(pjob.getPrintService().getName(),"Sesion/defaultPrinter.txt");
              
         }
    }
    public void startPrinter(Boolean selected, int cual) {
 
                  //PrinterJob job = PrinterJob.getPrinterJob();
         pjob.setPrintable(this);
        /* boolean ok = job.printDialog();
         if (ok) {
             */
            this.selected=selected;
            this.cual=cual;
             try {
                  pjob.print();
             } catch (PrinterException ex) {
              /* The job did not successfully complete */
             }
        // }
    }
}