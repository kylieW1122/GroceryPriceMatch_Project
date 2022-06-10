import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
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
	        drawAxes(g2);
	        drawBars(g2);
	        
	       
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
		
		        Color color = getRandomColor();
		        g2.setColor(color);
		        //g2.draw(rec);
		        g2.fill(rec);
		        
		        FontMetrics fm = g2.getFontMetrics();
		        int red = color.getRed();
		        int blue = color.getBlue();
		        int green = color.getGreen();
		        if ((red*0.299 + green*0.587 + blue*0.114) > 186) { 
		        	g2.setColor(Color.BLACK);
		        } else {
		        	g2.setColor(Color.WHITE);
		        }
		        
		 //       int x = xLeft + ((barWidth - fm.stringWidth(barName)) / 2);
		        int x = xLeft + (barWidth / 2);
		        int y = yTopLeft + height - 60;
		 //       g2.drawString(g2,x,  y, barName);
		        drawRotateText(g2, x,  y, barName);
		        
		        drawText(g2, x,  yTopLeft + 8, 8, value.toString());
		      
		        barCounter ++;
		    }
		
		    g2.setColor(original);
		}
		
		private void drawText(Graphics2D g2, int x, int y,int fontSize, String text) {
			Font original = g2.getFont();
			Font font = new Font(null, Font.BOLD, fontSize);    
			g2.setFont(font);
		    g2.drawString(text, x, y) ;
		    g2.setFont(original);
		}
		
		private void drawRotateText(Graphics2D g2, int x, int y, String text) {
			Font original = g2.getFont();
			Font font = new Font(null, Font.BOLD, 8);    
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.rotate(Math.toRadians(90), 0, 0);
			Font rotatedFont = font.deriveFont(affineTransform);
			
			g2.setFont(rotatedFont);
			g2.drawString(text,x,y);
			g2.setFont(original);
		 
		}
		private void drawAxes(Graphics2D g2) {
		
		
		    int rightX = chartX + chartwidth;
		    int topY = chartY - chartheight;
		    
		    Font original = g2.getFont();
		    Stroke origStoke = g2.getStroke();
		    g2.setStroke(new BasicStroke(5));
		
		    g2.drawLine(chartX, chartY, rightX, chartY);
		
		    g2.drawLine(chartX, chartY, chartX, topY);
		    Color origColor = g2.getColor(); 
		    g2.setColor(Color.BLACK);
		    drawText(g2, chartX + chartwidth/2, chartY + AXIS_OFFSET/2 +3, 12,  xLabel);
	//	    g2.drawString(xLabel, chartX + chartwidth/2, chartY + AXIS_OFFSET/2 +3) ;
		
		    // draw vertical string
		
		    drawText(g2, AXIS_OFFSET/2+3, chartY - chartheight/2, 12, yLabel);
	//	    g2.drawString(yLabel,AXIS_OFFSET/2+3, chartY - chartheight/2);

		
		    g2.setStroke(origStoke);
		    g2.setColor(origColor);
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
