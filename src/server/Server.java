package server;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.util.Date;
import java.util.HashMap;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
@SuppressWarnings("serial")
public class Server extends JFrame{

	private ServerSocket servSocket;
	private int connectionCount, nowInputCount;
	private ConnectionThread[] connections = new ConnectionThread[2];
	private HashMap<Socket,String> map = new HashMap<Socket, String>();
	private JTextArea textArea = new JTextArea();
	// filelist is the list of all img files under unknown
	private File[] filelist = new File("img").listFiles();
	// the file now showing on the screen
	private File nowFile;
	private Random random = new Random();
	private Date date = new Date();
	private Set<Integer> set = new HashSet<>();
	private ArrayList<String> nowFileList=new ArrayList<String>(); 
	
	Server(int portNum){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300 , 200);
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(new BoxLayout(this.getContentPane() , BoxLayout.PAGE_AXIS));
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.setPreferredSize(new Dimension(300 , 200));
		JScrollPane scrollPane = new JScrollPane(this.textArea);
		this.add(scrollPane);
		
		nowFile = filelist[random.nextInt(filelist.length)];
		
		try {
			servSocket = new ServerSocket(portNum);
			StringBuilder builder = new StringBuilder(date.toString() + "\n");
			builder.append("Server starts to listening on port ").append(portNum).append("\n");
			textArea.append(builder.toString());
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public void runForever(){
		while(true){
			try{
				if(connectionCount < 1) textArea.append("Server is waiting for cilent.\n");
				else return;
				
				Socket connectSocket = servSocket.accept();
				ConnectionThread connectThread = new ConnectionThread(connectSocket);
				connectThread.start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	class ConnectionThread extends Thread{
		private Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;
		
		public ConnectionThread(Socket socket){
			this.socket = socket;
			try{
				reader = new BufferedReader (new InputStreamReader(this.socket.getInputStream() ) );
				writer = new PrintWriter (new OutputStreamWriter(this.socket.getOutputStream() ) );
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
		public Socket getSocket(){
			return socket;
		}
		
		public void sendMessage(String str){
			writer.println(str);
			writer.flush();
		}
		
		public void run() {
			while(connectionCount < 1){
				try{
					Thread.sleep(100);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			sendMessage(nowFile.getName());
			
			while(true){
				try{
					String line = reader.readLine();
					nowInputCount++;
					if(nowInputCount == 1){
						nowFile = filelist[random.nextInt(filelist.length)];
						sendMessage(nowFile.getName());
					}else sendMessage("Error");
					
					nowInputCount = 0;
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	
	}
	// create server
	public static void main(String[] args) {
		Server server = new Server(8000);
		server.runForever();
	}
	
}