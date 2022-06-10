import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PriceAnalysisPanel extends JPanel {
    
    private MainFrame mainFrame;
    
    /**
     * Create the panel.
     */
    public PriceAnalysisPanel(Map<String, Double> graphContent) {
        mainFrame = MainFrame.getInstance();
        
        
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        
        
        ImageIcon imgSrchIcon = new ImageIcon(SearchResultsPanel.class.getResource("/resources/1024px-Search_Icon.svg.png"));
        Image imageSrch = imgSrchIcon.getImage();
        Image newSrchImg = imageSrch.getScaledInstance( 5, 10,  java.awt.Image.SCALE_SMOOTH ) ; 
        imgSrchIcon = new ImageIcon(newSrchImg);
        
        
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
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.gridwidth = 2;
        gbc_textField.insets = new Insets(0, 0, 5, 5);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 3;
        gbc_textField.gridy = 2;
        add(searchTextField, gbc_textField);
        searchTextField.setColumns(10);
        
        
        JButton btnSearch = new JButton("");
        btnSearch.setIcon(imgSrchIcon);
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton.gridx = 5;
        gbc_btnNewButton.gridy = 2;
        add(btnSearch, gbc_btnNewButton);
        
        
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
        
        JPanel panel = new BarChartPanel(graphContent, "Month", "$");
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridwidth = 4;
        gbc_panel.gridheight = 4;
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 2;
        gbc_panel.gridy = 4;
        add(panel, gbc_panel);
        
        
        
    }
    
    
}
