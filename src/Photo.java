import processing.core.PImage;

public class Photo {
	private Gamestage parent;
	
	protected PImage left, right;
	protected static float photoWidth, photoHeight;
	protected static float photoAnchor_X, photoAnchor_Y;
	protected static float photoGap;
	protected float photo_X, photo_Y;
	
	public Photo(Gamestage gs){
		parent = gs;
		photoWidth = 250;
		photoHeight = 250;
		photoAnchor_X = 175;
		photoAnchor_Y = 150;
		photo_X = photoAnchor_X;
		photo_Y = 800;
		photoGap = 100;
		
		//temporary, select option1 & option2 as photo
		left = parent.loadImage("res/option_1.jpg");
		right = parent.loadImage("res/option_2.jpg");
		
	}
	
	public void display(){
		parent.image(left, photo_X, photo_Y, photoWidth, photoHeight);
		parent.image(right, photo_X +photoWidth + photoGap, photo_Y, photoWidth, photoHeight);
	}
}
