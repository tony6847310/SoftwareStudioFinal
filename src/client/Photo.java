package client;
import processing.core.PImage;

public class Photo {
	private Gamestage parent;
	private PImage photoImg;
	private float cur_X, cur_Y;
	private float ori_X, ori_Y;
	private float width, height;
	
	public Photo(Gamestage gs){
		parent = gs;
	}
	
	public void display(){
		parent.image(photoImg, cur_X, cur_Y, width, height);
	}
	
	public void setImage(PImage pi){
		photoImg = pi;
	}
	
	public void setSize(float w, float h){
		width = w;
		height = h;
	}
	
	public void setOriPos(float x, float y){
		ori_X = x;
		ori_Y = y;
	}
	
	public void setCurPos(float x, float y){
		cur_X = x;
		cur_Y = y;
	}
	
	public void resetPos(){
		cur_X = ori_X;
		cur_Y = ori_Y;
	}
	
	public void resetSize(){
		width = Gamestage.photoWidth;
		height = Gamestage.photoHeight;
	}
	
	public float getCurX(){
		return cur_X;
	}
	
	public float getCurY(){
		return cur_Y;
	}
	
	public float getOriX(){
		return ori_X;
	}
	
	public float getOriY(){
		return ori_Y;
	}
	
	public void hide(){
	}
}
