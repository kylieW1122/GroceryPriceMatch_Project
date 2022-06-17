import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.ComponentOrientation;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.border.TitledBorder;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.AbstractButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**[HomePage.java]
  * This is final project - Grocery Helper Program
  * This class is the main GUI of the program
  * 
  * @author Kylie Wong, ICS4UE
  * @version 2.0, build June 16, 2022
  */
public class HomePage extends JFrame implements ActionListener{
    private User user;
    private static String[] actionStrings = {"Home", "Price Analysis", "Group Order", "View Account Details"};
    private SearchItemPage searchItemPanel;
    private PriceAnalysisPanel priceAnalysisPanel;
    private GroupOrderPage groupOrderPanel;
    private AccountDetailPage acountDetailPanel;
    private AccountLoginPage accountLoginPanel;
    private AccessDeclinedPage accessDeclinedPanel;
    
    private final String HOME_PAGE_INDEX  = "0";
    private final String SEARCH_PAGE_INDEX = "1";
    private final String PRICE_ANALYSIS_INDEX = "2";
    private final String GROUP_ORDER_INDEX = "3";
    private final String ACCOUNT_DETAIL_INDEX = "4";
    private final String ACCOUNT_LOGIN_INDEX = "5";
    private final String ACCESS_DECLINED_INDEX = "6";
    
    private final String SEARCH_STR = "Search";
    private final String PRICE_ANALYSIS_STR = "Price Analysis";
    private final String GROUP_ORDER_STR = "Group Order";
    private final String BACK_HOME_STR = "Back To Home";
    private final String ACCOUNT_DETAIL_STR = "View Account Details";
    
    private final int DEFAULT_FRAME_SIZE_WIDTH = 1000;
    private final int DEFAULT_FRAME_SIZE_HEIGHT = 700;
    private final Font TITLE_FONT = new Font("titleFont", Font.PLAIN, 18);
    
    private final static String ANALYSIS_ICON_FILE = "resources/icons/graphIcon.png";
    private final static String GROUP_ICON_FILE = "resources/icons/groupIcon.png";
    
    JPanel homePagePanel;
    
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
    //----------------------------------------------------------------------------
    HomePage(User user){
        this.user = user;
        this.setTitle("Grocery Price Comparison");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(DEFAULT_FRAME_SIZE_WIDTH, DEFAULT_FRAME_SIZE_HEIGHT);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        /********************set cardPanel**********************/
        actionList = new JComboBox(actionStrings);
        actionList.setSelectedIndex(0);
        actionList.addActionListener(this);
        cl = new CardLayout();
        cardPanel = new JPanel();
        cardPanel.setLayout(cl);
        /********************set homePagePanel, main panel of homepage**********************/
        homePagePanel = new JPanel();
        homePagePanel.setLayout(new BorderLayout());
        /****search panel******/
        searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(DEFAULT_FRAME_SIZE_WIDTH, 100));
        searchTextField = new JTextField(30);
        enterSearchPanel = new JButton(SEARCH_STR);
        enterSearchPanel.addActionListener(this);
        searchPanel.add(searchTextField);
        searchPanel.add(enterSearchPanel);
        homePagePanel.add(searchPanel, BorderLayout.NORTH);
        /****button panel******/
        Dimension buttonDimension = new Dimension(380, 380);
        midButtonPanel = new JPanel();
        priceAnalysisButton = new JButton(PRICE_ANALYSIS_STR, new ImageIcon(ANALYSIS_ICON_FILE));  
        priceAnalysisButton.setFont(TITLE_FONT);
        priceAnalysisButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        priceAnalysisButton.setHorizontalTextPosition(AbstractButton.CENTER);
        priceAnalysisButton.setPreferredSize(buttonDimension);
        priceAnalysisButton.setVerticalAlignment(JButton.CENTER);
        priceAnalysisButton.addActionListener(this);
        
        groupOrderButton = new JButton(GROUP_ORDER_STR, new ImageIcon(GROUP_ICON_FILE));
        groupOrderButton.setFont(TITLE_FONT);
        groupOrderButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        groupOrderButton.setHorizontalTextPosition(AbstractButton.CENTER);
        groupOrderButton.setPreferredSize(buttonDimension);
        groupOrderButton.setVerticalAlignment(JButton.CENTER);
        groupOrderButton.addActionListener(this);
       
        midButtonPanel.add(priceAnalysisButton);
        midButtonPanel.add(groupOrderButton);
        homePagePanel.add(midButtonPanel, BorderLayout.CENTER);
        /*******BOTTOM PANEL of homePagePanel********/
        mainBottomPanel = new JPanel();
        bottomHomeButton = new JButton(BACK_HOME_STR);
        bottomHomeButton.addActionListener(this);
        accountSettingButton = new JButton(ACCOUNT_DETAIL_STR);
        accountSettingButton.addActionListener(this);
        
        mainBottomPanel.add(bottomHomeButton);
        mainBottomPanel.add(accountSettingButton);
        /*****************************ADD ALL PANELS INTO cardPanel************************************/
        cardPanel.add(homePagePanel, HOME_PAGE_INDEX);
        searchItemPanel = new SearchItemPage();    //inner class in HomePage
        cardPanel.add(searchItemPanel, SEARCH_PAGE_INDEX);
        priceAnalysisPanel = new PriceAnalysisPanel();
        cardPanel.add(priceAnalysisPanel, PRICE_ANALYSIS_INDEX);
        accessDeclinedPanel = new AccessDeclinedPage();    //inner class in HomePage
        cardPanel.add(accessDeclinedPanel, ACCESS_DECLINED_INDEX);
        if(user.getHostStatus()){  //host is active. If not, only have access to search item and price analysis graph
            groupOrderPanel = new GroupOrderPage();    //inner class in HomePage
            cardPanel.add(groupOrderPanel, GROUP_ORDER_INDEX);
            acountDetailPanel = new AccountDetailPage();    //inner class in HomePage
            cardPanel.add(acountDetailPanel, ACCOUNT_DETAIL_INDEX);
            accountLoginPanel = new AccountLoginPage();    //inner class in HomePage
            cardPanel.add(accountLoginPanel, ACCOUNT_LOGIN_INDEX);
        }
        /*****************************************************************/
        cl.show(cardPanel, "0"); //cardLayout
        this.add(actionList, BorderLayout.NORTH);
        this.add(cardPanel, BorderLayout.CENTER);
        this.add(mainBottomPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }
    //----------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent event){
        if(event.getSource() instanceof JComboBox){
            JComboBox cb = (JComboBox)event.getSource();
            int index = cb.getSelectedIndex();
            if(index>0){
                index = index+1;
            }
            String cardNo = Integer.toString(index);
            changeCardPanel(cardNo);
        }else if (event.getSource() instanceof JButton){
            String command = event.getActionCommand();
            if(command.equals(PRICE_ANALYSIS_STR)){
                changeCardPanel(PRICE_ANALYSIS_INDEX);
            }else if (command.equals(GROUP_ORDER_STR)){
                changeCardPanel(GROUP_ORDER_INDEX);
            }else if (command.equals(BACK_HOME_STR)){
                changeCardPanel(HOME_PAGE_INDEX);
            }else if (command.equals(ACCOUNT_DETAIL_STR)){
                changeCardPanel(ACCOUNT_DETAIL_INDEX);
            }else if(command.equals(SEARCH_STR)){
                changeCardPanel(SEARCH_PAGE_INDEX);
                String userInputText = searchTextField.getText();
                searchTextField.setText("");
                searchItemPanel.searchKeyWord(userInputText);
            }
        }
    }
    //----------------------------------------------------------------------------
    private void changeCardPanel(String index){
        if(!user.getHostStatus()){  //host is not active, block all access except search item and price analysis graph
            if( (index.equals(GROUP_ORDER_INDEX)) || (index.equals(ACCOUNT_DETAIL_INDEX)) || (index.equals(ACCOUNT_LOGIN_INDEX)) ){
                cl.show(cardPanel, ACCESS_DECLINED_INDEX);
            }else{
                cl.show(cardPanel, index);
            }
        }else{
            if( (index.equals(GROUP_ORDER_INDEX)) || (index.equals(ACCOUNT_DETAIL_INDEX)) ){
                if(!user.getUserID().equals("")){     //if user is logged in
                    cl.show(cardPanel, index);
                }else{
                    accountLoginPanel.setNextIndex(index);
                    cl.show(cardPanel, ACCOUNT_LOGIN_INDEX);
                }
            }else{
                cl.show(cardPanel, index);
                /****update the panel if needed and actionLsit index****/
                if(index.equals(ACCOUNT_DETAIL_INDEX)){
                    acountDetailPanel.updateDetailPanel();
                }else if(index.equals(GROUP_ORDER_INDEX)){
                    groupOrderPanel.updateOrderPanel();
                }
            }
        }
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
        SearchItemPage(){
            String[][] emptyList = {{"", "", ""}};
            setUpSearchPanel(emptyList);
        }
        //----------------------------------------------------------------------------
        private void setUpSearchPanel(String[][] data){
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
        private void update(String[][] data){ //update jpanel with the search result
            this.removeAll();
            this.setUpSearchPanel(data);
            this.revalidate();
            this.repaint();
        }
        //----------------------------------------------------------------------------
        private void searchKeyWord(String searchText){
            searchResultList = DataBase.searchItemKeyword(searchText); //user.getSearchResultList(searchText);
            String [][] data = {{"","",""}}; 
            int numOfItems = searchResultList.size();
            if(numOfItems>0){
                data = new String[numOfItems][3];
                
                int mapCount = 0;
                for (String itemInfoStr: searchResultList){
                    data[mapCount][0] = itemInfoStr.substring(0, itemInfoStr.indexOf(" - "));
                    data[mapCount][1] = itemInfoStr.substring(itemInfoStr.indexOf(" - ") + 3, itemInfoStr.indexOf(" = "));
                    data[mapCount][2] = itemInfoStr.substring(itemInfoStr.indexOf(" = ") +3);
                    mapCount ++;
                    
                }
            }
            update(data);
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
            /********TOP PANEL*********/
            topPanel = new JPanel();
            topPanel.setBackground(Color.WHITE);
            enterSearchPanel = new JButton("Go! ");
            searchLabel = new JLabel(SEARCH_STR);
            searchTextField = new JTextField(30);
            topPanel.add(searchLabel);
            topPanel.add(searchTextField);
            topPanel.add(enterSearchPanel);
            this.add(topPanel, BorderLayout.NORTH);
            /******MID PANEL*******/
            //add the graph here
            /******BOTTOM PANEL*******/
            bottomPanel = new JPanel();
            creditLabel = new JLabel("Data from Statitics Canada");
            bottomPanel.add(creditLabel);
            this.add(bottomPanel, BorderLayout.SOUTH);
        }
    }
//----------------------------------------------------------------------------
//inner class - GroupOrderPage
//----------------------------------------------------------------------------
    private class GroupOrderPage extends JPanel implements ActionListener{
        JPanel topPanel;
        JPanel rightPanel;
        JPanel rightDownPanel;
        
        JScrollPane orderStream_scrollPane;
        JTextField itemNameField;
        JButton createRequestButton;
        
        JComboBox itemComboBox;
        JLabel itemLabel, amountLabel, priceLabel, locationLabel, messageLabel;
        JComboBox amountComboBox;
        
        JLabel dateLabel, timeLabel;
        JPanel orderDisplayPanel;
        JButton refreshButton;
        
        private ArrayList<String> list;
        
        final String PRICE_DEFAULT_STR = "Price: $ ";
        final String LOCATION_DEFAULT_STR = "Store Location: ";
        final int ORDER_PANEL_WIDTH = 230;
        final int ORDER_PANEL_HEIGHT = 190;
        final int SCROLL_PANE_WIDTH = 300;
        
        private Double amountPercentage = -1.0;
        private String itemInfo = "";
        
        private ArrayList<OrderPanel> orderPanelList;
        private HashMap<String, ArrayList<String>> groupOrderMap;
        //----------------------------------------------------------------------------
        private String[] amountList_cb = {"", "10 %", "20 %", "30 %", "40 %", "50 %", "60 %", "70 %", "80 %", "90 %"};
        GroupOrderPage(){
            this.setLayout(new BorderLayout());
            topPanel = new JPanel();
            rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setMaximumSize(new Dimension(400, 0));
            rightPanel.setPreferredSize(new Dimension(300, 0));
            rightPanel.setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT );
            /**********************set up create group order panel****************************/
            /***********ITEM************/
            itemLabel = new JLabel("Item: ");
            itemLabel.setAlignmentX(JLabel.LEFT);
            itemNameField = new JTextField(20);
            
            list = user.getWholeItemArrayList();
            String[] cb_itemList =  new String[list.size()+1];
            cb_itemList[0] = "";
            for(int st=0; st<list.size(); st++){
                String itemString =  list.get(st);
                itemString = itemString.substring(0, itemString.indexOf(" = "));
                cb_itemList[st+1] = itemString;
            }
            itemComboBox = new AutoCompleteComboBox(cb_itemList);
            itemComboBox.setMaximumSize(new Dimension(200, 20));
            itemComboBox.addActionListener(this);
            
            rightPanel.add(itemLabel);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            rightPanel.add(itemComboBox);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            /***********AMOUNT************/
            amountLabel = new JLabel("Amount(%): ");
            amountLabel.setAlignmentX(JLabel.LEFT);
            amountComboBox = new JComboBox(amountList_cb);
            amountComboBox.setMaximumSize(new Dimension(200, 20));
            amountComboBox.setSelectedIndex(0);
            amountComboBox.addActionListener(this);
            
            rightPanel.add(amountLabel);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            rightPanel.add(amountComboBox);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            /***********PRICE************/
            priceLabel = new JLabel(PRICE_DEFAULT_STR);
            priceLabel.setAlignmentX(JLabel.LEFT);
            rightPanel.add(priceLabel);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            /***********LOCATION AND TIME************/
            locationLabel = new JLabel(LOCATION_DEFAULT_STR);
            locationLabel.setAlignmentX(JLabel.LEFT);
            rightPanel.add(locationLabel);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            
            createRequestButton = new JButton("Create");
            createRequestButton.setAlignmentX(JButton.LEFT);
            createRequestButton.addActionListener(this);
            messageLabel = new JLabel();
            messageLabel.setAlignmentX(JLabel.LEFT);
            
            rightPanel.add(createRequestButton);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            rightPanel.add(messageLabel);
            /**********************Create group order stream******************************/
            orderDisplayPanel = new JPanel();
            this.updateOrderPanel();
            orderStream_scrollPane = new JScrollPane(orderDisplayPanel, 
                                                     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
            orderStream_scrollPane.setMaximumSize(new Dimension(SCROLL_PANE_WIDTH,0));
            
            refreshButton = new JButton("Refresh");
            refreshButton.addActionListener(this);
            
            this.add(refreshButton, BorderLayout.SOUTH);
            this.add(rightPanel, BorderLayout.EAST);
            this.add(orderStream_scrollPane, BorderLayout.CENTER);
        }
        //----------------------------------------------------------------------------
        private void updateOrderPanel(){
            this.orderPanelList = setUpGroupOrderBoxes();
            orderDisplayPanel.removeAll();
            
            orderDisplayPanel.setLayout(new GridLayout(0, 3));
            for(OrderPanel pane: this.orderPanelList){
                orderDisplayPanel.add(pane);
            }
            this.revalidate();
            this.repaint();
        }
        //----------------------------------------------------------------------------
        private ArrayList<OrderPanel> setUpGroupOrderBoxes(){
            this.groupOrderMap = new HashMap<String, ArrayList<String>>();
            this.groupOrderMap = user.refreshGroupOrder();
            ArrayList<OrderPanel> resultList = new ArrayList<OrderPanel>();
            resultList.clear();
            /*****loop throught the group order list, create OrderPanel****/
            for(String refNo : groupOrderMap.keySet()){
                ArrayList<String> orderInfo = groupOrderMap.get(refNo);
                resultList.add(new OrderPanel(refNo, orderInfo));
            }
            return resultList;
        }
        //----------------------------------------------------------------------------
        @Override
        public void actionPerformed(ActionEvent event){
            int amountIndex = amountComboBox.getSelectedIndex();
            int selectedIndex = itemComboBox.getSelectedIndex();
            String amountStr = amountList_cb[amountIndex];
            if(event.getSource() instanceof JComboBox){
                JComboBox cb = (JComboBox)event.getSource();
                
                if( (!amountStr.equals("")) && (selectedIndex>0) ){
                    if(list!=null){
                        this.itemInfo = list.get(selectedIndex-1);
                        String totalPriceString = itemInfo.substring(itemInfo.indexOf("$")+1 , itemInfo.indexOf(" @ "));
                        String locationString = itemInfo.substring(itemInfo.indexOf(" @ ")+3);
                        amountStr = amountStr.substring(0, 2);
                        
                        this.amountPercentage = 0.0;
                        Double totalPrice = 0.0;
                        try{
                            this.amountPercentage = Double.parseDouble(amountStr);
                            this.amountPercentage = amountPercentage/100.00;
                            totalPrice = Double.parseDouble(totalPriceString);
                        }catch (NumberFormatException numberEx){ //if the data format is not a double
                            this.amountPercentage = -1.0; 
                            totalPrice = -1.0;
                        }
                        Double finalPrice = totalPrice*amountPercentage;
                        finalPrice = Math.round(finalPrice*100.0) /100.0;
                        /**********update panel***************/
                        priceLabel.setText(PRICE_DEFAULT_STR + finalPrice);
                        locationLabel.setText(LOCATION_DEFAULT_STR + locationString);
                    }
                }           
                
            }
            if (event.getSource() instanceof JButton){
                JButton jButton = (JButton)event.getSource();
                if(jButton.equals(createRequestButton)){
                    boolean createStatus = false;
                    if ( (selectedIndex>0) && (amountPercentage>0.0) && (!itemInfo.equals("")) ){
                        createStatus = user.createGroupOrder(itemInfo, amountPercentage);
                        this.amountPercentage = -1.0;   //reset 
                        this.itemInfo = "";
                    }
                    if(createStatus){
                        this.resetRequestPanel();
                        this.setUpGroupOrderBoxes();
                        messageLabel.setText("Order created. ");
                    }else{
                        messageLabel.setText("Missing information. ");
                    }
                }else if (jButton.equals(refreshButton)){
                    this.updateOrderPanel();
                } 
            }
            messageLabel.setAlignmentX(JLabel.CENTER);
            this.revalidate();
            this.repaint();
        }
        //----------------------------------------------------------------------------
        private void resetRequestPanel(){
            priceLabel.setText(PRICE_DEFAULT_STR);
            locationLabel.setText(LOCATION_DEFAULT_STR);
            amountComboBox.setSelectedIndex(0);
            AutoCompleteComboBox cb = (AutoCompleteComboBox) itemComboBox;
            cb.resetField();
        }
        //----------------------------------------------------------------------------
        //inner class inside GroupOrderPage - OrderPanel
        //----------------------------------------------------------------------------
        private class OrderPanel extends JPanel implements ActionListener{ 
            private String refNo;
            private String itemInfo;
            private String itemPrice;
            private String location;
            private String memberStr;
            private String amountStr;
            private Double amountPercentage;
            
            final JButton acceptButton = new JButton("Accept");
            final String MEMBER_LABEL_STR = "Group member: ";
            final String ITEM_LABEL_STR = "Item: ";
            final String AMOUNT_LABEL_STR = "Amount Remaining: ";
            final String PRICE_LABEL_STR = "Price: ";
            final String LOCATION_STR = "Store Location: ";
            JLabel memberLabel, itemLabel, amountLabel, priceLabel, locationLabel;
            //----------------------------------------------------------------------------
            OrderPanel(String refNo, ArrayList<String> orderInfoList){
                //example format: 1438865252, {Red Bell Peppers 5 kg, $21.29, CostCo, userID~0.4}
                this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                this.setPreferredSize(new Dimension(ORDER_PANEL_WIDTH,ORDER_PANEL_HEIGHT));
                this.setMaximumSize(new Dimension(ORDER_PANEL_WIDTH,ORDER_PANEL_HEIGHT));
                this.refNo = refNo; 
                if(orderInfoList.size()>=3){
                    this.itemInfo = orderInfoList.get(0);
                    this.itemPrice = orderInfoList.get(1);
                    this.location = orderInfoList.get(2);
                }
                Double price_double = Double.parseDouble(itemPrice.substring(1));
                Double percentageCommited = 0.0;
                
                this.memberStr = "";
                for(int info=3; info<orderInfoList.size(); info++){
                    String temp = orderInfoList.get(info);
                    if(this.memberStr.equals("")){
                        this.memberStr = temp.substring(0, temp.indexOf(Const.SPLIT));
                    }else{
                        this.memberStr = temp.substring(0, temp.indexOf(Const.SPLIT)) + ", " + this.memberStr;
                    }
                    temp = temp.substring(temp.indexOf(Const.SPLIT)+1);
                    Double tempPercentage = Double.parseDouble(temp);
                    percentageCommited = percentageCommited + tempPercentage;
                }
                /***calculate the price and % remaning for the order***/
                Double percent_remaining = 1.0-percentageCommited;
                price_double = price_double * percent_remaining;
                price_double = Math.round(price_double*100.0)/100.0;
                amountPercentage = percent_remaining;
                percent_remaining = percent_remaining*100.0;
                this.itemPrice = Double.toString(price_double);
                this.amountStr = Double.toString(percent_remaining);
                /*************add all them into this jpanel*****************/
                this.add(Box.createRigidArea(new Dimension(50,10)));
                memberLabel = new JLabel(MEMBER_LABEL_STR + this.memberStr);
                memberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(memberLabel);
                this.add(Box.createRigidArea(new Dimension(50,10)));
                
                itemLabel = new JLabel(ITEM_LABEL_STR + this.itemInfo);
                itemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(itemLabel);
                this.add(Box.createRigidArea(new Dimension(50,10)));
                
                amountLabel = new JLabel(AMOUNT_LABEL_STR + this.amountStr + " %");
                amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(amountLabel);
                this.add(Box.createRigidArea(new Dimension(50,10)));
                
                priceLabel = new JLabel(PRICE_LABEL_STR + this.itemPrice);
                priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(priceLabel);
                this.add(Box.createRigidArea(new Dimension(50,10)));
                
                locationLabel = new JLabel(LOCATION_STR + this.location);
                locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                this.add(locationLabel);
                this.add(Box.createRigidArea(new Dimension(50,10)));
                
                acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                acceptButton.addActionListener(this);
                
                this.add(acceptButton);
                TitledBorder title;
                title = BorderFactory.createTitledBorder("Ref. # " + this.refNo);
                title.setTitleJustification(TitledBorder.CENTER);
                
                this.setBorder(title);
            }
            //----------------------------------------------------------------------------
            @Override
            public void actionPerformed(ActionEvent event){
                if (event.getSource() instanceof JButton){
                    JButton jButton = (JButton)event.getSource();
                    user.acceptGroupOrder(this.refNo, amountPercentage);
                    updateOrderPanel();
                }
            }
        }
    }
//----------------------------------------------------------------------------
//inner class - AccountDetailPage
//----------------------------------------------------------------------------
    private class AccountDetailPage extends JPanel implements ActionListener{
        private String userID;
        private ArrayList<HashMap> orderCombinedList;
        private final String[] pendingOrderTitles = {"Reference No.", "Item Info", "Location", "Price"};
        private final String[] completeOrderTitles = {"Reference No.", "Item Info", "Location", "Price", "Group member"};
        
        JPanel pendingGrpOrder_Panel, comfirmedGrpOrder_Panel, resetPassword_Panel;
        JLabel welcomeLabel;
        
        JPanel pendingOrder_Panel, completeOrder_Panel;
        JScrollPane pendingOrder_scrollPane, completeOrder_scrollPane;
        JTable pendingTable, completeTable;
        JButton refreshButton;
        JLabel pendingTitle_label, completeOrderTitle_label;
        
        JPanel oldPassword_Panel, newPassword_Panel;
        JLabel oldPassword_label, newPassword_label;
        JTextField oldPassword_tf, newPassword_tf;
        JButton resetPassword_button;
        JLabel password_msg_label;
        
        AccountDetailPage(){
            this.userID = user.getUserID();
            this.setLayout(new BorderLayout());
            welcomeLabel = new JLabel("Hi, " + this.userID + "!");
            welcomeLabel.setHorizontalAlignment(JButton.CENTER);
            welcomeLabel.setFont(TITLE_FONT);
            
            /***********Reset Password Panel************/
            resetPassword_Panel = new JPanel();
            resetPassword_Panel.setLayout(new BoxLayout(resetPassword_Panel, BoxLayout.Y_AXIS));
            resetPassword_Panel.setMaximumSize(new Dimension(DEFAULT_FRAME_SIZE_WIDTH, 100));
            oldPassword_Panel= new JPanel();             //set up old password area
            oldPassword_label = new JLabel("Old Password: ");
            oldPassword_tf = new JTextField(20);
            oldPassword_Panel.add(oldPassword_label);
            oldPassword_Panel.add(oldPassword_tf);
            newPassword_Panel = new JPanel();             //set up new password area
            newPassword_label = new JLabel("New Password: ");
            newPassword_tf = new JTextField(20);
            newPassword_Panel.add(newPassword_label);
            newPassword_Panel.add(newPassword_tf);
            
            resetPassword_button = new JButton("Change");
            password_msg_label = new JLabel("");
            resetPassword_button.addActionListener(this);
            resetPassword_button.setAlignmentX(Component.CENTER_ALIGNMENT);
            password_msg_label.setAlignmentX(Component.CENTER_ALIGNMENT);
            resetPassword_Panel.add(oldPassword_Panel);    //add them into reset password area
            resetPassword_Panel.add(newPassword_Panel);
            resetPassword_Panel.add(resetPassword_button);
            resetPassword_Panel.add(password_msg_label);
            
            /*************Create center panel for group order tables*********************/
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            pendingOrder_Panel = new JPanel();
            completeOrder_Panel = new JPanel();
            pendingTitle_label = new JLabel("Pending Order"); 
            pendingTitle_label.setAlignmentX(Component.CENTER_ALIGNMENT);
            completeOrderTitle_label = new JLabel("Completed Order");
            completeOrderTitle_label.setAlignmentX(Component.CENTER_ALIGNMENT);
            refreshButton = new JButton("Refresh");
            refreshButton.addActionListener(this);
            refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            centerPanel.add(Box.createRigidArea(new Dimension(100, 20)));
            centerPanel.add(pendingTitle_label);
            centerPanel.add(pendingOrder_Panel);
            centerPanel.add(completeOrderTitle_label);
            centerPanel.add(completeOrder_Panel);
            centerPanel.add(Box.createRigidArea(new Dimension(100, 10)));
            centerPanel.add(refreshButton);
            centerPanel.add(Box.createRigidArea(new Dimension(100, 20)));
            
            /******************add everything into this jpanel - boxlayout***********************/
            this.add(welcomeLabel, BorderLayout.NORTH);
            this.add(centerPanel, BorderLayout.CENTER);
            this.add(resetPassword_Panel, BorderLayout.SOUTH);
        }
        //----------------------------------------------------------------------------
        private void setUpGroupOrderListTables(){
            HashMap<String, ArrayList<String>> pendingList = orderCombinedList.get(0);
            HashMap<String, ArrayList<String>> completeList = orderCombinedList.get(1);
            ArrayList<String> orderInfo;
            String[][] pendingData = new String[pendingList.size()][pendingOrderTitles.length];
            String[][] completeData = new String[completeList.size()][completeOrderTitles.length];
            int row_index=0;
            for(String pen_refNo: pendingList.keySet()){
                orderInfo = pendingList.get(pen_refNo);
                //orderinfo format: [Organic Peeled Carrots, $11.19, CostCo, mike~0.4]
                String price = orderInfo.get(1);
                String percentage = orderInfo.get(3);
                price = price.substring(1);
                percentage = percentage.substring(percentage.indexOf(Const.SPLIT)+1);
                Double perc_double = Double.parseDouble(percentage);
                Double price_double = Double.parseDouble(price);
                Double finalPrice = Math.round((price_double*perc_double)*100.0)/100.0;
                //title format: {"Reference No.", "Item Info", "Location", "Price"}
                pendingData[row_index][0] = pen_refNo;
                pendingData[row_index][1] = orderInfo.get(0);
                pendingData[row_index][2] = orderInfo.get(2);
                pendingData[row_index][3] = Double.toString(finalPrice);
                row_index++;
            }
            row_index=0;
            for(String com_refNo: completeList.keySet()){
                orderInfo = completeList.get(com_refNo);
                //orderinfo format: [Asparagus 1 kg, $9.09, CostCo, A~0.1, mike~0.9]
                String price = orderInfo.get(1);
                price = price.substring(1);
                String grp_member_str = "";
                Double finalPrice = 0.0;
                for(int grp_mem_idx = 3; grp_mem_idx< orderInfo.size(); grp_mem_idx++){
                    String temp = orderInfo.get(grp_mem_idx);
                    String mem_userID = temp.substring(0, temp.indexOf(Const.SPLIT));
                    if( mem_userID.equals(user.getUserID()) ){
                        String percentage = temp.substring(temp.indexOf(Const.SPLIT)+1);
                        Double perc_double = Double.parseDouble(percentage);
                        Double price_double = Double.parseDouble(price);
                        finalPrice = Math.round((price_double*perc_double)*100.0)/100.0;
                    }else{
                        if(grp_member_str.equals("")){
                            grp_member_str = mem_userID;
                        }else{
                            grp_member_str = mem_userID + "," + grp_member_str;
                        }
                    }
                }
                //title format: {"Reference No.", "Item Info", "Location", "Price", "Group member"};
                completeData[row_index][0] = com_refNo;
                completeData[row_index][1] = orderInfo.get(0);
                completeData[row_index][2] = orderInfo.get(2);
                completeData[row_index][3] = Double.toString(finalPrice);
                completeData[row_index][4] = grp_member_str;
                row_index++;
            }
            pendingOrder_Panel.removeAll();
            completeOrder_Panel.removeAll();
            
            pendingTable = new JTable(pendingData, pendingOrderTitles);
            pendingOrder_scrollPane = new JScrollPane(pendingTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            pendingOrder_scrollPane.setMinimumSize(new Dimension(800, 180));
            pendingOrder_scrollPane.setPreferredSize(new Dimension(800, 180));
            pendingOrder_Panel.add(pendingOrder_scrollPane);
            
            completeTable = new JTable(completeData, completeOrderTitles);
            completeOrder_scrollPane = new JScrollPane(completeTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                       JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            completeOrder_scrollPane.setMinimumSize(new Dimension(800, 180));
            completeOrder_scrollPane.setPreferredSize(new Dimension(800, 180));
            completeOrder_Panel.add(completeOrder_scrollPane);
        }
        //----------------------------------------------------------------------------
        private void updateDetailPanel(){
            this.userID = user.getUserID();
            this.orderCombinedList = user.getGroupOrderList_user();
            setUpGroupOrderListTables();
            welcomeLabel.setText("Hi, " + this.userID + "!");
            
            this.revalidate();
            this.repaint();
        }
        //----------------------------------------------------------------------------
        @Override
        public void actionPerformed(ActionEvent event){
            if (event.getSource() instanceof JButton){
                JButton jButton = (JButton)event.getSource();
                if(jButton.equals(resetPassword_button)){    
                    String oldPassword = oldPassword_tf.getText();
                    String newPassword = newPassword_tf.getText();
                    boolean status = user.resetPassword(this.userID, oldPassword, newPassword);
                    if(status){
                        password_msg_label.setText("Password updated");
                    }else{
                        password_msg_label.setText("Wrong old password. Please try again. ");
                    }
                    oldPassword_tf.setText("");
                    newPassword_tf.setText("");
                }else if (jButton.equals(refreshButton)){
                    this.updateDetailPanel();
                }
            }
            
            this.revalidate();
            this.repaint();
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
        
        JPanel registerPanel;
        JLabel userLabel_register;
        JLabel passwordLabel_register;
        JTextField userInput_register;
        JTextField passwordInput_register;
        JButton registerEnter;
        
        JLabel loginLabel;
        JLabel registerLabel;
        
        AccountLoginPage(){
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            loginLabel = new JLabel("Login");
            loginLabel.setFont(TITLE_FONT);
            loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerLabel = new JLabel("Register");
            registerLabel.setFont(TITLE_FONT);
            registerLabel.setVisible(false);
            registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            /**********login panel***********/
            loginPanel = new JPanel();
            loginPanel.setMaximumSize(new Dimension(DEFAULT_FRAME_SIZE_WIDTH, 100));
            userLabel = new JLabel("User ID: ");
            passwordLabel = new JLabel("Password: ");
            loginEnter = new JButton("  Login ");
            messageLabel = new JLabel("");
            loginEnter.addActionListener(this);
            
            loginPanel.setLayout(new FlowLayout());
            userInput = new JTextField(20);
            loginPanel.add(userLabel);
            loginPanel.add(userInput);
            passwordInput = new JTextField(20);
            loginPanel.add(passwordLabel);
            loginPanel.add(passwordInput);
            loginPanel.add(loginEnter);
            /**********register panel***********/
            registerPanel = new JPanel();
            registerPanel.setMaximumSize(new Dimension(DEFAULT_FRAME_SIZE_WIDTH, 100));
            userLabel_register = new JLabel("User ID: ");
            passwordLabel_register = new JLabel("Password: ");
            registerEnter = new JButton("Register");
            registerEnter.addActionListener(this);
            
            registerPanel.setLayout(new FlowLayout());
            userInput_register = new JTextField(20);
            registerPanel.add(userLabel_register);
            registerPanel.add(userInput_register);
            passwordInput_register = new JTextField(20);
            registerPanel.add(passwordLabel_register);
            registerPanel.add(passwordInput_register);
            registerPanel.add(registerEnter);
            registerPanel.setVisible(false);
            /**********add everything into the panel***********/
            loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.add(Box.createRigidArea(new Dimension(100, 20)));
            this.add(loginLabel);
            this.add(Box.createRigidArea(new Dimension(100, 10)));
            this.add(loginPanel);
            this.add(Box.createRigidArea(new Dimension(100, 50)));
            this.add(registerLabel);
            this.add(Box.createRigidArea(new Dimension(100, 10)));
            this.add(registerPanel);
            this.add(Box.createRigidArea(new Dimension(100, 20)));
            this.add(messageLabel);
        }
        //----------------------------------------------------------------------------
        public void setNextIndex(String index){
            this.nextPageIndex = index;
        }
        //----------------------------------------------------------------------------
        @Override
        public void actionPerformed(ActionEvent event){
            //catch unacceptable character in here; edit messagelabel's text
            if (event.getSource() instanceof JButton){
                JButton jButton = (JButton)event.getSource();
                if(jButton.equals(loginEnter)){
                    String userText = userInput.getText();
                    String passwordText = passwordInput.getText();
                    String loginStatus = user.userLogin(userText, passwordText);
                    if(loginStatus.equals(Const.LOGIN_ACCEPTED)){
                        changeCardPanel(this.nextPageIndex);
                    }else if (loginStatus.equals(Const.WRONG_PASSWORD)){  
                        messageLabel.setText("Wrong password. Please try again.");
                    }else if (loginStatus.equals(Const.NO_SUCH_USER_ID)){ 
                        messageLabel.setText("User ID not found. Please register or try again.");
                        registerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        registerPanel.setVisible(true);
                        registerLabel.setVisible(true);
                    }
                }
                if(jButton.equals(registerEnter)){
                    String userText = userInput_register.getText();
                    String passwordText = passwordInput_register.getText();
                    String registerStatus = user.registerUser(userText, passwordText);
                    if(registerStatus.equals(Const.LOGIN_ACCEPTED)){
                        changeCardPanel(this.nextPageIndex);
                    }else if (registerStatus.equals(Const.USER_ID_TAKEN)){
                        messageLabel.setText("User ID is taken. Please try another ID.");
                    }
                }
            }
            userInput.setText("");                  //clear all jtextfield 
            passwordInput.setText("");
            userInput_register.setText("");
            passwordInput_register.setText("");
            
        }
    }
//----------------------------------------------------------------------------
//inner class - AccessDeclinedPage, host is not active
//----------------------------------------------------------------------------
    private class AccessDeclinedPage extends JPanel{
        JLabel declinedMessage_label;
        AccessDeclinedPage(){
            this.setLayout(new BorderLayout());
            declinedMessage_label = new JLabel("Access Declined. Host is not active, please contact administrator for more information"
                                                   ,  SwingConstants.CENTER);
            this.add(declinedMessage_label, BorderLayout.CENTER);
        }
    }
}