package client;
import processing.core.PApplet;
import processing.core.PImage;
import controlP5.ControlP5;
import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;

import java.util.ArrayList;
import java.awt.event.KeyEvent;

import gifAnimation.*;
import ddf.minim.*;;

public class Gamestage extends PApplet{
	private static int windowWidth = MyWindow.windowWidth, windowHeight = MyWindow.windowHeight;
	//declare resource objects
	private ArrayList<Option> options;
	private ArrayList<Photo> photos;
	private Photo leftPhoto, rightPhoto;
	private Option chosenOption;
	private Help help;
	private PImage bg, title;
	//in-game data
	private int score;
	private int lives;
	private boolean newRound;
	private int chosenOptionIndex;
	private int answerIndex;
	private float secret_X, secret_Y;
	protected static float optionWidth = 120, optionHeight = 120;
	protected static float optionAnchor_X = (windowWidth/4 - optionWidth)/2; 
	protected static float optionAnchor_Y = windowHeight - optionHeight - 60;
	protected static float optionGap = windowWidth/4;
	protected static float photoWidth = 220, photoHeight = 220;
	protected static float photoAnchor_X = (windowWidth/2 - photoWidth)/2, photoAnchor_Y = 150;
	protected static float photoGap = windowWidth/2;
	//choose which set to show 
	private int set;
	private int caseIndex;
	private int caseLength;
	//other tools
	private ControlP5 cp5;
	private Minim mn;
	private AudioPlayer correct;
	private AudioPlayer wrong;
	private AudioPlayer musicMenu;
	private AudioPlayer musicStart;
	private AudioPlayer musicJohn;
	//states
	private enum STATE{
		MENU, START, HELP, END, NAME
	};
	private STATE state = STATE.MENU;
	//animation control
	AniSequence seqPhoto;
	AniSequence seqLabel;
	//condition tags
	protected boolean pause;
	protected boolean hoverOverOption;
	protected boolean pressed;
	protected boolean clickedOption;
	private Gif myAnimation;
	
	//PImage[] allFrames = Gif.getPImages(this, "res/bg.gif");
	
	public  void setup(){
		size(windowWidth, windowHeight);
		myAnimation = new Gif(this, "res/bg.gif");
		//create objects
		options = new ArrayList<Option>();
		photos = new ArrayList<Photo>();
		help = new Help(this);
		//other parameters
		score = 0;
		lives = 4;
		pause = false;
		newRound = true;
		hoverOverOption = false;
		set = 1;
		caseIndex = 1;
		caseLength = 8;
		secret_X = windowWidth/2 - photoWidth/2;
		secret_Y = 800;
		//load and set data
		loadData();
		setAnswer();
		leftPhoto = photos.get(0);
		rightPhoto = photos.get(1);
		bg = loadImage("res/bg.jpg");
		title = loadImage("res/title.png");
		//cp5 settings
		cp5 = new ControlP5(this);
		PImage[] imgs1 = {loadImage("res/start_btn.png"),loadImage("res/start_hover.png"),loadImage("res/start_btn.png")};
		cp5.addButton("startBtn")
			.setPosition(windowWidth/4 - 100, 150+windowHeight * 1/8)
			.setImages(imgs1)
			.setSize(160, 80)
			;
		PImage[] imgs2 = {loadImage("res/help_btn.png"),loadImage("res/help_hover.png"),loadImage("res/start_btn.png")};
		cp5.addButton("helpBtn")
			.setPosition(windowWidth/4 - 100, 150+windowHeight * 2/8)
			.setImages(imgs2)
			.setSize(160, 80)
			;
		PImage[] imgs3 = {loadImage("res/exit_btn.png"),loadImage("res/exit_hover.png"),loadImage("res/start_btn.png")};
		cp5.addButton("quitBtn")
			.setPosition(windowWidth/4 - 100, 150+windowHeight * 3/8)
			.setImages(imgs3)
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
		
		seqLabel = new AniSequence(this);
		seqLabel.beginSequence();
		seqLabel.add(Ani.to(cp5, (float)1.5 , "cur_Y", photoAnchor_Y, Ani.QUART_OUT) );
		//seqLabel.add(Ani.to(rightPhoto, (float)1.5 , "cur_Y", photoAnchor_Y, Ani.QUART_OUT) );
		seqLabel.beginStep();		
		//seqPhoto.add(Ani.to(leftPhoto, (float)1.5 , "cur_Y", 800, Ani.QUART_IN) );
		seqLabel.add(Ani.to(cp5, (float)1.5 , "cur_Y", 800, Ani.QUART_IN) );
		seqLabel.endStep();
		seqLabel.endSequence();
		//text align
		textAlign(CENTER);
		
		myAnimation.play();
		mn = new Minim(this);
		correct = mn.loadFile(this.getClass().getResource("/res/correct.mp3").getPath());
		wrong = mn.loadFile(this.getClass().getResource("/res/wrong.mp3").getPath());
		musicMenu = mn.loadFile(this.getClass().getResource("/res/bg-00.mp3").getPath());
		musicStart = mn.loadFile(this.getClass().getResource("/res/bg-01.mp3").getPath());
		musicJohn = mn.loadFile(this.getClass().getResource("/res/bg-02.mp3").getPath());
	}
	
	public void draw(){
		//set background color
		background(242, 196, 58);
		if(state == STATE.MENU){
			cp5.setVisible(true);
			seqLabel.start();
			fill(0, 0, 128);
			textSize(70);
			//image(bg, windowWidth/4-50, 100, 750, 750);	
			image(myAnimation,100,100);
			image(title, 50, 0, 320, 300);
			//text("Final : The Game", windowWidth/2, 120);
			//reset in-game data
			score = 0;
			lives = 5;
			if(!musicMenu.isPlaying()){
				musicMenu.play();
			}
		}else if(state == STATE.START){
			//detect remain lives
			if(lives <= 0){
				if(caseIndex == 3){
					if(seqPhoto.isEnded()){
						PImage secret = loadImage("case3/02-07.png");
						Ani.to(this, (float)3.0, "secret_Y", windowHeight/2 - photoHeight);
						state = STATE.MENU;
					}
				}else{
					state = STATE.MENU;
				}
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
				if(mouseY >  o.getOriY() && mouseY < o.getOriY() + optionHeight && 
				mouseX > o.getOriX() && mouseX < o.getOriX() + optionWidth){
					hoverOverOption = true;
					chosenOption = o;
					chosenOptionIndex = i+1;
					float x = chosenOption.getOriX() +10, y = chosenOption.getOriY()+10;
					chosenOption.setCurPos(x, y);
					break;
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
				//reload data, change set
				set++;
				if(set > caseLength)
					set = 1;
				loadData();
				setAnswer();
				seqReset();
			}
			
			if(!musicStart.isPlaying())
				musicStart.play();
		}else if(state == STATE.HELP){
			cp5.setVisible(false);
			help.display();
		}else if(state == STATE.END){
			//TO-DO
		}
		//image(myAnimation, 10,10);
	}
	
	private void loadData(){
		options.clear();
		photos.clear();
		
		for(int i=0 ; i<4 ;i++){
			//temporary, set option4 ~ option7 as options
			int index = i+3;
			Option o = new Option(this);
			PImage pi = loadImage("case"+ caseIndex +"/0" + set +"-0"+ index +".png");
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
			PImage pi = loadImage("case" + caseIndex + "/0" + set + "-0" + index +".png");
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
			if(!pause){
				if(hoverOverOption){
					clickedOption = true;
					if(newRound){
						if(chosenOptionIndex == answerIndex){
							addScore();
							correct.play();
						}else{
							wrong.play();
						}
						newRound = false;
					}
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
			}else if(keyCode == KeyEvent.VK_P){
				pause  = !pause;
				if(pause){
					seqPhoto.pause();
				}else{
					seqPhoto.resume();
				}
			}
		}else if(state == STATE.MENU){
			if(keyCode == KeyEvent.VK_ENTER){
				state = STATE.START;
				seqPhoto.start();
			}else if(keyCode == KeyEvent.VK_1){
				caseIndex = 1;
				caseLength = 8;
				set = 1;
				loadData();
				setAnswer();
				seqReset();
			}else if(keyCode == KeyEvent.VK_2){
				caseIndex = 2;
				caseLength = 1;
				set = 1;
				loadData();
				setAnswer();
				seqReset();
			}else if(keyCode == KeyEvent.VK_3){
				caseIndex = 3;
				caseLength = 2;
				set = 1;
				loadData();
				setAnswer();
				seqReset();
			}
		}else if(state == STATE.HELP){
			if(keyCode == KeyEvent.VK_ENTER){
				state = STATE.START;
				seqPhoto.start();
			}else if(keyCode == KeyEvent.VK_ESCAPE){
				this.exit();
			}else if(keyCode == KeyEvent.VK_BACK_SPACE){
				state = STATE.MENU;
			}
		}
	}
	//input username
	/*public class Username extends PApplet{
		private ControlP5 cp5;
		public void setup(){
			size(200, 120);
			cp5 = new ControlP5(this);
			cp5.addTextfield("Name")
				.setPosition(100, 20)
				.setSize(150, 50)
				.setAutoClear(false);
			cp5.addBang("Submit")
				.setPosition(100, 100)
				.setSize(75,30);
			textAlign(CENTER);
		}
		public void draw(){
			//
		}
	}*/
	//ControlP5 buttons
	public void startBtn(){
		if(state == STATE.MENU){
			state = STATE.START;
			seqPhoto.start();
			musicStart.play();
			musicMenu.close();
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
	public void seqReset(){
		leftPhoto = photos.get(0);
		rightPhoto = photos.get(1);
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
		seqPhoto.start();
	}
	//game-control methods
	public void addScore(){
		score++;
	}
	public void setAnswer(){
		if(caseIndex == 1){
			if(set == 1)
				answerIndex = 2;
			else if(set == 2)
				answerIndex = 4;
			else if(set == 3)
				answerIndex = 1;
			else if(set == 4)
				answerIndex = 1;
			else if(set == 5)
				answerIndex = 4;
			else if(set == 6)
				answerIndex = 1;
			else if(set == 7)
				answerIndex = 4;
			else if(set == 8)
				answerIndex = 2;
		}else if(caseIndex == 2){
			answerIndex = 3;
		}else if(caseIndex == 3){
			if(set == 1){
				answerIndex = 4;
			}else{
				answerIndex = 7;
			}
		}
	}
}
