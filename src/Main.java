import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Scanner;

public class Main {
	private static GamePanel gamepanel;
	public Main() {
		JFrame frame = new JFrame();
		gamepanel = new GamePanel();
		frame.add(gamepanel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("DJIKSTRAPROJECT");
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
	}
	
	public static void main(String[] args) {
		new Main();
		Scanner scanner = new Scanner(System.in);
                String readString = scanner.nextLine();
                while(readString != null) {
			if (readString.isEmpty()) {
				//System.out.println("Read Enter Key.");
				gamepanel.Dijkstra();
			} else { readString = null; }
		}
	}

}
