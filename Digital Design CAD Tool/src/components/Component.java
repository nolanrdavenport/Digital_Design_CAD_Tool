package components;

import javafx.scene.image.Image;

public abstract class Component {
	public double xPos, yPos;
	public String rotation; // options: "right", "left", "up", "down"
	public String id;
	public Image image;
	public boolean selected;
	public int width, height;
	public Component(double xPos, double yPos, String rotation) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rotation = rotation;
		this.selected = false;
	}
	
	public boolean insideBounds(double mouseX, double mouseY) {
		if(mouseX > xPos && mouseX < xPos + width && mouseY > yPos && mouseY < yPos + height) {
			selected = true;
		}
		
		return selected;
	}
	
	public void setX(double x) {
		xPos = x;
	}
	public void setY(double y) {
		yPos = y;
	}
	public double getX() {
		return xPos;
	}
	public double getY() {
		return yPos;
	}
	
	
}
