import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Michelle
 * This is the display frame to define the outline layer of the display
 *
 */

public class MainFrame extends JFrame {


	private static MainFrame instance;
	public static void main(String[] args) {
	
				try {
					MainFrame frame = new MainFrame();
					JPanel searchPanel = new SearchPanel();
					frame.setContentPane(searchPanel);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

	}

	public MainFrame() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// sets the title and the boundaries of the frame
		setBounds(100, 100, 500, 500);
		setTitle("Grocery Price Analysis");
		// adds the first search panel to the frame
		JPanel searchPanel = new SearchPanel();
		getContentPane().add(searchPanel);
		instance = this;

	}
	// returns the main frame instance to the caller
	public static MainFrame getInstance() {
		return instance;
	}
	
	

}
