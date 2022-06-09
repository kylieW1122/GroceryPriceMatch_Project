import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.Window.Type;

public class MainFrame extends JFrame {


	private static MainFrame instance;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	
				try {
					MainFrame frame = new MainFrame();
					JPanel searchPanel = new SearchPanel();
				//	frame.setContentPane(searchPanel);
					frame.add(searchPanel, BorderLayout.CENTER);
			//		frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	setBounds(100, 100, 425, 250);
		
		setBounds(100, 100, 500, 500);
		setTitle("Grocery Price Analysis");

		instance = this;

	}
	
	public static MainFrame getInstance() {
		return instance;
	}
	
	

}
