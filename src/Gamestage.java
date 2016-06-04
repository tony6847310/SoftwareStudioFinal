import processing.core.PApplet;
import controlP5.ControlP5;
import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;

import java.util.ArrayList;
import java.awt.event.KeyEvent;

public class Gamestage extends PApplet{
	private static int windowWidth = MyWindow.windowWidth, windowHeight = MyWindow.windowHeight;
	//declare resource objects
	private ArrayList<Option> options;
	private ArrayList<Photo> photos;
	private Photo selectedPhotos;
	private Help help;
	//in-game data
	private int score;
	private int lives;
	private boolean newRound;
	//choose which set to show 
	private int optionSet;
	private int photoSet;
	//other tools
	private ControlP5 cp5;
	//mouse interactions
	private enum STATE{
		MENU, START, HELP, END
	};
	private STATE state = STATE.MENU;
	//animation control
	AniSequence seqPhoto;
	protected int chosenOption;
	protected boolean hoverOverOption;
	protected boolean pressed;
	protected boolean clickedOption;
	
	public  void setup(){
		size(windowWidth, windowHeight);
		//create objects
		options = new ArrayList<Option>();
		photos = new ArrayList<Photo>();
		help = new Help(this);
		//other parameters
		score = 0;
		lives = 4;
		newRound = true;
		hoverOverOption = false;
		optionSet = 0;
		photoSet = 0;
		chosenOption = 0;
		//load and set data
		loadData();
		selectedPhotos = photos.get(photoSet);
		//cp5 settings
		cp5 = new ControlP5(this);
		cp5.addButton("startBtn")
			.setLabel("Start")
			.setPosition(windowWidth/2 - 100, windowHeight * 1/4)
			.setSize(160, 80);
		cp5.addButton("helpBtn")
			.setLabel("Help")
			.setPosition(windowWidth/2 - 100, windowHeight * 2/4)
			.setSize(160, 80);
		cp5.addButton("quitBtn")
			.setLabel("Quit")
			.setPosition(windowWidth/2 - 100, windowHeight * 3/4)
			.setSize(160, 80);
		//animation settings
		Ani.init(this);
		seqPhoto = new AniSequence(this);
		seqPhoto.beginSequence();
			//step 0
		seqPhoto.add(Ani.to(selectedPhotos, (float)1.5 , "photo_Y", Photo.photoAnchor_Y, Ani.QUART_OUT) );
		seqPhoto.beginStep();
			//step 1
		seqPhoto.add(Ani.to(selectedPhotos, (float)1.5 , "photo_Y", 800, Ani.QUART_IN) );
		seqPhoto.endStep();
		seqPhoto.endSequence();
		//text align
		textAlign(CENTER);
	}
	
	public void draw(){
		//set background color
		background(255, 255, 153);
		if(state == STATE.MENU){
			cp5.setVisible(true);
			fill(0, 0, 128);
			textSize(70);
			text("Final : The Game", windowWidth/2, 120);
			seqPhoto.pause();
			//reset in-game data
			score = 0;
			lives = 3;
		}else if(state == STATE.START){
			//detect remain lives
			if(lives <= 0){
				state = STATE.MENU;
			}
			//hide buttons after starting the game
			cp5.setVisible(false);
			//display current score
			fill(255, 128, 0);
			textSize(30);
			text("Score: " + score ,windowWidth - 150, 60);
			//display lives remain
			text("Lives: " + lives, 100, 60);
			//detect mouse pointing on options
			for(int i=0 ; i<4 ;i++){
				if(mouseY > Option.anchor_Y && mouseY < Option.anchor_Y+Option.optionHeight){
					if(mouseX > (Option.anchor_X + Option.gap * i) && mouseX < (Option.anchor_X + Option.gap * i + Option.optionWidth)){
						hoverOverOption = true;
						chosenOption = i;
						break;
					}
				} else {
					hoverOverOption = false;
				}
			}
			//show selected photo-set
			photos.get(photoSet).display();
			
			//draw option slots
			fill(204, 230, 255);
			stroke(153, 206, 255);
			strokeWeight(3);
			rect(0, windowHeight - 200, windowWidth, 200);
			//show selected option-set
			options.get(optionSet).display();
			//start photo ani
			if(seqPhoto.isEnded()){
				if(!clickedOption){
					lives--;
				}
				clickedOption = false;
				newRound = true;
				seqPhoto.start();
			}else if(!seqPhoto.isPlaying()){
				seqPhoto.start();
			}
		}else if(state == STATE.HELP){
			cp5.setVisible(false);
			help.display();
			seqPhoto.pause();
		}else if(state == STATE.END){
			//TO-DO
		}
	}
	
	private void loadData(){
		for(int i=0 ; i<1 ;i++){
			//should modify file path later
			Option o = new Option(this);
			options.add(o);
		}
		
		for(int i=0 ; i<1; i++){
			//should modify file path later
			Photo p = new Photo(this);
			photos.add(p);
		}
	}
	
	public void mousePressed(){
		pressed = true;
	}
	
	public void mouseReleased(){
		pressed = false;
	}
	
	public void mouseClicked(){
		if(state == STATE.START){
			if(hoverOverOption){
				clickedOption = true;
				if(newRound){
					addScore();
					newRound = false;
				}
			}
		}
	}
	
	public void keyPressed(){
		if(state == STATE.START){
			if(keyCode == KeyEvent.VK_SPACE){
				seqPhoto.start();
			}else if(keyCode == KeyEvent.VK_BACK_SPACE){
				state = STATE.MENU;
			}
		}else if(state == STATE.MENU){
			if(keyCode == KeyEvent.VK_ENTER){
				state = STATE.START;
			}
		}else if(state == STATE.HELP){
			if(keyCode == KeyEvent.VK_ENTER){
				state = STATE.START;
			}else if(keyCode == KeyEvent.VK_ESCAPE){
				this.exit();
			}else if(keyCode == KeyEvent.VK_BACK_SPACE){
				state = STATE.MENU;
			}
		}
	}
	//ControlP5 buttons
	public void startBtn(){
		if(state == STATE.MENU){
			state = STATE.START;
		}
	}
	
	public void helpBtn(){
		if(state == STATE.MENU){
			state = STATE.HELP;
		}
	}
	public void quitBtn(){
		if(state == STATE.MENU){
			this.exit();
		}
	}
	//animations
	public void seqEnd(){
		seqPhoto.start();
	}
	//game-control methods
	public void addScore(){
		score++;
	}
}
