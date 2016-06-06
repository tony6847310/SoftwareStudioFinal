import processing.core.PApplet;
import processing.core.PImage;
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
	private Photo leftPhoto, rightPhoto;
	private Option chosenOption;
	private Help help;
	//in-game data
	private int score;
	private int lives;
	private boolean newRound;
	protected static float optionWidth = 120, optionHeight = 120;
	protected static float optionAnchor_X = (windowWidth/4 - optionWidth)/2; 
	protected static float optionAnchor_Y = windowHeight - optionHeight - 60;
	protected static float optionGap=windowWidth/4;
	protected static float photoWidth = 250, photoHeight = 250;
	protected static float photoAnchor_X = 175, photoAnchor_Y= 150;
	protected static float photoGap = 400;
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
		//load and set data
		loadData();
		leftPhoto = photos.get(0);
		rightPhoto = photos.get(1);
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
		seqPhoto.add(Ani.to(leftPhoto, (float)1.5 , "cur_Y", photoAnchor_Y, Ani.QUART_OUT) );
		seqPhoto.add(Ani.to(rightPhoto, (float)1.5 , "cur_Y", photoAnchor_Y, Ani.QUART_OUT) );
		seqPhoto.beginStep();
			//step 1
		seqPhoto.add(Ani.to(leftPhoto, (float)1.5 , "cur_Y", 800, Ani.QUART_IN) );
		seqPhoto.add(Ani.to(rightPhoto, (float)1.5 , "cur_Y", 800, Ani.QUART_IN) );
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
				Option o = options.get(i);
				if(mouseY >  o.getOriY() && mouseY < o.getOriY() + optionHeight){
					if(mouseX > o.getOriX() && mouseX < o.getOriX() + optionWidth){
						hoverOverOption = true;
						chosenOption = o;
						float x = chosenOption.getOriX() +10, y = chosenOption.getOriY()+10;
						chosenOption.setCurPos(x, y);
						break;
					}
				} else {
					o.resetPos();
				}
			}
			//show selected photo-set
			for(int i=0; i<2 ;i++){
				photos.get(i).display();
			}
			//draw option slots
			fill(204, 230, 255);
			stroke(153, 206, 255);
			strokeWeight(3);
			rect(0, windowHeight - 200, windowWidth, 200);
			//show selected option-set
			for(int i=0 ;i<4 ;i++){
				options.get(i).display();
			}
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
		
		for(int i=0 ; i<4 ;i++){
			//temporary, set option4 ~ option7 as options
			int index = i+4;
			Option o = new Option(this);
			PImage pi = loadImage("res/option_" + index +".jpg");
			o.setImage(pi);
			o.setSize(optionWidth, optionHeight);
			o.setOriPos(optionAnchor_X + optionGap * i, optionAnchor_Y);
			o.resetPos();
			options.add(o);
		}
		
		for(int i=0 ; i<2; i++){
			//should modify file path later
			//temporary, set option1 & option2 as photos
			int index = i+1; 
			Photo p = new Photo(this);
			PImage pi = loadImage("res/option_" + index +".jpg");
			p.setImage(pi);
			p.setSize(photoWidth, photoHeight);
			p.setOriPos(photoAnchor_X + photoGap * i, 800); //y -> below bottom of the window
			p.resetPos();
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
	
	public void mouseDragged(){
		if(state == STATE.START){
			if(hoverOverOption){
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
