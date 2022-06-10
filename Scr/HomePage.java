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

import java.util.ArrayList;


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

/**[HomePage.java]
  * This is final project - price match program
  * This class contains the main method to start the program
  * 
  * @author Kylie Wong and Michelle Chan, ICS4UE
  */
public class HomePage extends JFrame implements ActionListener{
    private User user;
    private static String[] actionStrings = {"Home", "Price Analysis", "Group Order", "View Account Details"};
    private SearchItemPage searchItemPanel;
    private PriceAnalysisPage priceAnalysisPanel;
    private GroupOrderPage groupOrderPanel;
    private AccountDetailPage acountDetailPanel;
    private AccountLoginPage accountLoginPanel;
    
    private final String HOME_PAGE_INDEX  = "0";
    private final String SEARCH_PAGE_INDEX = "1";
    private final String PRICE_ANALYSIS_INDEX = "2";
    private final String GROUP_ORDER_INDEX = "3";
    private final String ACCOUNT_DETAIL_INDEX = "4";
    private final String ACCOUNT_LOGIN_INDEX = "5";
    
    private final String SEARCH_STR = "Search";
    private final String PRICE_ANALYSIS_STR = "Price Analysis";
    private final String GROUP_ORDER_STR = "Group Order";
    private final String BACK_HOME_STR = "Back To Home";
    private final String ACCOUNT_DETAIL_STR = "View Account Details";
    
    JPanel homePagePanel;
    
    JPanel midPanel;
    JPanel searchPanel;
    JPanel midButtonPanel;
    JTextField searchTextField;
    JButton enterSearchPanel;
    
    JButton priceAnalysisButton;
    JButton groupOrderButton;
    
    JPanel mainBottomPanel;
    JButton accountSettingButton;
    JButton reviewGrpOrderButton;
    JButton bottomHomeButton;
    
    JPanel cardPanel;
    CardLayout cl;
    JComboBox actionList;
    //
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        HomePage frame = new HomePage(new User()); //delete - just for testing
    }
//----------------------------------------------------------------------------
    HomePage(User user){
        this.user = user;
        this.setTitle("Company Name");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(750, 500);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
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
        midPanel = new JPanel(new GridLayout(0,1));
        midPanel.setBackground(Color.WHITE);
        searchPanel = new JPanel();
        searchTextField = new JTextField(30);
        enterSearchPanel = new JButton(SEARCH_STR);
        enterSearchPanel.addActionListener(this);
        
        midButtonPanel = new JPanel();
        priceAnalysisButton = new JButton(PRICE_ANALYSIS_STR);
        priceAnalysisButton.addActionListener(this);
        groupOrderButton = new JButton(GROUP_ORDER_STR);
        groupOrderButton.addActionListener(this);
        searchPanel.add(searchTextField);
        searchPanel.add(enterSearchPanel);
        
        midPanel.add(searchPanel);
        midButtonPanel.add(priceAnalysisButton);
        midButtonPanel.add(groupOrderButton);
        midPanel.add(midButtonPanel);
        
        homePagePanel.add(midPanel, BorderLayout.CENTER);
        /************************BOTTOM PANEL**********************************/
        mainBottomPanel = new JPanel();
        bottomHomeButton = new JButton(BACK_HOME_STR);
        bottomHomeButton.addActionListener(this);
        accountSettingButton = new JButton(ACCOUNT_DETAIL_STR);
        accountSettingButton.addActionListener(this);
        
        mainBottomPanel.add(bottomHomeButton);
        mainBottomPanel.add(accountSettingButton);
        /*****************************ADD EVERYTHING INTO mainhomePagePanel************************************/
        cardPanel.add(homePagePanel, HOME_PAGE_INDEX);
        String[][] emptyList = {{"", "", ""}};
        searchItemPanel = new SearchItemPage(emptyList);
        cardPanel.add(searchItemPanel, SEARCH_PAGE_INDEX);
        priceAnalysisPanel = new PriceAnalysisPage();
        cardPanel.add(priceAnalysisPanel, PRICE_ANALYSIS_INDEX);
        groupOrderPanel = new GroupOrderPage();
        cardPanel.add(groupOrderPanel, GROUP_ORDER_INDEX);
        acountDetailPanel = new AccountDetailPage();
        cardPanel.add(acountDetailPanel, ACCOUNT_DETAIL_INDEX);
        accountLoginPanel = new AccountLoginPage();
        cardPanel.add(accountLoginPanel, ACCOUNT_LOGIN_INDEX);
        
        /*****************************************************************/
        cl.show(cardPanel, "0");
        this.add(actionList, BorderLayout.NORTH);
        this.add(cardPanel, BorderLayout.CENTER);
        this.add(mainBottomPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }
//------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent event){
        if(event.getSource() instanceof JComboBox){
            JComboBox cb = (JComboBox)event.getSource();
            int index = cb.getSelectedIndex();
            if(index>0){
                index = index+1;
            }
            String cardNo = Integer.toString(index);
            System.out.println("card no: " + cardNo);
            if(cardNo.equals(GROUP_ORDER_INDEX)){
                loginRequired(GROUP_ORDER_INDEX);
            }else if (cardNo.equals(ACCOUNT_DETAIL_INDEX)){
                loginRequired(ACCOUNT_DETAIL_INDEX);
            }else{
                cl.show(cardPanel, cardNo);
            }
        }else if (event.getSource() instanceof JButton){
            System.out.println("jbutton click!");
            String command = event.getActionCommand();
            if(command.equals(PRICE_ANALYSIS_STR)){
                changeCardPanel(PRICE_ANALYSIS_INDEX);
            }else if (command.equals(GROUP_ORDER_STR)){
                loginRequired(GROUP_ORDER_INDEX);
            }else if (command.equals(BACK_HOME_STR)){
                changeCardPanel(HOME_PAGE_INDEX);
            }else if (command.equals(ACCOUNT_DETAIL_STR)){
                loginRequired(ACCOUNT_DETAIL_INDEX);
            }else if(command.equals(SEARCH_STR)){
                changeCardPanel(SEARCH_PAGE_INDEX);
                String userInputText = searchTextField.getText();
                searchTextField.setText("");
                System.out.println("user text: " + userInputText);
                searchItemPanel.searchKeyWord(userInputText);
            }
        }
    }
//----------------------------------------------------------------------------
    private void loginRequired(String pageIndex){
        if(user.getUserID()!=null){     //if user is logged in
            changeCardPanel(pageIndex);
        }else{
            accountLoginPanel.setNextIndex(pageIndex);
            cl.show(cardPanel, ACCOUNT_LOGIN_INDEX);
            System.out.println("not yet login");
        }
    }
//----------------------------------------------------------------------------
    private void changeCardPanel(String index){
         cl.show(cardPanel, index);
         if(index.equals(HOME_PAGE_INDEX)){
             actionList.setSelectedIndex(Integer.parseInt(index));
         }else if (!index.equals(SEARCH_PAGE_INDEX)){
             actionList.setSelectedIndex(Integer.parseInt(index)-1);
         }
    }
//----------------------------------------------------------------------------
//inner class - SearchItemPage
//----------------------------------------------------------------------------
    private class SearchItemPage extends JPanel{
        private ArrayList<String> searchResultList = new ArrayList<String>();
        private final String[] columnNames = {"Store", "Item name", "Price"};
        
        JScrollPane scrollPane;
        JTable table;
        DefaultTableModel model;
        JTableHeader header;
        GridBagLayout gridBagLayout;
        GridBagConstraints gbc_scrollPane;
//----------------------------------------------------------------------------
        SearchItemPage(String[][] data){
            if(data.length>0){
                model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public Class getColumnClass(int column) {
                        return getValueAt(0, column).getClass();
                    }
                };

            gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
            gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
            gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.5, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
            this.setLayout(gridBagLayout);
            
            gbc_scrollPane = new GridBagConstraints();
            gbc_scrollPane.gridheight = 2;
            
            table = new JTable(model);
            table.setBorder(new LineBorder(new Color(0, 0, 0)));
            header = table.getTableHeader();
            
            header.setBackground(Color.GREEN);
            header.setForeground(Color.BLACK);
            
            scrollPane = new JScrollPane(table);
            gbc_scrollPane.gridwidth = 4;
            gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
            gbc_scrollPane.fill = GridBagConstraints.BOTH;
            gbc_scrollPane.gridx = 2;
            gbc_scrollPane.gridy = 4;
            
            this.add(scrollPane, gbc_scrollPane);
            }
        }
//---------------------------------------------------------------------------- 
        private void update(){
            String[][] temp = {{"a", "b", "c"}, {"x", "zs", "sdfasd"}};
            model = new DefaultTableModel(temp, columnNames) {
                @Override
                public Class getColumnClass(int column) {
                    return getValueAt(0, column).getClass();
                }
            };
            this.revalidate();
            this.repaint();
        }
//---------------------------------------------------------------------------- 
        private void searchKeyWord(String string){
            
            System.out.println("searching for item: " + string);
            searchResultList = user.getKeywordList(string);

            for(int i=0; i<searchResultList.size(); i++){
                String str = searchResultList.get(i);
                
                String store = str.substring(0, str.indexOf(" - "));
                String itemName = str.substring(str.indexOf(" - ")+3, str.indexOf(" = ")); // user Const.xxx
                String price = str.substring(str.indexOf(" = ")+3);
                
//                storeLabelList.add(new JLabel(store));
//                itemNameList.add(new JLabel(itemName));
//                priceLabelList.add(new JLabel(price));
            }
            System.out.println("arraylist of result:\n" + searchResultList.toString());
            update();
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
//----------------------------------------------------------------------------
//inner class - AccountDetailPage
//----------------------------------------------------------------------------
    private class AccountDetailPage extends JPanel{
        AccountDetailPage(){
        
        }
    }
//----------------------------------------------------------------------------
//inner class - AccountLoginPage
//----------------------------------------------------------------------------
    private class AccountLoginPage extends JPanel implements ActionListener{
        private String nextPageIndex;
        JPanel loginPanel;
        JLabel userLabel;
        JLabel passwordLabel;
        JLabel messageLabel;
        JTextField userInput;
        JTextField passwordInput;
        JButton loginEnter;
        
        AccountLoginPage(){
            loginPanel = new JPanel();
            userLabel = new JLabel("User ID: ");
            passwordLabel = new JLabel("Password: ");
            loginEnter = new JButton("Enter");
            messageLabel = new JLabel("");
            
            loginPanel.setLayout(new FlowLayout());
            userInput = new JTextField(20);
            loginPanel.add(userLabel);
            loginPanel.add(userInput);
            passwordInput = new JTextField(20);
            loginPanel.add(passwordLabel);
            loginPanel.add(passwordInput);
            
            this.add(loginPanel);
            this.add(loginEnter);
            this.add(messageLabel);
            
            loginEnter.addActionListener(this);
        }
        public void setNextIndex(String index){
            this.nextPageIndex = index;
        }
        @Override
        public void actionPerformed(ActionEvent event){
            String userText = userInput.getText();
            String passwordText = passwordInput.getText();
            //catch unacceptable character in here; edit messagelabel's text
            
            boolean loginStatus = user.userLogin(userText, passwordText);
            if(loginStatus){
                changeCardPanel(this.nextPageIndex);
            }else{    //wrong userId / password
                messageLabel.setText("Wrong user ID / password. Please try again.");
            }
            userInput.setText("");
            passwordInput.setText("");
        }
    }
}
