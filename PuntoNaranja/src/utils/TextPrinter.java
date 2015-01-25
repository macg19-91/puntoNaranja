
package utils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.print.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            br = new BufferedReader(new FileReader("Files\\Trans"+new auth().leerArchivo("archivoTransacciones.txt")+".txt"));
        String line;
        
        try {
        while ((line = br.readLine()) != null) {
           // process the line.
            
           g.drawString(line, 10, place); 
           place+=20;
        }
        br.close();
        } catch (IOException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
        //g.drawString("Hello world!", 10, 30);
        //g.drawString(" ff", 10, 50);

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
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

    public void startPrinter() {
 
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
}