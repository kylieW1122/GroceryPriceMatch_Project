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
import javax.swing.JList;

/**
 * @author Michelle 
 * This is the search panel which will call the SearchResultsPanel to display the search results or 
 * the PriceAnalysisPanel to display the graph result.
 * Within this panel, there is a search text button for the user to search for the product keyword.
 * There are two text boxes to display the graph.
 */

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

		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.1, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		ImageIcon imgSrchIcon = new ImageIcon(SearchPanel.class.getResource("/resources/1024px-Search_Icon.svg.png"));
		Image imageSrch = imgSrchIcon.getImage();
		Image newSrchImg = imageSrch.getScaledInstance(5, 10, java.awt.Image.SCALE_SMOOTH);
		imgSrchIcon = new ImageIcon(newSrchImg);

		ImageIcon imgGraphIcon = new ImageIcon(SearchPanel.class.getResource("/resources/graph.jpg"));
		Image imageGraph = imgGraphIcon.getImage();
		Image newGraphImg = imageGraph.getScaledInstance(5, 6, java.awt.Image.SCALE_SMOOTH);
		imgGraphIcon = new ImageIcon(newGraphImg);

		JLabel lblNewLabel = new JLabel("My Company");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, 28));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridwidth = 10;
		gbc_lblNewLabel.gridx = 3;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);

		txtSearchForYour = new JTextField();
		txtSearchForYour.setText("Search");
		txtSearchForYour.setFont(new Font("Calibri", Font.PLAIN, 15));

		GridBagConstraints gbc_txtSearchForYour = new GridBagConstraints();
		gbc_txtSearchForYour.gridwidth = 9;
		gbc_txtSearchForYour.insets = new Insets(0, 0, 5, 5);
		gbc_txtSearchForYour.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSearchForYour.gridx = 4;
		gbc_txtSearchForYour.gridy = 3;
		add(txtSearchForYour, gbc_txtSearchForYour);
		txtSearchForYour.setColumns(10);

		JButton buttonSearch = new JButton("     Search    ");
		buttonSearch.setFont(new Font("Calibri", Font.PLAIN, 11));
		buttonSearch.setHorizontalAlignment(SwingConstants.LEFT);
		buttonSearch.setIcon(imgSrchIcon);
		GridBagConstraints gbc_buttonSearch = new GridBagConstraints();
		gbc_buttonSearch.anchor = GridBagConstraints.NORTHWEST;
		gbc_buttonSearch.insets = new Insets(0, 0, 5, 5);
		gbc_buttonSearch.gridx = 13;
		gbc_buttonSearch.gridy = 3;

		add(buttonSearch, gbc_buttonSearch);
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String[][] data = { { "", "", "" } };
				String searchText = txtSearchForYour.getText();
				Map<String, Map<String, Double>> map = DataBase.productSearch(searchText);

				if (map != null && !map.isEmpty()) {
					int numOfItems = 1;
					Set<String> mapKeys = map.keySet();
					for (String key1 : mapKeys) {
						Map<String, Double> productMap = map.get(key1);
						Set<String> productMapKeys = productMap.keySet();
						for (String prodKey : productMapKeys) {
							numOfItems++;
						}
					}
					data = new String[numOfItems][3];

					int mapCount = 0;
					for (String key : mapKeys) {
						Map<String, Double> productMap = map.get(key);

						Set<String> productMapKeys = productMap.keySet();
						for (String prodKey : productMapKeys) {
							data[mapCount][0] = key;
							data[mapCount][1] = prodKey;
							data[mapCount][2] = "$" + productMap.get(prodKey).toString();
							mapCount++;
						}
					}
				}
				SearchResultsPanel searchResultPanel = new SearchResultsPanel(data);
				mainFrame.setContentPane(searchResultPanel);

				mainFrame.revalidate();
				mainFrame.repaint();
			}
		});

		JList list = new JList();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 7;
		gbc_list.gridy = 6;
		add(list, gbc_list);

		JLabel lblNewLabel_1 = new JLabel("Price Analysis Item Name");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.gridwidth = 3;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 4;
		gbc_lblNewLabel_1.gridy = 7;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Year");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 8;
		gbc_lblNewLabel_2.gridy = 7;
		add(lblNewLabel_2, gbc_lblNewLabel_2);

		JTextField itemNameTextField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 5;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 4;
		gbc_textField.gridy = 8;
		add(itemNameTextField, gbc_textField);
		itemNameTextField.setColumns(10);

		JTextField yearTextField = new JTextField("2021");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 4;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 9;
		gbc_textField_1.gridy = 8;
		add(yearTextField, gbc_textField_1);
		yearTextField.setColumns(10);

		JButton buttonPA = new JButton("Price Analysis");
		buttonPA.setHorizontalAlignment(SwingConstants.LEFT);
		buttonPA.setFont(new Font("Calibri", Font.PLAIN, 11));
		buttonPA.setIcon(imgGraphIcon);
		buttonPA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Map<String, Double> map = new HashMap<String, Double>();
				map.put("", 0.0);

				String item = itemNameTextField.getText();
				String year = yearTextField.getText();
				if (item != null && !item.isEmpty() && year != null && !year.isEmpty()) {
					map = DataBase.getPriceListByItemNYear(item, year);
				}
				PriceAnalysisPanel priceAnalysisPanel = new PriceAnalysisPanel(item, year, map);
				mainFrame.setContentPane(priceAnalysisPanel);

				mainFrame.revalidate();
				mainFrame.repaint();
			}
		});

		GridBagConstraints gbc_buttonPA = new GridBagConstraints();
		gbc_buttonPA.anchor = GridBagConstraints.NORTHWEST;
		gbc_buttonPA.insets = new Insets(0, 0, 5, 5);
		gbc_buttonPA.gridx = 13;
		gbc_buttonPA.gridy = 8;
		add(buttonPA, gbc_buttonPA);

	}

}
