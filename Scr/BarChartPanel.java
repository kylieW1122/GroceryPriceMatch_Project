import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;

public class BarChartPanel extends JPanel{
	
	    public static final int TOP_BUFFER = 30; // where additional text is drawn
	    public static final int AXIS_OFFSET = 20;


	    private Map<String, Double> graphContent = new HashMap<>();

	    private int chartwidth, chartheight, chartX, chartY;

	    private String xLabel, yLabel;
	    
	    public BarChartPanel(Map<String, Double> map, String xTitle, String yTitle) {
	        super();
	      
	        graphContent = map;
	        xLabel = xTitle;
	        yLabel = yTitle;

	    }
	    
	    public void paintComponent(Graphics g) {
	    	super.paintComponent(g);

	      
	        computeSize();

	        Graphics2D g2 = (Graphics2D) g;
	        drawBars(g2);
	        drawAxes(g2);
	       
	    }
	    
	    
	    private void computeSize() {

	        int width = this.getWidth();
	        int height = this.getHeight();

	        // chart area size
	        chartwidth = width - 2*AXIS_OFFSET;
	        chartheight = height - 2*AXIS_OFFSET - TOP_BUFFER;

	        // Chart origin coords
	        chartX = AXIS_OFFSET;
	        chartY = height - AXIS_OFFSET;

	    }

		public void drawBars(Graphics2D g2) {
		
		    Color original = g2.getColor();
		
		    double numBars = graphContent.keySet().size();
		    double max = 0.;
		
		    for (Double wrapper : graphContent.values()) {
		        if (max < wrapper)
		            max = wrapper;
		    }
		//    System.out.println("max "+max);
		    int barWidth = (int) (chartwidth/numBars);
		
		    int  height, xLeft, yTopLeft;
		    Double value;
		    int barCounter = 0;
		    for (String barName : graphContent.keySet()) {
		        value = graphContent.get(barName);
		
		        double height2 = (value/max)*chartheight;
		        height = (int) height2;
		
		        xLeft = AXIS_OFFSET + barCounter * barWidth;
		        yTopLeft = chartY - height;
		        Rectangle rec = new Rectangle(xLeft, yTopLeft, barWidth, height);
		
		        g2.setColor(getRandomColor());
		        //g2.draw(rec);
		        g2.fill(rec);
		        
		        FontMetrics fm = g2.getFontMetrics();
		        g2.setColor(Color.BLACK);
		        
		        int x = xLeft + ((barWidth - fm.stringWidth("A")) / 2);
		        int y = yTopLeft + height;
		 //       g2.drawString("A", , yTopLeft + height);
		        drawText(g2,x,  y, barName);
		        
		        drawText(g2, x,  yTopLeft, value.toString());
		      
		        barCounter ++;
		    }
		
		    g2.setColor(original);
		}
		
		private void drawText(Graphics2D g2, int x, int y, String text) {
			
		    g2.drawString(text, x, y) ;
		}
		
		private void drawAxes(Graphics2D g2) {
		
		
		    int rightX = chartX + chartwidth;
		    int topY = chartY - chartheight;
		
		    g2.drawLine(chartX, chartY, rightX, chartY);
		
		    g2.drawLine(chartX, chartY, chartX, topY);
		
		    g2.drawString(xLabel, chartX + chartwidth/2, chartY + AXIS_OFFSET/2 +3) ;
		
		    // draw vertical string
		
		    Font original = g2.getFont();
		
		    Font font = new Font(null, original.getStyle(), original.getSize());    
		    AffineTransform affineTransform = new AffineTransform();
		    affineTransform.rotate(Math.toRadians(-90), 0, 0);
		    Font rotatedFont = font.deriveFont(affineTransform);
		    g2.setFont(rotatedFont);
		    g2.drawString(yLabel,AXIS_OFFSET/2+3, chartY - chartheight/2);
		    g2.setFont(original);
		
		
		}
		

		
		private Color getRandomColor() {
		
		    Random rand = new Random();
		
		    float r = rand.nextFloat();
		    float g = rand.nextFloat();
		    float b = rand.nextFloat();
		
		    return new Color(r, g, b);
		}
		
		public static int random(int maxRange) {
		
			Random random = new Random();
		
			return random.nextInt(maxRange);
		}
		


}
