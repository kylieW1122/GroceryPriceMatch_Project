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
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * @author Michelle
 * This is the price analysis panel which will call BarChartPanel to display the graph
 * Within this panel, there is a search text button for the user to search for the product keyword
 * There are two labels to display the item name and year for the searched product from the Statistics Canada data
 * There is a back button to go back to the search panel
 */
public class PriceAnalysisPanel extends JPanel implements ActionListener{
    JPanel barChartPanel;
    JLabel itemNameLabel, yearLabel; 
    JComboBox userInputSearch_box;
    GridBagConstraints gbc_panel;
    JButton searchButton;
    
    private String[] dataList;
    private Map<String, Double> dataMap = new HashMap<String, Double>();
    private String year;
    /**
     * Create the panel.
     */
    public PriceAnalysisPanel(String item, String year, Map<String, Double> graphContent) {
        this.year = year;
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        this.setLayout(gridBagLayout);
        
        /*******set up search list for jcombobox********/
        ArrayList<String> statItemList = DataBase.getStatisticsItemList();
        this.dataList = new String[statItemList.size()+1];
        this.dataList[0] = "";
        for(int i=0; i<statItemList.size(); i++){
            String temp = statItemList.get(i);
            this.dataList[i+1] = temp;
        }
        
        this.setUpPanel(0);
    }
    private void setUpPanel(int selectedIndex){
        this.removeAll();
        
        userInputSearch_box = new AutoCompleteComboBox(this.dataList);
        userInputSearch_box.setSelectedIndex(selectedIndex);
        this.add(userInputSearch_box);
        String item = dataList[selectedIndex];
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        this.add(searchButton);
        
        itemNameLabel = new JLabel("Item name: " + item);
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 2;
        gbc_lblNewLabel_2.gridy = 3;
        this.add(itemNameLabel, gbc_lblNewLabel_2);
        
        yearLabel = new JLabel("Year: " + year);
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
        this.add(yearLabel, gbc_lblNewLabel_3);
        
        barChartPanel = new BarChartPanel(this.dataMap, "Month", "$");
        gbc_panel = new GridBagConstraints();
        gbc_panel.gridwidth = 4;
        gbc_panel.gridheight = 3;
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 2;
        gbc_panel.gridy = 4;

        this.add(barChartPanel, gbc_panel);
        this.revalidate();
        this.repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent event){
        if(event.getSource() instanceof JButton){
            JButton jButton = (JButton)event.getSource();
            if(jButton.equals(searchButton)){
                int selectedIndex = userInputSearch_box.getSelectedIndex();
                if(selectedIndex>0){
                    String itemName = dataList[selectedIndex];
                    this.dataMap = DataBase.getPriceListByItemNYear(itemName, year);
                    this.setUpPanel(selectedIndex); //update panel with a new bar chart
                }
            }
        }
    }
}