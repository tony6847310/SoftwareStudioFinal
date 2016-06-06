package client;

import javax.swing.*;
public class Client {
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MyWindow window = new MyWindow();
			}
			
		});
	}
}
