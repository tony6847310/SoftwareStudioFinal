
public class Help {
	private float windowWidth = MyWindow.windowWidth, windowHeight = MyWindow.windowHeight;
	private Gamestage parent;
	
	public Help(Gamestage gs){
		parent = gs;
	}
	public void display(){
		parent.fill(0, 0, 128);
		parent.textSize(50);
		parent.text("About", windowWidth/2, windowHeight/4);
		parent.text("How to play", windowWidth/2, windowHeight * 2/4);
	}
}
