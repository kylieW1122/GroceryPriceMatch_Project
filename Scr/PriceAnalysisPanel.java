import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;

/**
 * @author Michelle, Kylie
 * This is the price analysis panel which will call BarChartPanel to display the graph
 * Within this panel, there is a search text button for the user to search for the product keyword
 * There are two labels to display the item name and year for the searched product from the Statistics Canada data
 * There is a back button to go back to the search panel
 */
public class PriceAnalysisPanel extends JPanel implements ActionListener{
    JPanel barChartPanel, centerPanel;
    JPanel searchPanel, titlePanel;
    JLabel creditLabel;
    JLabel itemNameLabel, yearLabel; 
    JComboBox userInputSearch_box;
    JButton searchButton;
    
    private String[] dataList;
    private Map<String, Double> dataMap = new HashMap<String, Double>();
    private String year = "2021";
    /**
     * Create the panel.
     */
    public PriceAnalysisPanel() {
        this.year = year;
        centerPanel = new JPanel();
        searchPanel = new JPanel();
        titlePanel = new JPanel();
        this.setLayout(new BorderLayout());
        
        /*******set up search list for jcombobox********/
        ArrayList<String> statItemList = DataBase.getStatisticsItemList();
        this.dataList = new String[statItemList.size()+1];
        this.dataList[0] = "";
        for(int i=0; i<statItemList.size(); i++){
            String temp = statItemList.get(i);
            this.dataList[i+1] = temp;
        }
        
        this.setUpPanel(0); //selectedIndex=0
    }
    private void setUpPanel(int selectedIndex){
        this.removeAll();
        searchPanel.removeAll();
        centerPanel.removeAll();
        titlePanel.removeAll();
        
        String item = dataList[selectedIndex];
        
        userInputSearch_box = new AutoCompleteComboBox(this.dataList);
        userInputSearch_box.setSelectedIndex(selectedIndex);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(userInputSearch_box);
        searchPanel.add(searchButton);
        this.add(searchPanel, BorderLayout.NORTH);
        
        itemNameLabel = new JLabel("Item name: " + item);
        yearLabel = new JLabel("Year: " + year);
        titlePanel.add(itemNameLabel);
        titlePanel.add(yearLabel);
        
        barChartPanel = new BarChartPanel(this.dataMap, "Month", "$");
        
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(titlePanel);
        centerPanel.add(barChartPanel);
        this.add(centerPanel, BorderLayout.CENTER);
        
        creditLabel = new JLabel("Data from Statitics Canada");
        creditLabel.setHorizontalAlignment(JLabel.CENTER);
        creditLabel.setVerticalAlignment(JLabel.CENTER);
        this.add(creditLabel, BorderLayout.SOUTH);    
        
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