package client;
import javax.swing.JFrame;

public class MyWindow extends JFrame{
	public final static int windowWidth = 1000, windowHeight = 750;
	
	public static void main(String[] args){
		Gamestage gs = new Gamestage();
		gs.init();
		gs.start();
		gs.setFocusable(true);
		
		JFrame window = new JFrame("Default");
		window.setContentPane(gs);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(windowWidth, windowHeight);
		window.setVisible(true);
	}
}
