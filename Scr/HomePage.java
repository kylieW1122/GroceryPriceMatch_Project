import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.CardLayout;
import java.awt.*; // delete after - wildcard not accepted

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.*; // delete after - wildcard
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.SwingConstants;
/**[HomePage.java]
  * This is final project - price match program
  * This class contains the main method to start the program
  * 
  * @author Kylie Wong and Michelle Chan, ICS4UE
  */
public class HomePage extends JFrame implements ActionListener{
    private static String[] actionStrings = {"Home", "Search Item", "Price Analysis", "Group Order"};
    //HomePage mainhomePagePanel  
    private JPanel mainPanel;
    private SearchItemPage searchItemPanel;
    private PriceAnalysisPage priceAnalysisPanel;
    private GroupOrderPage groupOrderPanel;
    
    private final String SEARCH_STR = "Search: ";
    //private inner class panels 
    
    JPanel homePagePanel;
    
    JPanel midPanel;
    JLabel searchLabel;
    JTextField searchTextField;
    JButton priceAnalysisButton;
    JButton groupOrderButton;
    
    JPanel bottomPanel;
    JButton accountSettingButton;
    JButton reviewGrpOrderButton;
    
    JPanel cardPanel;
    CardLayout cl;
    JComboBox actionList;
    //
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        HomePage frame = new HomePage();
    }
//----------------------------------------------------------------------------
    HomePage(){
        this.setTitle("Company Name");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 500);
        this.setResizable(true);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        /********************set cardPanel**********************/
        actionList = new JComboBox(actionStrings);
        actionList.setSelectedIndex(0);
        actionList.addActionListener(this);
        cl = new CardLayout();
        cardPanel = new JPanel();
        cardPanel.setLayout(cl);
        /********************set homePagePanel - HomePagePanel**********************/
        homePagePanel = new JPanel();
        homePagePanel.setLayout(new BorderLayout());
        
        /*************************MID JPANEL**********************************/
        midPanel = new JPanel();
        midPanel.setBackground(Color.WHITE);
        
        searchLabel = new JLabel(SEARCH_STR);
        searchLabel.setHorizontalAlignment(SwingConstants.LEFT);
        searchTextField = new JTextField(30);
        
        priceAnalysisButton = new JButton("Price Analysis");
        groupOrderButton = new JButton("Group Order");
        groupOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        midPanel.add(searchLabel);
        midPanel.add(searchTextField);
        midPanel.add(priceAnalysisButton);
        midPanel.add(groupOrderButton);
        homePagePanel.add(midPanel, BorderLayout.CENTER);
        /************************BOTTOM PANEL**********************************/
        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        
        accountSettingButton = new JButton("Account Settings");
        accountSettingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        reviewGrpOrderButton = new JButton("Review Group Order");
        reviewGrpOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        bottomPanel.add(accountSettingButton);
        bottomPanel.add(reviewGrpOrderButton);
        homePagePanel.add(bottomPanel, BorderLayout.SOUTH);
        /*****************************ADD EVERYTHING INTO mainhomePagePanel************************************/
        cardPanel.add(homePagePanel, "1");
        searchItemPanel = new SearchItemPage();
        cardPanel.add(searchItemPanel, "2");
        priceAnalysisPanel = new PriceAnalysisPage();
        cardPanel.add(priceAnalysisPanel, "3");
        groupOrderPanel = new GroupOrderPage();
        cardPanel.add(groupOrderPanel, "4");
        
        /*****************************************************************/
        cl.show(cardPanel, "1");
        mainPanel.add(actionList, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        this.add(mainPanel);
        this.setVisible(true);
    }
//------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent event){
        if(event.getSource() instanceof JComboBox){
            JComboBox cb = (JComboBox)event.getSource();
            int index = cb.getSelectedIndex();
            String cardNo = Integer.toString(index+1);
            cl.show(cardPanel, cardNo);
        }
    }
//----------------------------------------------------------------------------
//inner class - SearchItemPage
//----------------------------------------------------------------------------
    private class SearchItemPage extends JPanel{
        JPanel topPanel;
        JButton enterSearchPanel;
        JLabel searchLabel;
        JTextField searchTextField;
        
        JPanel midPanel;
        JPanel midStoreListPanel;
        JPanel midItemNameListPanel;
        JPanel midPriceListPanel;
        JLabel storeLabel;
        JLabel itemNameLabel;
        JLabel priceLabel;
        
        SearchItemPage(){
            this.setLayout(new BorderLayout());
            /************************TOP PANEL**********************************/
            topPanel = new JPanel();
            topPanel.setBackground(Color.WHITE);
            enterSearchPanel = new JButton("Go! ");
            searchLabel = new JLabel(SEARCH_STR);
            searchTextField = new JTextField(30);
            topPanel.add(searchLabel);
            topPanel.add(searchTextField);
            topPanel.add(enterSearchPanel);
            this.add(topPanel, BorderLayout.NORTH);
            /************************MID PANEL**********************************/ 
            midPanel = new JPanel();
            midStoreListPanel = new JPanel();
            midItemNameListPanel = new JPanel();
            midPriceListPanel = new JPanel();
            //Title Labels
            storeLabel = new JLabel("Store              ");
            itemNameLabel = new JLabel("Item Name             ");
            priceLabel = new JLabel("Price");
            midStoreListPanel.add(storeLabel);
            midPriceListPanel.add(itemNameLabel);
            midPriceListPanel.add(priceLabel);
            
            //display the list of info here with a button of the group order
            
            midPanel.add(midStoreListPanel);
            midPanel.add(midPriceListPanel);
            midPanel.add(midPriceListPanel);
            this.add(midPanel, BorderLayout.CENTER);
        }
    }
//----------------------------------------------------------------------------
//inner class - PriceAnalysisPage
//----------------------------------------------------------------------------
    private class PriceAnalysisPage extends JPanel{
        JPanel topPanel;
        JButton enterSearchPanel;
        JLabel searchLabel;
        JTextField searchTextField;
        
        JPanel midPanel;
        JPanel graphPanel;
        
        JPanel bottomPanel;
        JLabel creditLabel;
        PriceAnalysisPage(){
            this.setLayout(new BorderLayout());
            /************************TOP PANEL**********************************/
            topPanel = new JPanel();
            topPanel.setBackground(Color.WHITE);
            enterSearchPanel = new JButton("Go! ");
            searchLabel = new JLabel(SEARCH_STR);
            searchTextField = new JTextField(30);
            topPanel.add(searchLabel);
            topPanel.add(searchTextField);
            topPanel.add(enterSearchPanel);
            this.add(topPanel, BorderLayout.NORTH);
            /************************MID PANEL**********************************/
            //add the graph here
            /************************BOTTOM PANEL**********************************/
            bottomPanel = new JPanel();
            creditLabel = new JLabel("Data from Statitics Canada");
            bottomPanel.add(creditLabel);
            this.add(bottomPanel, BorderLayout.SOUTH);
        }
    }
//----------------------------------------------------------------------------
//inner class - GroupOrderPage
//----------------------------------------------------------------------------
    private class GroupOrderPage extends JPanel{
        JPanel topPanel;
        JPanel rightPanel;
        JPanel labelPanel, textFieldPanel;
        JLabel[] requestLabelList;
        JTextField[] requestJTextFieldList;
        JButton createRequestButton;
        
        private String[] labels = {"Item: ", "Amount: ", "Location: ", "Time: "};
        GroupOrderPage(){
            this.setLayout(new BorderLayout());
            
            topPanel = new JPanel();
            rightPanel = new JPanel();
            labelPanel = new JPanel();
            textFieldPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            /**********************set up request label list ****************************/
            createRequestButton = new JButton("Create");
            int labelsLength = labels.length;
            requestLabelList = new JLabel[labelsLength];
            requestJTextFieldList = new JTextField[labelsLength];
            
            for (int l = 0; l < labelsLength; l++) {
                requestLabelList[l] = new JLabel(labels[l]);
                requestJTextFieldList[l] = new JTextField(10);
                rightPanel.add(requestLabelList[l]);
                rightPanel.add(requestJTextFieldList[l]);
            }
            rightPanel.add(createRequestButton);
            JPanel innerPanel = new JPanel(new FlowLayout());
            
            
            this.add(rightPanel, BorderLayout.EAST);
            innerPanel.add(new OrderPanel("user_1_abc", "testing item"));
            innerPanel.add(new OrderPanel("user2", "item___1"));
            innerPanel.add(new OrderPanel("user3", "item"));
            innerPanel.add(new OrderPanel("user4", "item1"));
            this.add(innerPanel);
        }
        private class OrderPanel extends JPanel{
            private String userID;
            private String itemInfo;
            
            final JLabel itemLabel = new JLabel("Item: ");
            final JLabel amountLabel = new JLabel("Amount Remaining: ");
            final JButton acceptButton = new JButton("Accept");
            JLabel userIdLabel,itemNameLabel, amountUnitLabel;
                
            OrderPanel(String id, String itemInfoStr){
                this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                this.setSize(100,50);
                this.userID = id;
                this.itemInfo = itemInfoStr;
                this.itemNameLabel = new JLabel(itemInfoStr);      //do some decoding on the itemInfoStr
                this.amountUnitLabel = new JLabel("unit?");
                /********************************/
                itemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(itemLabel);
                this.add(itemNameLabel);
                this.add(Box.createRigidArea(new Dimension(50,10)));
                /********************************/
                amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(amountLabel);
                /********************************/
                amountUnitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(amountUnitLabel);        //!!!!!!!!!!!!!!!!!!!!!!!!!!!
                this.add(Box.createRigidArea(new Dimension(50,20)));
                /********************************/
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(acceptButton);
                TitledBorder title;
                title = BorderFactory.createTitledBorder("ID: " + this.userID);
                title.setTitleJustification(TitledBorder.CENTER);
                
                this.setBorder(title);
            }
        }
    }
}
