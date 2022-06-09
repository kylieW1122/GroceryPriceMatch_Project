import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class SearchPanel extends JPanel {
	private JTextField txtSearchForYour;
	private MainFrame mainFrame;
	private DataBase database;

	/**
	 * Create the panel.
	 */
	public SearchPanel() {
		mainFrame = MainFrame.getInstance();
		new DataBase();
	
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		ImageIcon imgSrchIcon = new ImageIcon(SearchPanel.class.getResource("/resources/1024px-Search_Icon.svg.png"));
		Image imageSrch = imgSrchIcon.getImage();
		Image newSrchImg = imageSrch.getScaledInstance( 5, 10,  java.awt.Image.SCALE_SMOOTH ) ; 
		imgSrchIcon = new ImageIcon(newSrchImg);
		
		ImageIcon imgGraphIcon = new ImageIcon(SearchPanel.class.getResource("/resources/graph.jpg"));
		Image imageGraph = imgGraphIcon.getImage();
		Image newGraphImg = imageGraph.getScaledInstance( 5, 6,  java.awt.Image.SCALE_SMOOTH ) ; 
		imgGraphIcon = new ImageIcon(newGraphImg);
					
		JLabel lblNewLabel = new JLabel("My Company");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, 28));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridwidth = 5;
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);
								
								txtSearchForYour = new JTextField();
								txtSearchForYour.setFont(new Font("Calibri", Font.PLAIN, 15));
								txtSearchForYour.setText("Search or Price Analysis on Your Item");
								//	txtSearchForYour.setSize(txtSearchForYour.getWidth(), txtSearchForYour.getHeight() + 1000);
									
									GridBagConstraints gbc_txtSearchForYour = new GridBagConstraints();
									gbc_txtSearchForYour.gridwidth = 3;
									gbc_txtSearchForYour.insets = new Insets(0, 0, 5, 5);
									gbc_txtSearchForYour.fill = GridBagConstraints.HORIZONTAL;
									gbc_txtSearchForYour.gridx = 3;
									gbc_txtSearchForYour.gridy = 3;
									add(txtSearchForYour, gbc_txtSearchForYour);
									txtSearchForYour.setColumns(10);
								
									
												
									JButton buttonSearch = new JButton("      Search      ");
									buttonSearch.setFont(new Font("Calibri", Font.PLAIN, 11));
									buttonSearch.setHorizontalAlignment(SwingConstants.LEFT);
									buttonSearch.setIcon(imgSrchIcon);
									GridBagConstraints gbc_buttonSearch = new GridBagConstraints();
									gbc_buttonSearch.anchor = GridBagConstraints.NORTHEAST;
									gbc_buttonSearch.insets = new Insets(0, 0, 5, 5);
									gbc_buttonSearch.gridx = 3;
									gbc_buttonSearch.gridy = 4;
									
									add(buttonSearch, gbc_buttonSearch);
									buttonSearch.addActionListener(new ActionListener()
									{
										  public void actionPerformed(ActionEvent e) {
									
										      String [][] data = {{"","",""}};  
											  String searchText = txtSearchForYour.getText();
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
									//		  mainFrame.getContentPane().removeAll();

											
											  mainFrame.revalidate();
											  mainFrame.repaint();
										  }
									});								
								
								JButton buttonPA = new JButton("Price Analysis");
								buttonPA.setHorizontalAlignment(SwingConstants.LEFT);
								buttonPA.setFont(new Font("Calibri", Font.PLAIN, 11));
								buttonPA.setIcon(imgGraphIcon);
								buttonPA.addActionListener(new ActionListener() {
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
								
								GridBagConstraints gbc_buttonPA = new GridBagConstraints();
								gbc_buttonPA.anchor = GridBagConstraints.NORTHWEST;
								gbc_buttonPA.insets = new Insets(0, 0, 5, 5);
								gbc_buttonPA.gridx = 4;
								gbc_buttonPA.gridy = 4;
								add(buttonPA, gbc_buttonPA);

	}

}
