import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

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
public class HomePage extends JFrame {
        
    JPanel mainPanel;
    JTextField search;
    
    
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        HomePage frmae = new HomePage();
    }
//----------------------------------------------------------------------------
    public HomePage() {
        setTitle("Company Name");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        mainPanel.setLayout(null);
        
        JPanel topPanel = new JPanel();
        topPanel.setBounds(5, 5, 426, 37);
        topPanel.setBackground(Color.WHITE);
        mainPanel.add(topPanel);
        topPanel.setLayout(new FormLayout(new ColumnSpec[] {
            ColumnSpec.decode("146px"),
                ColumnSpec.decode("32px"),
                FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
                ColumnSpec.decode("96px"),},
                                          new RowSpec[] {
                                              FormSpecs.RELATED_GAP_ROWSPEC,
                                                  RowSpec.decode("19px"),}));
        
        JLabel lblNewLabel = new JLabel("Search");
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(lblNewLabel, "1, 2, right, center");
        
        search = new JTextField(30);
        topPanel.add(search, "4, 2, left, top");
        search.setColumns(10);
        
        JPanel midPanel = new JPanel();
        midPanel.setBounds(5, 40, 426, 175);
        midPanel.setBackground(Color.WHITE);
        mainPanel.add(midPanel);
        midPanel.setLayout(new FormLayout(new ColumnSpec[] {
            FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,},
                                          new RowSpec[] {
                                              FormSpecs.RELATED_GAP_ROWSPEC,
                                                  FormSpecs.DEFAULT_ROWSPEC,
                                                  FormSpecs.RELATED_GAP_ROWSPEC,
                                                  FormSpecs.DEFAULT_ROWSPEC,
                                                  FormSpecs.RELATED_GAP_ROWSPEC,
                                                  FormSpecs.DEFAULT_ROWSPEC,
                                                  FormSpecs.RELATED_GAP_ROWSPEC,
                                                  FormSpecs.DEFAULT_ROWSPEC,
                                                  FormSpecs.RELATED_GAP_ROWSPEC,
                                                  FormSpecs.DEFAULT_ROWSPEC,
                                                  FormSpecs.RELATED_GAP_ROWSPEC,
                                                  FormSpecs.DEFAULT_ROWSPEC,}));
        
        JButton btnNewButton = new JButton("Price Analysis");
        midPanel.add(btnNewButton, "8, 4, 1, 7");
        
        JButton btnNewButton_1 = new JButton("Group Order");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        midPanel.add(btnNewButton_1, "12, 4, 1, 7");
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBounds(5, 213, 426, 50);
        bottomPanel.setBackground(Color.WHITE);
        mainPanel.add(bottomPanel);
        
        JButton btnNewButton_2 = new JButton("Account Settings");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        JButton btnNewButton_3 = new JButton("Review Group Order");
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        GroupLayout gl_bottomPanel = new GroupLayout(bottomPanel);
        gl_bottomPanel.setHorizontalGroup(
                                          gl_bottomPanel.createParallelGroup(Alignment.LEADING)
                                              .addGroup(gl_bottomPanel.createSequentialGroup()
                                                            .addGap(61)
                                                            .addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
                                                            .addGap(36)
                                                            .addComponent(btnNewButton_3, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
                                                            .addGap(58))
                                         );
        gl_bottomPanel.setVerticalGroup(
                                        gl_bottomPanel.createParallelGroup(Alignment.LEADING)
                                            .addGroup(gl_bottomPanel.createSequentialGroup()
                                                          .addGap(7)
                                                          .addGroup(gl_bottomPanel.createParallelGroup(Alignment.BASELINE)
                                                                        .addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(btnNewButton_3, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)))
                                       );
        bottomPanel.setLayout(gl_bottomPanel); 
    }
//----------------------------------------------------------------------------
//inner class - SearchItemPage
//----------------------------------------------------------------------------
    private class SearchItemPage{
        SearchItemPage(){
            
        
        }
    }
//----------------------------------------------------------------------------
//inner class - PriceAnalysisPage
//----------------------------------------------------------------------------
    private class PriceAnalysisPage{
        PriceAnalysisPage(){
            
        
        }
    }
//----------------------------------------------------------------------------
//inner class - GroupOrderPage
//----------------------------------------------------------------------------
    private class GroupOrderPage{
        GroupOrderPage(){
            
        
        }
    }
}
