import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.JFrame;  
import javax.swing.SwingUtilities;  
  
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.data.category.DefaultCategoryDataset;  
/**[PriceAnalysisPage.java]
  * This is final project - price match program
  * This class should be an inner class inside HomePage
  * @author Kylie Wong and Michelle Chan, ICS4UE
  * @version 1.0, build May 31, 2022
  */

//https://www.javatpoint.com/jfreechart-line-chart <--- jpanel graph
//https://docs.oracle.com/javafx/2/charts/line-chart.htm <--- application
public class PriceAnalysisPage extends JPanel{ // implements ActionListener
    JFrame mainWindow;
    //JPanel 
    PriceAnalysisPage(HashMap<String, double[]> priceDatabase){
        /******************set JFrame********************/
        mainWindow = new JFrame("Price Analysis Page");
        mainWindow.setSize(750,500);
        mainWindow.setResizable(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create dataset
        DefaultCategoryDataset dataset = createDataset();
        
        this.setVisible(true);
        mainWindow.add(this);
        mainWindow.setVisible(true);
    }
}