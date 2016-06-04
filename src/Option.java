import java.util.ArrayList;
import processing.core.PImage;

public class Option {
	private static int windowWidth = MyWindow.windowWidth, windowHeight = MyWindow.windowHeight;
	private ArrayList<PImage> imgs;
	private Gamestage parent;
	protected static float optionWidth, optionHeight;
	protected static float anchor_X, anchor_Y;
	protected static float gap;
	
	public Option(Gamestage gs){
		imgs = new ArrayList<PImage>();
		parent = gs;
		optionWidth = 120;
		optionHeight = 120;
		anchor_X = (windowWidth/4 - optionWidth)/2;
		anchor_Y = windowHeight - optionHeight - 60;
		gap = windowWidth/4;
		
		
		for(int i=0 ; i<4 ;i++){
			//temporary, set option4 ~ option7 as options
			int index = i+4;
			PImage pi = parent.loadImage("res/option_" + index +".jpg");
			imgs.add(pi);
		}
	}
	
	public void display(){
		int count = 0;
		for(PImage i : imgs){
			//if mouse hover over an option
			if(parent.hoverOverOption && parent.chosenOption == count){
				if(parent.pressed){
					//mouse pressed on the option
					parent.image(i, anchor_X + gap*count + 8, anchor_Y - 5, optionWidth, optionHeight);
					
				} else {
					parent.image(i, anchor_X + gap*count + 15, anchor_Y - 15, optionWidth, optionHeight);
				}
				
			}else{
				parent.image(i, anchor_X + gap*count, anchor_Y, optionWidth, optionHeight);
			}
			count++;
		}
	}
}
