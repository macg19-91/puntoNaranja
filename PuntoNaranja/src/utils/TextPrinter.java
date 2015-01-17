
package utils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.print.*;

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

        /* Now we perform our rendering */
        g.drawString("Hello world!"+System.lineSeparator()+" ff", 100, 100);

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