import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Font;

public class SearchResultsPanel extends JPanel {

	private JTable table;
	private DefaultTableModel model;
	String[] columnNames = {"Store", "Item name", "Price"};
	private MainFrame mainFrame;
 //   JTextField searchTextField = new JTextField();
	
	/**
	 * Create the panel.
	 */
	public SearchResultsPanel(String [][] data) {
		
		mainFrame = MainFrame.getInstance();
	
	  
	  model = new DefaultTableModel(data, columnNames) {
	      @Override
	      public Class getColumnClass(int column) {
	         return getValueAt(0, column).getClass();
	      }
	   };
	   	   
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.5, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

	    
	    
		ImageIcon imgSrchIcon = new ImageIcon(SearchResultsPanel.class.getResource("/resources/1024px-Search_Icon.svg.png"));
		Image imageSrch = imgSrchIcon.getImage();
		Image newSrchImg = imageSrch.getScaledInstance( 5, 10,  java.awt.Image.SCALE_SMOOTH ) ; 
		imgSrchIcon = new ImageIcon(newSrchImg);
		
		
		ImageIcon imgGraphIcon = new ImageIcon(SearchResultsPanel.class.getResource("/resources/graph.jpg"));
		Image imageGraph = imgGraphIcon.getImage();
		Image newGraphImg = imageGraph.getScaledInstance( 5, 10,  java.awt.Image.SCALE_SMOOTH ) ; 
		imgGraphIcon = new ImageIcon(newGraphImg);
		
		ImageIcon imgGoBackIcon = new ImageIcon(SearchResultsPanel.class.getResource("/resources/back.png"));
		Image imageGoBack = imgGoBackIcon.getImage();
		Image newGoBackImg = imageGoBack.getScaledInstance( 5, 10,  java.awt.Image.SCALE_SMOOTH ) ; 
		imgGoBackIcon = new ImageIcon(newGoBackImg);
	    Dimension dim = new Dimension(20, 5);
	    
	    JButton btnGoBack = new JButton("       Back      ");
	    btnGoBack.setHorizontalAlignment(SwingConstants.LEADING);
	    btnGoBack.setIcon(imgGoBackIcon);
	    btnGoBack.setPreferredSize(new Dimension(20, 5));
	    GridBagConstraints gbc_btnGoBack = new GridBagConstraints();
	    gbc_btnGoBack.anchor = GridBagConstraints.BELOW_BASELINE;
	    gbc_btnGoBack.insets = new Insets(0, 0, 5, 5);
	    gbc_btnGoBack.gridx = 3;
	    gbc_btnGoBack.gridy = 7;
	    
	    btnGoBack.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e) {
				
				  SearchPanel searchPanel = new SearchPanel();
				  mainFrame.setContentPane(searchPanel);
				  mainFrame.revalidate();
				  mainFrame.repaint();
			  }
		});		
	    
	    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
	    gbc_scrollPane.gridheight = 2;
	    
	    
	    table = new JTable(model);
	    table.setBorder(new LineBorder(new Color(0, 0, 0)));
	    JTableHeader header = table.getTableHeader();
	    
	    header.setBackground(Color.GREEN);
	    header.setForeground(Color.BLACK);
	    
	    JLabel lblNewLabel_1 = new JLabel("My Company");
	    lblNewLabel_1.setForeground(Color.BLUE);
	    lblNewLabel_1.setFont(new Font("Arial Black", Font.PLAIN, 18));
	    GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
	    gbc_lblNewLabel_1.gridwidth = 3;
	    gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
	    gbc_lblNewLabel_1.gridx = 1;
	    gbc_lblNewLabel_1.gridy = 1;
	    add(lblNewLabel_1, gbc_lblNewLabel_1);
	    
	    JLabel lblNewLabel = new JLabel("Search");
	    lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
	    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
	    gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
	    gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
	    gbc_lblNewLabel.gridx = 2;
	    gbc_lblNewLabel.gridy = 2;
	    add(lblNewLabel, gbc_lblNewLabel);
	    

	    JTextField searchTextField = new JTextField();
	    GridBagConstraints gbc_searchTextField = new GridBagConstraints();
	    gbc_searchTextField.gridwidth = 2;
	    gbc_searchTextField.insets = new Insets(0, 0, 5, 5);
	    gbc_searchTextField.fill = GridBagConstraints.HORIZONTAL;
	    gbc_searchTextField.gridx = 3;
	    gbc_searchTextField.gridy = 2;
	    add(searchTextField, gbc_searchTextField);
	    searchTextField.setColumns(10);
	    
	    
	    JButton btnSearch = new JButton("");
	    btnSearch.setIcon(imgSrchIcon);
	    GridBagConstraints gbc_btnSearch = new GridBagConstraints();
	    gbc_btnSearch.insets = new Insets(0, 0, 5, 5);
	    gbc_btnSearch.gridx = 5;
	    gbc_btnSearch.gridy = 2;
	    add(btnSearch, gbc_btnSearch);
	    
	    btnSearch.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e) {
		
			      String [][] data = {{"","",""}};  
				  String searchText = searchTextField.getText();
				  Map<String, Map<String, Double>> map = DataBase.productSearch(searchText);
				  
				  
				  if (map != null && !map.isEmpty()) {
					  int numOfItems = 1;
					  Set<String> mapKeys = map.keySet();
					  for (String key1: mapKeys) {
						  Map<String, Double> productMap = map.get(key1);
						  Set<String> productMapKeys = productMap.keySet();
						  for (String prodKey : productMapKeys) {
							  numOfItems ++;
						  }
					  }
					  data = new String[numOfItems][3];
					  
					  int mapCount = 0;
					  for (String key: mapKeys) {
						  Map<String, Double> productMap = map.get(key);
						  
						  Set<String> productMapKeys = productMap.keySet();
						  for (String prodKey : productMapKeys) {
							  data[mapCount][0] = key;
							  data[mapCount][1] = prodKey;
							  data[mapCount][2] = "$" + productMap.get(prodKey).toString();
							  mapCount ++;
						  }
					  }
				  }		  
				  SearchResultsPanel searchResultPanel = new SearchResultsPanel(data);
				  mainFrame.setContentPane(searchResultPanel);
		

				
				  mainFrame.revalidate();
				  mainFrame.repaint();
			  }
		});	
	    
	    
	    
	    JScrollPane scrollPane = new JScrollPane(table);
	    gbc_scrollPane.gridwidth = 4;
	    gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
	    gbc_scrollPane.fill = GridBagConstraints.BOTH;
	    gbc_scrollPane.gridx = 2;
	    gbc_scrollPane.gridy = 4;
	    add(scrollPane, gbc_scrollPane);
	    
	    add(btnGoBack, gbc_btnGoBack);
	    
	    JButton btnPriceAnalysis = new JButton("Price Analysis");
	    btnPriceAnalysis.setIcon(imgGraphIcon);
	    btnPriceAnalysis.setSize(20, 5);
	    GridBagConstraints gbc_btnPriceAnalysis = new GridBagConstraints();
	    gbc_btnPriceAnalysis.insets = new Insets(0, 0, 5, 5);
	    gbc_btnPriceAnalysis.gridx = 4;
	    gbc_btnPriceAnalysis.gridy = 7;
	    
	    btnPriceAnalysis.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
				
				  Map<String, Double>map = new HashMap<String, Double>();
				  map.put("2021/01", 12.99);
				  map.put("2021/02", 11.89);
				  map.put("2021/03", 10.02);
				  map.put("2021/04", 8.99);
				  map.put("2021/05", 10.99);
				  map.put("2021/06", 12.99);
				  map.put("2021/07", 13.99);
				  map.put("2021/08", 14.99);
				  map.put("2021/09", 15.99);
				  map.put("2021/10", 20.99);
				  map.put("2021/11", 18.99);
				  map.put("2021/12", 13.99);
				  
				  PriceAnalysisPanel priceAnalysisPanel = new PriceAnalysisPanel(map);
				  mainFrame.setContentPane(priceAnalysisPanel);
		//		  mainFrame.getContentPane().removeAll();

				
				  mainFrame.revalidate();
				  mainFrame.repaint();
			  }
		});
	    
	    add(btnPriceAnalysis, gbc_btnPriceAnalysis);



	}
	
	

}