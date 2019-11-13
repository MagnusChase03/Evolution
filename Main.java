package evolution;

import javax.swing.JFrame;

public class Main extends JFrame {

	Main() {
		
		setTitle("Evolution");
		setSize(1024, 330);
		//setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Panel p = new Panel();
		add(p);
		new Thread(p).start();
		
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		
		new Main();
		
	}
	
}
