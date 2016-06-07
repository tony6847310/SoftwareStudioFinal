package client;

import processing.core.PImage;

public class Help {
	private float windowWidth = MyWindow.windowWidth, windowHeight = MyWindow.windowHeight;
	private Gamestage parent;
	private PImage inst;
	
	public Help(Gamestage gs){
		parent = gs;
		inst = parent.loadImage("res/help.png");
	}
	public void display(){
		parent.fill(0, 0, 128);
		parent.textSize(50);
		parent.text("How to play", windowWidth/2, windowHeight * 1/4);
		parent.image(inst, windowWidth/2 - 400, 200, 800, 600);
	}
}
